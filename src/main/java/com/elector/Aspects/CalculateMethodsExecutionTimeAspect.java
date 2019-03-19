package com.elector.Aspects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Aspect
@Component
public class CalculateMethodsExecutionTimeAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateMethodsExecutionTimeAspect.class);

//    @Around("(execution(* com.elector.Controllers.BaseController.*(..))) || " +
//            "(execution(* com.elector.Controllers.CandidateDashboardController.*(..))) && " +
//            "!(execution(* com.elector.Controllers.BaseController.initBinder(..)))")
    public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object retVal = null;
        if (isDebugEnabled()) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            retVal = joinPoint.proceed();
            stopWatch.stop();
            LOGGER.info(joinPoint.getSignature() + " [execution time: " + stopWatch.getTotalTimeMillis() + " ms / " + stopWatch.getTotalTimeSeconds() + " sec");
        } else {
            retVal = joinPoint.proceed();
        }
        return retVal;
    }

    private boolean isDebugEnabled() {
        return false;
    }


}
