package com.lzh.gitstar.controller;

import cn.hutool.core.date.StopWatch;
import com.lzh.gitstar.github.GraphqlSearcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private GraphqlSearcher graphqlSearcher;

    @RequestMapping("/{login}")
    @ResponseBody
    public String searchByLogin(@PathVariable String login) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String search = graphqlSearcher.search(login);
        stopWatch.stop();
        log.info("cost:{} second",stopWatch.getTotalTimeSeconds());
        return search;
    }
}
