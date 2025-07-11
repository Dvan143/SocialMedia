package org.example.socialmedia.classes.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
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

    @Around("loggableMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Calling method: {} with args: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
        return joinPoint.proceed();
    }

}
