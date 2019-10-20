package com.lzh.gitstar.domain.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : lizhihao
 * @since : 2019/10/15, 星期二
 **/
@Getter
@Setter
public class Response<T> {

    private Integer code;

    private T data;

    private String errorMsg;

    public static Response ok(){
        Response response = new Response();
        response.setCode(0);
        response.setData("success");
        return response;
    }

    public static Response ok(Object t) {
        Response ok = Response.ok();
        ok.setData(t);
        return ok;
    }

    public static Response fail(String errorMsg) {
        Response response = new Response();
        response.setCode(-1);
        response.setErrorMsg(errorMsg);
        return response;
    }
}
