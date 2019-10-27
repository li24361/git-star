package com.lzh.gitstar.domain.dto;

import cn.hutool.json.JSONException;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author : lizhihao
 * @since : 2019/10/27, 星期日
 **/
@Data
public class SearchQuery {

    private String login;

    private String userName;

    private String token;

    public void check() {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(login)) {
            throw new JSONException("params error!");
        }
    }
}
