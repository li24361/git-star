package com.lzh.gitstar.controller;

import cn.hutool.json.JSONException;
import com.lzh.gitstar.domain.dto.Index;
import com.lzh.gitstar.domain.dto.SearchQuery;
import com.lzh.gitstar.domain.response.Response;
import com.lzh.gitstar.service.GithubSearchService;
import com.lzh.gitstar.service.IndexService;
import com.lzh.gitstar.service.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private GithubSearchService githubSearchService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private UserLoginService userLoginService;


    @RequestMapping
    @ResponseBody
    public Response searchByLogin(@RequestBody SearchQuery searchQuery) {
        searchQuery.check();
        userLoginService.fillInUserToken(searchQuery);
        Index index = githubSearchService.handleUserIndex(searchQuery);
        return Response.ok(index);
    }

    @RequestMapping("/list")
    @ResponseBody
    public Response list() {
        return Response.ok(indexService.listAll());
    }



}
