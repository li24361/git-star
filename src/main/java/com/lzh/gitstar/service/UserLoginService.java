package com.lzh.gitstar.service;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.lzh.gitstar.domain.dto.SearchQuery;
import com.lzh.gitstar.domain.entity.UserInfo;
import com.lzh.gitstar.repo.UserRepository;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/

@Service
@Slf4j
public class UserLoginService {

    @Autowired
    private AuthRequestFactory factory;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private Cache cache;

    private static final String github = "github";

    private static AuthRequest authRequest;



    @PostConstruct
    public void init() {
        authRequest  = factory.get(github);
    }

    public String githubLogin(){
        return authRequest.authorize(AuthStateUtils.createState()) + "&scope=user:email%20read:org" ;
    }

    public void fillInUserToken(SearchQuery searchQuery) {
        String cacheUserToken = getCacheUserToken(searchQuery.getUserName());
        searchQuery.setToken(cacheUserToken);
        log.info("searchQuery:{}", searchQuery);
    }

    public AuthUser githubCallback(AuthCallback callback){
        AuthRequest authRequest = factory.get(github);
        AuthResponse response = authRequest.login(callback);
        AuthUser authUser = (AuthUser)response.getData();
        return authUser;
    }

    public void saveUser(AuthUser authUser) {
        Executors.newSingleThreadExecutor().submit(() -> {
            UserInfo user = new UserInfo();
            user.setId(Long.valueOf(authUser.getUuid()));
            user.setLogin(authUser.getUsername());
            user.setToken(authUser.getToken().getAccessToken());
            user.setLocation(authUser.getLocation());
            userRepository.save(user);
        });
        cache.put(authUser.getUsername(),authUser.getToken().getAccessToken());
    }


    @Cacheable("login")
    public String getCacheUserToken(String login) {
        return userRepository.findByLogin(login).orElseThrow(()->{
            return new JSONException("user not login");
        }).getToken();
    }

}
