package com.lzh.gitstar.github;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.lzh.gitstar.domain.Index;
import com.lzh.gitstar.domain.graphql.JsonRootBean;
import com.lzh.gitstar.domain.graphql.Repositories;
import com.lzh.gitstar.index.GIndexCalculator;
import com.lzh.gitstar.index.HIndexCalculator;
import com.lzh.gitstar.index.IndexCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.LongBinaryOperator;

/**
 * @Author lizhihao
 * @Date 2019-10-12
 */
@Component
@Slf4j
public class GraphqlSearcher {

    @Value("${github.token}")
    private String token;

    @Value("${github.endpoint}")
    private String endpoint;

    @Autowired
    GIndexCalculator gIndexCalculator;

    @Autowired
    HIndexCalculator hIndexCalculator;

    private String search(String login) {
        String resource = new ClassPathResource("query.graphql").readUtf8Str();
        String graphql = StrUtil.format(resource, MapUtil.of("login", login)).replace("\"", "\\\"").replace(System.lineSeparator(), "\\n");
        String body = "{\"query\":\"" + graphql + "\"}";
        log.info("request:{}", body);
        String result = HttpRequest.post(endpoint)
                .header("Authorization", "bearer " + token)
                .body(body)
                .execute()
                .body();
        log.info("result:{}", result);

        return result;
    }

    public Index handleIndex(String login) {

        String result = search(login);
        JSONArray errors = JSONUtil.parseObj(result).getJSONArray("errors");
        if (errors!=null) {
            throw new RuntimeException(errors.get(0).toString());
        }
        JsonRootBean jsonRootBean = JSONUtil.toBean(result, JsonRootBean.class);
        Index index = new Index();
        index.setLogin(login);
        index.setFollower(Long.valueOf(jsonRootBean.getData().getUser().getFollowers().getTotalCount()));
        index.setAvatarUrl(jsonRootBean.getData().getUser().getAvatarUrl());
        index.setPrimaryLanguage(jsonRootBean.getData().getUser().getTopRepositories().getNodes().get(0).getPrimaryLanguage().getName());
        index.setTopRepository(jsonRootBean.getData().getUser().getTopRepositories().getNodes().get(0).getNameWithOwner());
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(()->{
            index.setRepositoryHIndex(hIndexCalculator.calculate(jsonRootBean.getData().getUser().getRepositories()) + "");
        });
        executorService.submit(()->{
            index.setRepositoryGIndex(gIndexCalculator.calculate(jsonRootBean.getData().getUser().getRepositories()) + "");
        });
        executorService.submit(()->{
            index.setContributeRepositoryHIndex(hIndexCalculator.calculate(jsonRootBean.getData().getUser().getRepositoriesContributedTo()) + "");
        });
        executorService.submit(()->{
            index.setContributeRepositoryGIndex(gIndexCalculator.calculate(jsonRootBean.getData().getUser().getRepositoriesContributedTo()) + "");
        });
        long contributeStar = jsonRootBean.getData().getUser().getRepositoriesContributedTo().getNodes().stream().mapToInt(nodes -> nodes.getStargazers().getTotalCount()).sum();
        long ownStar = jsonRootBean.getData().getUser().getRepositories().getNodes().stream().mapToInt(nodes -> nodes.getStargazers().getTotalCount()).sum();
        index.setAllStar(ownStar+contributeStar);
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("close thread poll error",e);
        }
        return index;
    }
}
