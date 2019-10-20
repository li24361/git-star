package com.lzh.gitstar.shiro;

import lombok.Data;
import me.zhyd.oauth.model.AuthCallback;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
@Data
public class GithubToken implements AuthenticationToken {

    private AuthCallback authCallback;

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return authCallback.getCode();
    }

}
