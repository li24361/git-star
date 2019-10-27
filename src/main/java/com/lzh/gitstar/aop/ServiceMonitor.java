package com.lzh.gitstar.aop;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author : lizhihao
 * @since : 2019/10/19, 星期六
 **/
@Aspect
@Component
@Slf4j
public class ServiceMonitor {

    @Around("execution(* com.lzh..*Controller.*(..))")
    public Object logServiceAccess(ProceedingJoinPoint pjp) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String className = pjp.getTarget().getClass().getName();
        String fullMethodName = className + "." + pjp.getSignature().getName();
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            log.error("error:",e);
            throw new JSONException(e.getMessage());
        }
        stopWatch.stop();
        log.info(fullMethodName + "执行耗时:" +  stopWatch.getTotalTimeSeconds() + " 秒!");
        return result;
    }
}
