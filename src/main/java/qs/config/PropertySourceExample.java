package qs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;


@Configuration
@PropertySource("classpath:props.properties")
public class PropertySourceExample {
    @Autowired
    Environment env;

    @Value("${app.name:qs}")
    String appName;

    @Value("${app.version}}")
    String appVersion;

    @Value("#{ systemProperties['java.version'] }")
    private String javaVersion;

    @Value("#{systemEnvironment['JAVA_HOME']}")
    private String javaHome;

    @Bean
    MyBean myBean() {
        System.out.println("---------------------------------------");
        //use @value with spel
        System.out.println(appVersion);
        //use @value with placeHolder
        System.out.println(appName);
        //use injected environment variable
        System.out.println(env.getProperty("app.name"));
        System.out.println("---------------------------------------");
        return new MyBean();
    }


    //@Bean
    //public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
    //    return new PropertySourcesPlaceholderConfigurer();
    //}

    public static class MyBean {
    }

}
