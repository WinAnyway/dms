package pl.com.bottega.dms.infrastructure.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Component
@Aspect
public class ProfilingAspect {

    @Around("execution(* pl.com.bottega.dms.application..*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint jPoint) throws Throwable {

        LocalTime start = LocalTime.now();
        try {
            Object result = jPoint.proceed();
            LocalTime end = LocalTime.now();
            System.out.print(jPoint.getSignature() + " Executed in: ");
            System.out.println(ChronoUnit.MILLIS.between(start, end));
            return result;
        } catch (Throwable throwable) {
            LocalTime end = LocalTime.now();
            System.out.println(ChronoUnit.MILLIS.between(start, end));
            throw throwable;
        }

    }

}
