package qs.config.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
        DbChoosing dbChoosing = AnnotationUtils.findAnnotation(point.getTarget().getClass(), DbChoosing.class);
        if (dbChoosing == null) {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            try {
                method = point.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
                if (method != null) {
                    dbChoosing = method.getAnnotation(DbChoosing.class);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return dbChoosing;
    }

    @Before("pointCut()")
    public void setReadDataSourceType(JoinPoint point) {
        logger.info("********** enter: " + point.getSignature().toShortString());
        DbChoosing dbChoosing = getDbChoosing(point);
        if (dbChoosing != null)
            DataSourceContextHolder.push(dbChoosing.value());
    }

    @After("pointCut()")
    public void unsetReadDataSourceType(JoinPoint point) {
        DbChoosing dbChoosing = getDbChoosing(point);
        if (dbChoosing != null)
            DataSourceContextHolder.pop();
    }

}
