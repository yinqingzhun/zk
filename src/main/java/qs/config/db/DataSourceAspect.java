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

@Component
@Aspect
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
                //method = point.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
                if (method != null) {
                    dbChoosing = method.getAnnotation(DbChoosing.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbChoosing;
    }

    //@Before("@annotation(d)")
    //public void printAnnotationJointPointBefore(JoinPoint joinpoint,DbChoosing d) {
    //    //if (joinpoint.getStaticPart().getKind().contains("execution"))
    //    System.out.println("print[Annotation]JointPoint Before: " + joinpoint.getSignature());
    //}
    //
    //@After("@annotation(DbChoosing)")
    //public void printAnnotationJointPointAfter(JoinPoint joinpoint) {
    //    //if (joinpoint.getStaticPart().getKind().contains("execution"))
    //    System.out.println("print[Annotation]JointPoint After: " + joinpoint.getSignature());
    //}


    @Around("pointCut()")
    public Object printAnnotationJointPointAround(ProceedingJoinPoint joinpoint) throws Throwable {
        DbChoosing dbChoosing = getDbChoosing(joinpoint);
        if (dbChoosing == null) {
            return joinpoint.proceed(joinpoint.getArgs());
        } else {
            System.out.println("print[Annotation]JointPoint Around start: " + joinpoint.getSignature() + "," + joinpoint.getStaticPart().getKind());
            Object object = joinpoint.proceed(joinpoint.getArgs());
            System.out.println("print[Annotation]JointPoint Around end: " + joinpoint.getSignature() + "," + joinpoint.getStaticPart().getKind());
            return object;
        }
    }

    //@AfterReturning("@annotation(DbChoosing)")
    //public void printAnnotationJointPointReturning(JoinPoint joinpoint) {
    //    //if (joinpoint.getStaticPart().getKind().contains("execution"))
    //    System.out.println("print[Annotation]JointPoint Returning: " + joinpoint.getSignature());
    //}

    //@Before("@within(qs.config.db.DbChoosing)")
    //public void printWithinJointPoint(JoinPoint joinpoint) {
    //    System.out.println("print[Within]JointPoint: " + joinpoint.getSignature());
    //}

    //@Before("pointCut()")
    //public void setReadDataSourceType(JoinPoint point) {
    //
    //    DbChoosing dbChoosing = getDbChoosing(point);
    //    if (dbChoosing != null)
    //        DataSourceContextHolder.push(dbChoosing.value());
    //}
    //
    //@After("pointCut()")
    //public void unsetReadDataSourceType(JoinPoint point) {
    //    DbChoosing dbChoosing = getDbChoosing(point);
    //    if (dbChoosing != null)
    //        DataSourceContextHolder.pop();
    //}

}
