package qs.config.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Component
@Aspect
@DeclarePrecedence("DataSourceAspect,*")
public class DataSourceAspect {
    Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut("execution(* qs.service..*(..))")
    public void pointCut() {
    }

    private DbChoosing getDbChoosing(JoinPoint point) {
        DbChoosing dbChoosing = point.getTarget().getClass().getAnnotation(DbChoosing.class);
        if (dbChoosing == null) {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            try {

                method = Optional.ofNullable(point.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes())).orElse(method);

            } catch (Exception e) {
                logger.debug(e.getMessage());
            }

            if (method != null) {
                dbChoosing = method.getAnnotation(DbChoosing.class);
            }
        }
        return dbChoosing;
    }


    @Around("pointCut()")
    public Object printAnnotationJointPointAround(ProceedingJoinPoint joinpoint) throws Throwable {
        DbChoosing dbChoosing = getDbChoosing(joinpoint);
        if (dbChoosing == null) {
            return joinpoint.proceed(joinpoint.getArgs());
        } else {
            System.out.println("print[Annotation]JointPoint Around start: " + joinpoint.getSignature() + "," + joinpoint.getStaticPart().getKind());
            DataSourceContextHolder.push(dbChoosing.value());
            Object object = joinpoint.proceed(joinpoint.getArgs());
            System.out.println("print[Annotation]JointPoint Around end: " + joinpoint.getSignature() + "," + joinpoint.getStaticPart().getKind());
            DataSourceContextHolder.pop();
            return object;
        }
    }


}
