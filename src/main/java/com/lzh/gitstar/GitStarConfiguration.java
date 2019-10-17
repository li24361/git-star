package com.lzh.gitstar;

import cn.hutool.core.map.MapUtil;
import com.lzh.gitstar.domain.Index;
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


}
