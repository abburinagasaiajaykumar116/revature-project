package org.example.revshop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // ALL CONTROLLERS
    @Pointcut("execution(* org.example.revshop.controller..*(..))")
    public void controllerLayer() {}

    // ALL SERVICES
    @Pointcut("execution(* org.example.revshop.service.impl..*(..))")
    public void serviceLayer() {}

    // METHOD START
    @Before("controllerLayer() || serviceLayer()")
    public void logMethodStart(JoinPoint joinPoint) {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("ENTER -> {}.{}()", className, methodName);
    }

    // METHOD SUCCESS
    @AfterReturning(pointcut = "controllerLayer() || serviceLayer()", returning = "result")
    public void logMethodSuccess(JoinPoint joinPoint, Object result) {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("SUCCESS -> {}.{}() | Result = {}", className, methodName, result);
    }

    //METHOD ERROR
    @AfterThrowing(pointcut = "controllerLayer() || serviceLayer()", throwing = "ex")
    public void logMethodError(JoinPoint joinPoint, Throwable ex) {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.error("FAILED -> {}.{}() : {}", className, methodName, ex.getMessage());
    }
}