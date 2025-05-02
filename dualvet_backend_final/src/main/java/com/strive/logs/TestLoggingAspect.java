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
@Profile("test") // Active only in the "test" profile
public class TestLoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Service *)")
    public void testPointcut() {}

    @Around("testPointcut()")
    public Object logAroundTest(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Test log - Entering: {}", joinPoint.getSignature());
        Object result = joinPoint.proceed();
        log.info("Test log - Exiting: {}", joinPoint.getSignature());
        return result;
    }
}
