package com.lzh.gitstar;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.hutool.core.map.MapUtil;
import com.lzh.gitstar.domain.dto.Index;
import com.lzh.gitstar.shiro.OAuth2Realm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : lizhihao
 * @since : 2019/10/15, 星期二
 **/
@Configuration
public class GitStarConfiguration {

    @Bean
    public ConcurrentHashMap<String, Index> timedCache(){
        return MapUtil.newConcurrentHashMap();
    }

    @Bean
    public Realm realm() {
        return new OAuth2Realm();
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        // 由于demo1展示统一使用注解做访问控制，所以这里配置所有请求路径都可以匿名访问
        chain.addPathDefinition("/**", "anon"); // all paths are managed via annotations
        return chain;
    }

    @Bean
    protected CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
