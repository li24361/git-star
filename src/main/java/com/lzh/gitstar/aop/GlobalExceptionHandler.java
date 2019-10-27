package com.lzh.gitstar.aop;

import cn.hutool.json.JSONException;
import com.lzh.gitstar.domain.response.Response;
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


    @ExceptionHandler(value = JSONException.class)
    @ResponseBody
    public Response<String> jsonErrorHandler(HttpServletRequest req, JSONException e) {
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Response<String> jsonErrorHandler(HttpServletRequest req, NullPointerException e)  {
        return Response.fail("npe"+e.getStackTrace()[0]);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<String> jsonErrorHandler(HttpServletRequest req, Exception e) {
        return Response.fail(e.getMessage());
    }


}
