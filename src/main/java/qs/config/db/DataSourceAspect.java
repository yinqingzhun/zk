package qs.config.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
//@DeclarePrecedence("DataSourceAspect,*")
public class DataSourceAspect {

    @Pointcut("execution(* qs.service..*(..))")
    public void execService() {
    }

    private DbChoosing getDbChoosing(JoinPoint point) {
        DbChoosing dbChoosing = point.getTarget().getClass().getAnnotation(DbChoosing.class);
        if (dbChoosing == null) {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            try {
                method = point.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
                if (method != null) {
                    dbChoosing = method.getAnnotation(DbChoosing.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbChoosing;
    }


    @Around("execService()")
    public Object printAnnotationJointPointAround(ProceedingJoinPoint joinpoint) throws Throwable {
        DbChoosing dbChoosing = getDbChoosing(joinpoint);
        System.out.println("print[Annotation]JointPoint Around start: " + joinpoint.getSignature() + "," + joinpoint.getStaticPart().getKind());
        if (dbChoosing != null)
            DataSourceContextHolder.push(dbChoosing.value());
        Object object = joinpoint.proceed(joinpoint.getArgs());
        System.out.println("print[Annotation]JointPoint Around end: " + joinpoint.getSignature() + "," + joinpoint.getStaticPart().getKind());
        if (dbChoosing != null)
            DataSourceContextHolder.pop();
        return object;
    }



}
