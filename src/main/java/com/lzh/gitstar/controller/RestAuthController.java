package com.lzh.gitstar.controller;

import com.alibaba.fastjson.JSONObject;
import com.lzh.gitstar.service.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : lizhihao
 * @since : 2019/10/14, 星期一
 **/
@Slf4j
@RestController
public class RestAuthController {

    @Autowired
    private UserLoginService userLoginService;

    @Value("${vue.endpoint}")
    private String vueEndpoint;

    @GetMapping("/login")
    public String login(HttpServletResponse response) throws IOException {
        //        response.sendRedirect(userLoginService.githubLogin());
        return userLoginService.githubLogin();
    }

    @RequestMapping("/oauth/github/callback")
    public void login(AuthCallback callback, HttpServletResponse response) throws IOException {
        AuthUser authUser = userLoginService.githubCallback(callback);
        userLoginService.saveUser(authUser);

        JSONObject res = new JSONObject();
        res.put("username", authUser.getUsername());
        response.sendRedirect(vueEndpoint+"/user?result=" + res.toJSONString());
    }

}
