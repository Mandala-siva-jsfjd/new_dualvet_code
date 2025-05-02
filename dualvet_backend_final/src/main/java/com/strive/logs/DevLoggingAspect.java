package com.strive.logs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("dev") // Active only in the "dev" profile
public class DevLoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Service *)")
    public void devPointcut() {}

    @Around("devPointcut()")
    public Object logAroundDev(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("Enter: {} with arguments = {}", joinPoint.getSignature(), joinPoint.getArgs());
        try {
            Object result = joinPoint.proceed();
            log.debug("Exit: {} with result = {}", joinPoint.getSignature(), result);
            return result;
        } catch (Exception e) {
            log.error("Error in {} with message = {}", joinPoint.getSignature(), e.getMessage());
            throw e;
        }
    }
}
