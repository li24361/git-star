package com.lzh.gitstar.shiro;

import com.lzh.gitstar.domain.entity.UserInfo;
import com.lzh.gitstar.repo.UserRepository;
import com.lzh.gitstar.service.UserLoginService;
import me.zhyd.oauth.model.AuthUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserRepository userRepository;

    public boolean supports(AuthenticationToken token) {
        return token instanceof GithubToken; //表示此Realm只支持OAuth2Token类型
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        GithubToken token = (GithubToken) authenticationToken;
        AuthUser authUser = userLoginService.githubCallback(token.getAuthCallback());

        Executors.newSingleThreadExecutor().submit(() -> {
            UserInfo user = new UserInfo();
            user.setId(Long.valueOf(authUser.getUuid()));
            user.setLogin(authUser.getUsername());
            user.setToken(authUser.getToken().getAccessToken());
            user.setLocation(authUser.getLocation());
            userRepository.save(user);
        });
        SimpleAuthenticationInfo authenticationInfo =
                new SimpleAuthenticationInfo(authUser, token.getAuthCallback().getCode(), getName());
        return authenticationInfo;
    }
}
