package com.lzh.gitstar.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : lizhihao
 * @since : 2019/10/21, 星期一
 **/
//@Component
public class FetchUserJob {

    @Scheduled(cron = "0/10 * * * * *")
    public void fetchUser() {

    }
}
