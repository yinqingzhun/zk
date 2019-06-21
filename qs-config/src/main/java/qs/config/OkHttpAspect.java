package qs.config;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class OkHttpAspect {
    @Pointcut("execution(* okhttp3.RealCall.execute(..))")
    public void point() {

    }

    @Around("point()")
    public Object printAnnotationJointPointAround(ProceedingJoinPoint joinpoint) throws Throwable {
        okhttp3.Call call = (okhttp3.Call) joinpoint.getTarget();
        Request request = call.request();

        Stopwatch stopwatch = Stopwatch.createStarted();

        Object object = joinpoint.proceed(joinpoint.getArgs());

        stopwatch.stop();
        
        long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        log.info("HTTP {} {}  costs {} ms", request.method().toUpperCase(), request.url(),duration );
        SlowLogCenter.publishEvent(SlowLogCenter.LogType.HTTP, String.format("%s %s", request.method().toUpperCase(), request.url()), duration);

        return object;


    }

}