package qs.config.db;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//not work
aspect DataSourceAj {
    //@Around("@annotation(DbChoosing)")
    //public Object printAnnotationJointPointAround(ProceedingJoinPoint joinpoint) throws Throwable {
    //    System.out.println("print[Annotation]JointPoint Around start: " + joinpoint.getSignature());
    //    Object object =  joinpoint.proceed(joinpoint.getArgs());
    //    System.out.println("print[Annotation]JointPoint Around end: " + joinpoint.getSignature());
    //    return object;
    //}

  public   Object around():@annotation(DbChoosing){
        System.out.println("print[Annotation]JointPoint Around start: " + thisJoinPoint.getSignature());
        Object object = proceed(thisJoinPoint.getArgs());
        System.out.println("print[Annotation]JointPoint Around end: " + thisJoinPoint.getSignature());
        return object;
    }

    private pointcut executionOfAnyPublicMethodInAtTransactionalType() :
            execution(public * ((@DbChoosing *)+).*(..)) && within(@DbChoosing *);

    /**
     * Matches the execution of any method with the Transactional annotation.
     */
    private pointcut executionOfTransactionalMethod() :
            execution(@DbChoosing * *(..));

    /**
     * Definition of pointcut from super aspect - matched join points
     * will have Spring transaction management applied.
     */
    protected pointcut transactionalMethodExecution(Object txObject) :
            (executionOfAnyPublicMethodInAtTransactionalType() || executionOfTransactionalMethod() ) && this(txObject);
}
