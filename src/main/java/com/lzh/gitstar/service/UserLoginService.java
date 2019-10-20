package com.lzh.gitstar.service;

import cn.hutool.json.JSONUtil;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/

@Service
@Slf4j
public class UserLoginService {

    @Autowired
    private AuthRequestFactory factory;

    private static final String github = "github";

    private static AuthRequest authRequest;

    @PostConstruct
    public void init() {
        authRequest  = factory.get(github);
    }

    public String githubLogin(){
        return authRequest.authorize(AuthStateUtils.createState()) + "&scope=user:email%20read:org";
    }

    public AuthUser githubCallback(AuthCallback callback){
        AuthRequest authRequest = factory.get(github);
        AuthResponse response = authRequest.login(callback);
        log.info("【response】= {}", JSONUtil.toJsonStr(response));
        AuthUser authUser = (AuthUser)response.getData();
        return authUser;
    }

}
