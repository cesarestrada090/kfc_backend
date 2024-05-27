package com.kfc.app.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
    @Pointcut("within(com.example.demo.controller.*)")
    public void logAllControllers() {

    }
    @Before("logAllControllers()")
    public void logAllControllersBefore(JoinPoint jp){
        System.out.println("Iniciando Request: " + jp.getSignature());
    }

    @After("logAllControllers()")
    public void logAllControllersAfter(JoinPoint jp)
    {
        System.out.println("Terminando Request: " + jp.getSignature());
    }
}
