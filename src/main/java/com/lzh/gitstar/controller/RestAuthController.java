package com.lzh.gitstar.controller;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.lzh.gitstar.domain.response.Response;
import com.lzh.gitstar.service.UserLoginService;
import com.lzh.gitstar.shiro.GithubToken;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthDefaultSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author : admin
 * @since : 2019/10/14, 星期一
 **/
@Slf4j
@Controller
public class RestAuthController {

    @Autowired
    private UserLoginService userLoginService;


    @GetMapping("/login")
    public void login( HttpServletResponse response) throws IOException {
        response.sendRedirect(userLoginService.githubLogin());
    }

    @GetMapping("/logout")
    @RequiresAuthentication
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "index";
    }

    @RequestMapping("/oauth/github/callback")
    public String login(AuthCallback callback) {

        GithubToken githubToken = new GithubToken();
        githubToken.setAuthCallback(callback);
        try {
            SecurityUtils.getSubject().login(githubToken);
        }catch (Exception e){
            log.error("login error", e);
            throw new JSONException(e.getMessage());
        }
        return "index";
    }
}
