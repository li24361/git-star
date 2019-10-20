package com.lzh.gitstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : lizhihao
 * @since : 2019/10/20, 星期日
 **/
@Controller
public class UserController {

    @GetMapping("/index")
    public String login() {
        return "index";
    }
}
