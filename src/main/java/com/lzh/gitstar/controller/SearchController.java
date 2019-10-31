package com.lzh.gitstar.controller;

import com.lzh.gitstar.domain.dto.IndexDto;
import com.lzh.gitstar.domain.dto.SearchQueryDto;
import com.lzh.gitstar.domain.response.Response;
import com.lzh.gitstar.service.GithubSearchService;
import com.lzh.gitstar.service.IndexService;
import com.lzh.gitstar.service.RankService;
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

    @Autowired
    private RankService rankService;


    @RequestMapping
    @ResponseBody
    public Response searchByLogin(@RequestBody SearchQueryDto searchQuery) {
        searchQuery.check();
        userLoginService.fillInUserToken(searchQuery);
        IndexDto index = githubSearchService.handleUserIndex(searchQuery);
        return Response.ok(index);
    }

    @RequestMapping("/list")
    @ResponseBody
    public Response list() {
        return Response.ok(rankService.list());
    }



}
