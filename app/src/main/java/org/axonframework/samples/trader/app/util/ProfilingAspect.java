package org.axonframework.samples.trader.app.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author Jettro Coenradie
 */
//@Aspect
//@Component
public class ProfilingAspect {

    @Around("methodsToBeProfiled()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch sw = new StopWatch(getClass().getSimpleName());
        try {
            sw.start(pjp.getSignature().getName());
            return pjp.proceed();
        } finally {
            sw.stop();
            System.out.println(sw.getLastTaskName()+sw.shortSummary());
        }
    }

    @Pointcut("execution(public * org.axonframework.samples.trader.app.eventstore.mongo.MongoEventStore.*(..))")
    public void methodsToBeProfiled(){}
}