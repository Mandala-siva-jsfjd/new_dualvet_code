package com.strive.logs;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("prod") // Active only in the "prod" profile
public class ProdLoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Service *)")
    public void prodPointcut() {}

    @Around("prodPointcut()")
    public Object logAroundProd(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Enter: {}", joinPoint.getSignature()); // Less verbose
        try {
            Object result = joinPoint.proceed();
            log.info("Exit: {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            log.error("Error in {} with message = {}", joinPoint.getSignature(), e.getMessage());
            throw e;
        }
    }
}
