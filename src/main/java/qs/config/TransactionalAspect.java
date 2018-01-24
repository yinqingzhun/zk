package qs.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.core.Ordered;
//@Component
//@Aspect
//@Order(value = Ordered.LOWEST_PRECEDENCE)
//public class TransactionalAspect   {
//
//    // this method is the around advice
//    @Around("@annotation(org.springframework.transaction.annotation.Transactional)&&execution(* qs.service..*(..))")
//    public Object profile(ProceedingJoinPoint call) throws Throwable {
//        Object returnValue;
//        StopWatch clock = new StopWatch(getClass().getName());
//        try {
//            clock.start(call.toShortString());
//            returnValue = call.proceed();
//        } finally {
//            clock.stop();
//            System.out.println(clock.prettyPrint());
//        }
//        return returnValue;
//    }
//}
