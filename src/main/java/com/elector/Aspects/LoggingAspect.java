package com.elector.Aspects;

import com.elector.Enums.ConfigEnum;
import com.elector.Utils.ConfigUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Value("${aspects.logging.enabled}")
    private Boolean loggingEnabled;

    @Around("execution(* *(..)) && @annotation(com.elector.Annotations.Loggable)")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        if (ConfigUtils.getConfig(ConfigEnum.enable_logging_aspect, false)) {
            long startTime = System.currentTimeMillis();
            if(loggingEnabled) {
                LOGGER.info("Method {} in class {} was executed with args {} took {} ms",
                        MethodSignature.class.cast(point.getSignature()).getMethod().getName(),
                        point.getThis().getClass().getSimpleName(),
                        point.getArgs(),
                        System.currentTimeMillis() - startTime
                );
            }
        }
        return result;
    }
}
