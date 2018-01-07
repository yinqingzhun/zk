package qs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class DataSourceConfig     {
    @Autowired
    PlatformTransactionManager dataSourceTransactionManager;

    @Bean
    @Primary
    AnnotationTransactionAspect annotationTransactionAspect( ) {
        AnnotationTransactionAspect annotationTransactionAspect = new AnnotationTransactionAspect();
        annotationTransactionAspect.setTransactionManager(dataSourceTransactionManager);
        return annotationTransactionAspect;
    }

}
