package com.lzh.gitstar.github;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String search(String login) {
        String resource = new ClassPathResource("query.graphql").readUtf8Str();
        String graphql = StrUtil.format(resource, MapUtil.of("login", login));

        log.info("request:{}", graphql);
        String result = HttpRequest.post(endpoint)
                .header("Authorization", "bearer " + token)
                .body(graphql)
                .execute()
                .body();
        log.info("result:{}",result);
        return result;
    }

}
