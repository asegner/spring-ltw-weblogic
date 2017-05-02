package net.segner.poc.wlltw.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

/**
 * @author asegner
 */
@Aspect
@Slf4j
public class DemoViewAspect {

    @Autowired
    private DemoCounter demoCounter;

    @Pointcut("execution(@DemoAspectHere * *.*(..))")
    void reusableAnnotationPointcut() {
    }

    @Around("reusableAnnotationPointcut()")
    public Object aroundAdvise(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        demoCounter.incrementAroundAdvise();
        log.info("around counter {}", demoCounter.getAroundAdvise());

        return proceedingJoinPoint.proceed();
    }

    @AfterThrowing("reusableAnnotationPointcut()")
    public void throwingAdvise() {
        demoCounter.incrementThrowAdvise();
        log.info("throwing counter {}", demoCounter.getThrowAdvise());
    }
}
