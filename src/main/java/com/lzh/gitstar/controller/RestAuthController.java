package com.lzh.gitstar.controller;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.lzh.gitstar.domain.response.Response;
import com.lzh.gitstar.service.UserLoginService;
import com.lzh.gitstar.shiro.GithubToken;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author : admin
 * @since : 2019/10/14, 星期一
 **/
@Slf4j
@RestController
public class RestAuthController {

    @Autowired
    private UserLoginService userLoginService;


    @GetMapping("/login")
    public String login( HttpServletResponse response) throws IOException {
//        response.sendRedirect(userLoginService.githubLogin());
        return userLoginService.githubLogin();
    }

    @GetMapping("/logout")
    @RequiresAuthentication
    @ResponseBody
    public Response logout(){
        SecurityUtils.getSubject().logout();
        return Response.ok();
    }

    @RequestMapping("/oauth/github/callback")
    public void login(AuthCallback callback, HttpServletResponse response) throws IOException {
        GithubToken githubToken = new GithubToken();
        githubToken.setAuthCallback(callback);
        try {
            SecurityUtils.getSubject().login(githubToken);
        }catch (Exception e){
            log.error("login error", e);
            throw new JSONException(e.getMessage());
        }
//        return Response.ok("success");
        AuthUser principal = (AuthUser) SecurityUtils.getSubject().getPrincipal();
        JSONObject res = new JSONObject();
        res.put("username", principal.getUsername());
        res.put("token", principal.getToken().getAccessToken());
        response.sendRedirect("http://localhost:8010/user?result="+res.toJSONString());
    }
}
