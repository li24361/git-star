package com.lzh.gitstar.aop;

import cn.hutool.json.JSONException;
import com.lzh.gitstar.domain.response.Response;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String jsonErrorHandler(HttpServletRequest req, UnauthenticatedException e) throws Exception {
        return "index";
    }

    @ExceptionHandler(value = JSONException.class)
    @ResponseBody
    public Response<String> jsonErrorHandler(HttpServletRequest req, JSONException e) throws Exception {
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Response<String> jsonErrorHandler(HttpServletRequest req, NullPointerException e) throws Exception {
        return Response.fail("npe"+e.getStackTrace()[0]);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Response.fail(e.getMessage());
    }


}
