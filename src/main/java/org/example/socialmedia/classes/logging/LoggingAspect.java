package org.example.socialmedia.classes.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(org.example.socialmedia.classes.logging.Loggable)")
    public void loggableMethods(){
    }

    @Before("loggableMethods()")
    public void logBefore(JoinPoint joinPoint){
        logger.info("Calling method: {} with args: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "loggableMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method {} returned: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "loggableMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex){
        logger.error("Method {} threw exception: {}", joinPoint.getSignature(), ex.getMessage());
    }
}
