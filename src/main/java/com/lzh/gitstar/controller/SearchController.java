package com.lzh.gitstar.controller;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONException;
import com.lzh.gitstar.domain.dto.Index;
import com.lzh.gitstar.domain.entity.UserIndex;
import com.lzh.gitstar.domain.response.Response;
import com.lzh.gitstar.service.GithubSearchService;
import com.lzh.gitstar.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private GithubSearchService githublSearchService;

    @Autowired
    private IndexService indexService;


    @RequestMapping("/{login}")
    @RequiresAuthentication
    public String searchByLogin(@PathVariable String login, Model model) {
        Index index = githublSearchService.handleIndex(login);
        indexService.saveIndex(index);
        model.addAttribute("index", index);
        return "index";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Response list() {
        return Response.ok(indexService.listAll());
    }



}
