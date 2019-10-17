package com.lzh.gitstar.controller;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.StopWatch;
import com.lzh.gitstar.domain.Index;
import com.lzh.gitstar.domain.response.Response;
import com.lzh.gitstar.github.GraphqlSearcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private GraphqlSearcher graphqlSearcher;

    @Autowired
    private ConcurrentHashMap<String, Index> timedCache;

    @RequestMapping("/{login}")
    @ResponseBody
    public Response searchByLogin(@PathVariable String login) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Index hIndex = null;
        try {
            hIndex = graphqlSearcher.handleIndex(login);
        } catch (Exception e) {
            log.error("error", e);
            return Response.fail(e.getMessage());
        }
        timedCache.put(login, hIndex);
        stopWatch.stop();
        log.info("cost:{} second", stopWatch.getTotalTimeSeconds());
        return Response.ok(hIndex);
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<Index> list() {
        return timedCache.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

}
