package com.example.wenda.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Slf4j
@Component
public class LogAspect {
    @Before("execution(* com.example.wenda.controller.*Controller.*(..))")
    public void beforeMethod() {
        log.info("开始调用接口");
    }
    @After("execution(* com.example.wenda.controller.*Controller.*(..))")
    public void afterMethod() {
        log.info("调用接口完毕");
    }
}
