package com.trip.noting.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class BeanAspect {

    @After("within(com.trip.noting.configuration.Hello)")
    @Async("taskExecutor")
    public void recordLog(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName().toLowerCase();
        System.out.println(methodName + ":------");
        log.info("methodName:{}", "recordLog 1");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("methodName:{}", "recordLog 2");
    }
}
