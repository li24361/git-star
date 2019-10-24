package com.lzh.gitstar.controller;

import cn.hutool.core.date.StopWatch;
import com.lzh.gitstar.domain.dto.Index;
import com.lzh.gitstar.domain.entity.UserIndex;
import com.lzh.gitstar.domain.response.Response;
import com.lzh.gitstar.service.GithubSearchService;
import com.lzh.gitstar.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
    private GithubSearchService githublSearchService;

    @Autowired
    private IndexService indexService;


    @RequestMapping("/{login}")
    @ResponseBody
    @RequiresAuthentication
    public Response searchByLogin(@PathVariable String login) {
        Index index = githublSearchService.handleUserIndex(login);
        return Response.ok(index);
    }

    @RequestMapping("/list")
    @ResponseBody
    public Response list() {
        return Response.ok(indexService.listAll());
    }



}
