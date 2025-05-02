//package com.strive.logs;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class LoggingAspect {
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    // Pointcut that matches all controllers, services, and repository methods.
//    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Service *) || within(@org.springframework.stereotype.Repository *)")
//    public void applicationPointcut() {
//        // Pointcut is an expression that tells Spring where the advice should be applied.
//    }
//
//    // Advice that logs methods entering and exiting
//    @Around("applicationPointcut() && !within(com.strive.logs..*)") // Exclude LoggingAspect itself
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        if (joinPoint.getSignature().getName().contains("criticalService")) { // Log only specific methods
//            log.info("Enter: {} with arguments = {}", joinPoint.getSignature(), joinPoint.getArgs());
//        }
//        try {
//            Object result = joinPoint.proceed();
//            if (joinPoint.getSignature().getName().contains("criticalService")) {
//                log.info("Exit: {} with result = {}", joinPoint.getSignature(), result);
//            }
//            return result;
//        } catch (Exception e) {
//            log.error("Error in {} with message = {}", joinPoint.getSignature(), e.getMessage());
//            throw e;
//        }
//    }
//
//    // Advice that logs when an exception is thrown
//    @AfterThrowing(pointcut = "applicationPointcut()", throwing = "e")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
//        log.error("Exception in {}() with message = '{}'", joinPoint.getSignature().getName(), e.getMessage());
//    }
//}
