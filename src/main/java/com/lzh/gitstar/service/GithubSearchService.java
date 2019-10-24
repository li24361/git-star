package com.lzh.gitstar.service;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.lzh.gitstar.common.FormulaConst;
import com.lzh.gitstar.domain.dto.Index;
import com.lzh.gitstar.domain.entity.UserIndex;
import com.lzh.gitstar.domain.graphql.ContributionsCollection;
import com.lzh.gitstar.domain.graphql.JsonRootBean;
import com.lzh.gitstar.index.GIndexCalculator;
import com.lzh.gitstar.index.HIndexCalculator;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author lizhihao
 * @Date 2019-10-12
 */
@Component
@Slf4j
public class GithubSearchService {

//    @Value("${github.token}")
//    private String token;

    @Value("${github.endpoint}")
    private String endpoint;

    @Autowired
    private IndexService IndexService;

    @Autowired
    HIndexCalculator hIndexCalculator;

    private String search(String login) {
        String resource = new ClassPathResource("query.graphql").readUtf8Str();
        String graphql = StrUtil.format(resource, MapUtil.of("login", login)).replace("\"", "\\\"").replace(System.lineSeparator(), "\\n");
        String body = "{\"query\":\"" + graphql + "\"}";
        log.info("request:{}", body);
        AuthUser principal = (AuthUser) SecurityUtils.getSubject().getPrincipal();
        String result = HttpRequest.post(endpoint)
                .header("Authorization", "bearer " + principal.getToken().getAccessToken())
                .body(body)
                .execute()
                .body();
        log.info("result:{}", result);
        return result;
    }

    private UserIndex searchUserInfo(String login) {
        String result = search(login);
        JSONArray errors = JSONUtil.parseObj(result).getJSONArray("errors");
        if (errors!=null) {
            throw new JSONException(errors.get(0).toString());
        }
        JsonRootBean jsonRootBean = JSONUtil.toBean(result, JsonRootBean.class);
        UserIndex index = new UserIndex();
        index.setLogin(login);
        index.setFollower(jsonRootBean.getData().getUser().getFollowers().getTotalCount());
        index.setAvatarUrl(jsonRootBean.getData().getUser().getAvatarUrl());
        index.setPrimaryLanguage(Optional.ofNullable(jsonRootBean.getData().getUser().getTopRepositories().getNodes().get(0).getPrimaryLanguage()).map(l-> l.getName()).orElse("Markdown"));
        index.setTopRepository(jsonRootBean.getData().getUser().getTopRepositories().getNodes().get(0).getNameWithOwner());
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(()->{
            index.setRepositoryHIndex(hIndexCalculator.calculate(jsonRootBean.getData().getUser().getRepositories()));
        });
        executorService.submit(()->{
            index.setContributeRepositoryHIndex(hIndexCalculator.calculate(jsonRootBean.getData().getUser().getRepositoriesContributedTo()));
        });

        long contributeStar = jsonRootBean.getData().getUser().getRepositoriesContributedTo().getNodes().stream().mapToInt(nodes -> nodes.getStargazers().getTotalCount()).sum();
        long ownStar = jsonRootBean.getData().getUser().getRepositories().getNodes().stream().mapToInt(nodes -> nodes.getStargazers().getTotalCount()).sum();
        index.setOwnStar((int) ownStar);
        index.setContributeStar((int) contributeStar);
        index.setTopStar(jsonRootBean.getData().getUser().getTopRepositories().getNodes().get(0).getStargazers().getTotalCount());
        Optional<ContributionsCollection> collectionOptional = Optional.ofNullable(jsonRootBean.getData().getUser().getContributionsCollection());
        index.setContributes(collectionOptional.map(c->c.getAllContributions()).orElse(0));
        index.setContributeYears(collectionOptional.map(c->c.getContributionYears()).map(y->y.size()).orElse(0));
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("close thread poll error",e);
        }
        return index;
    }

    private Index calculateUserIndex(UserIndex userIndex) {
        Index index = new Index();
        index.setLogin(userIndex.getLogin());
        index.setAvatarUrl(userIndex.getAvatarUrl());
        index.setPrimaryLanguage(userIndex.getPrimaryLanguage());
        index.setTopRepository(userIndex.getTopRepository());
        index.setContributeYears(userIndex.getContributeYears());
        index.setAllStarsPercent(getPercent(userIndex.getTopStar()+userIndex.getOwnStar(),FormulaConst.maxFollower));
        index.setFollowerPercent(getPercent(userIndex.getFollower(),FormulaConst.maxFollower));
        index.setRepositoryHIndexPercent(getPercent(userIndex.getRepositoryHIndex()));
        index.setContributeRepositoryHIndexPercent(getPercent(userIndex.getContributeRepositoryHIndex()));
        index.setContributesPercent(getPercent(userIndex.getContributes(),FormulaConst.maxComtribute));
        return index;
    }

    public Index handleUserIndex(String login) {
        UserIndex userIndex = this.searchUserInfo(login);
        Index index = calculateUserIndex(userIndex);
        Executors.newSingleThreadExecutor().submit(()->{
            userIndex.setAllScore(index.getScore());
            IndexService.saveIndex(userIndex);
        });
        return index;
    }

    private BigDecimal getPercent(Integer value) {
        return getPercent(value, 1, null);
    }

    private BigDecimal getPercent(Integer value, Integer max) {
        return getPercent(value, 2, max);
    }

    /**
     *
     * @param value
     * @param type 1 arctan ，2 log10
     * @param max log 最大值
     * @return
     */
    private BigDecimal getPercent(Integer value, Integer type, Integer max) {
        if (value < 1) {
            return BigDecimal.ZERO;
        }
        BigDecimal percent = BigDecimal.ZERO;
        if (type == 1 || type == null) {
            percent = new BigDecimal(Math.atan(value)).multiply(new BigDecimal("2")).divide(BigDecimal.valueOf(Math.PI), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            if (max == null) {
                max = FormulaConst.maxFollower;
            }
            percent = BigDecimal.valueOf(Math.log10(value)).divide(BigDecimal.valueOf(Math.log10(max)), 2, BigDecimal.ROUND_HALF_UP);

        }
        if (percent.compareTo(BigDecimal.ONE) >= 0) {
            return BigDecimal.ONE;
        }
        return percent;
    }


}
