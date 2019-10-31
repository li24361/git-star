package com.lzh.gitstar.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lzh.gitstar.common.FormulaConst;
import com.lzh.gitstar.domain.dto.Index;
import com.lzh.gitstar.domain.dto.SearchQuery;
import com.lzh.gitstar.domain.entity.UserIndex;
import com.lzh.gitstar.domain.graphql.ContributionsCollection;
import com.lzh.gitstar.domain.graphql.JsonRootBean;
import com.lzh.gitstar.index.HIndexCalculator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
@Data
public class GithubSearchService {

//    @Value("${github.token}")
//    private String token;

    @Value("${github.endpoint}")
    private String endpoint;

    @Autowired
    private IndexService IndexService;

    @Autowired
    HIndexCalculator hIndexCalculator;


    private String search(String login, String token) {
        if (StringUtils.isEmpty(token)) {
            throw new JSONException("not found token, Please login");
        }
        String resource = new ClassPathResource("query.graphql").readUtf8Str();
        String graphql = StrUtil.format(resource, MapUtil.of("login", login)).replace("\"", "\\\"").replace(System.lineSeparator(), "\\n");
        String body = "{\"query\":\"" + graphql + "\"}";
        String result = HttpRequest.post(endpoint)
                .header("Authorization", "bearer " + token)
                .body(body)
                .execute()
                .body();
        log.info("result:{}", result);
        return result;
    }


    private UserIndex searchUserInfo(JsonRootBean jsonRootBean) {
        UserIndex index = new UserIndex();
        index.setFollower(jsonRootBean.getData().getUser().getFollowers().getTotalCount());
        index.setAvatarUrl(jsonRootBean.getData().getUser().getAvatarUrl());
        if (jsonRootBean.getData().getUser().getRepositories().getTotalCount() > 0) {
            index.setPrimaryLanguage(Optional.ofNullable(jsonRootBean.getData().getUser().getTopRepositories().getNodes().get(0).getPrimaryLanguage()).map(l -> l.getName()).orElse("Markdown"));
            index.setTopRepository(jsonRootBean.getData().getUser().getTopRepositories().getNodes().get(0).getNameWithOwner());
            index.setTopStar(jsonRootBean.getData().getUser().getTopRepositories().getNodes().get(0).getStargazers().getTotalCount());
        } else {
            index.setPrimaryLanguage("none");
            index.setTopRepository("none");
            index.setTopStar(0);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            index.setRepositoryHIndex(hIndexCalculator.calculate(jsonRootBean.getData().getUser().getRepositories()));
        });
        executorService.submit(() -> {
            index.setContributeRepositoryHIndex(hIndexCalculator.calculate(jsonRootBean.getData().getUser().getRepositoriesContributedTo()));
        });

        long contributeStar = jsonRootBean.getData().getUser().getRepositoriesContributedTo().getNodes().stream().mapToInt(nodes -> nodes.getStargazers().getTotalCount()).sum();
        long ownStar = jsonRootBean.getData().getUser().getRepositories().getNodes().stream().mapToInt(nodes -> nodes.getStargazers().getTotalCount()).sum();
        index.setOwnStar((int) ownStar);
        index.setContributeStar((int) contributeStar);
        Optional<ContributionsCollection> collectionOptional = Optional.ofNullable(jsonRootBean.getData().getUser().getContributionsCollection());
        index.setContributes(collectionOptional.map(c -> c.getAllContributions()).orElse(0));
        index.setContributeYears(collectionOptional.map(c -> c.getContributionYears()).map(y -> y.size()).orElse(0));
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("close thread poll error", e);
        }
        return index;
    }

    private JsonRootBean searchByGithub(SearchQuery searchQuery) {
        String result = search(searchQuery.getLogin(), searchQuery.getToken());
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (jsonObject.getJSONArray("errors") != null) {
            throw new JSONException(jsonObject.getJSONArray("errors").get(0).toString());
        }
        if (result.indexOf("NOT_FOUND") != -1) {
            throw new JSONException("user not exist!");
        }
        if (jsonObject.get("data") == null) {
            throw new JSONException("query error!");
        }
        return JSONUtil.toBean(result, JsonRootBean.class);
    }

    /**
     * 计算
     *
     * @param userIndex
     * @return
     */
    private Index calculateUserIndex(UserIndex userIndex) {
        Index index = new Index();
        index.setLogin(userIndex.getLogin());
        index.setAvatarUrl(userIndex.getAvatarUrl());
        index.setOwnStars(userIndex.getOwnStar());
        index.setFollowers(userIndex.getFollower());
        index.setPrimaryLanguage(userIndex.getPrimaryLanguage());
        index.setTopRepository(userIndex.getTopRepository());
        index.setContributeYears(userIndex.getContributeYears());
        index.setAllStarsScore(getScore(userIndex.getTopStar() + userIndex.getOwnStar(), FormulaConst.maxFollower));
        index.setFollowerScore(getScore(userIndex.getFollower(), FormulaConst.maxFollower));
        index.setRepositoryHIndexScore(getScore(userIndex.getRepositoryHIndex()));
        index.setContributeRepositoryHIndexScore(getScore(userIndex.getContributeRepositoryHIndex()));
        index.setContributesScore(getScore(userIndex.getContributes(), FormulaConst.maxComtribute));
        return index;
    }

    public Index handleUserIndex(SearchQuery searchQuery) {
        JsonRootBean jsonRootBean = searchByGithub(searchQuery);
        UserIndex userIndex = searchUserInfo(jsonRootBean);
        userIndex.setLogin(searchQuery.getLogin());
        Index index = calculateUserIndex(userIndex);
        index.setCreatedAt(DateUtil.format(jsonRootBean.getData().getUser().getCreatedAt(), "yyyy-MM-dd"));
        Executors.newSingleThreadExecutor().submit(() -> {
            userIndex.setAllScore(index.getScore());
            IndexService.saveIndex(userIndex);
        });
        return index;
    }

    private BigDecimal getScore(Integer value) {
        return getScore(value, 1, null);
    }

    private BigDecimal getScore(Integer value, Integer max) {
        return getScore(value, 2, max);
    }

    /**
     * @param value
     * @param type  1 arctan ，2 log10
     * @param max   log 最大值
     * @return
     */
    private BigDecimal getScore(Integer value, Integer type, Integer max) {
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
        BigDecimal hundred = BigDecimal.valueOf(100L);
        if (percent.compareTo(BigDecimal.ONE) >= 0) {
            return hundred;
        }
        return percent.multiply(hundred);
    }


}
