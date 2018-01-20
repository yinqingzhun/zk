package qs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class DataSourceConfig     {
    //@Autowired
    //PlatformTransactionManager dataSourceTransactionManager;

    //@Bean
    //@Primary
    //AnnotationTransactionAspect annotationTransactionAspect( ) {
    //    AnnotationTransactionAspect annotationTransactionAspect = new AnnotationTransactionAspect();
    //    annotationTransactionAspect.setTransactionManager(dataSourceTransactionManager);
    //    return annotationTransactionAspect;
    //}

}
