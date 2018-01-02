package qs.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ServiceAspect {
    @Pointcut("execution(* com.daeyes.service..*(..))")
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
        DbChoosing dbChoosing = getDbChoosing(point);
        if (dbChoosing != null)
            DataSourceContextHolder.push(dbChoosing.value());
    }

    @After("pointCut()")
    public void unsetReadDataSourceType(JoinPoint point) {
        DataSourceContextHolder.pop();
    }

}
