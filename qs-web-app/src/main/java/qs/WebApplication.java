package qs;

import de.invesdwin.instrument.DynamicInstrumentationLoader;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import qs.web.controller.WelcomeController;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;


/**
 * Created by yinqingzhun on 2017/08/29.
 */
@EnableScheduling
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
public class WebApplication extends SpringBootServletInitializer {
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        DynamicInstrumentationLoader.waitForInitialized(); //dynamically attach java agent to jvm if not already present
        DynamicInstrumentationLoader.initLoadTimeWeavingContext(); //weave all classes before they are loaded as beans
        return builder.sources(WebApplication.class);
    }

    public static void main(String[] args) {
        DynamicInstrumentationLoader.waitForInitialized(); //dynamically attach java agent to jvm if not already present
        DynamicInstrumentationLoader.initLoadTimeWeavingContext(); //weave all classes before they are loaded as beans
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.sources(WebApplication.class).web(WebApplicationType.SERVLET).run();
    }

    @Bean
    SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        Map<String, Object> urlMap = new LinkedHashMap<String, Object>();
        urlMap.put("welcome3", "welcomeController");
        urlMap.put("welcome4", "welcomeController");

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setUrlMap(urlMap);
        return handlerMapping;
    }

    @Bean
    WelcomeController welcomeController() {
        return new WelcomeController();
    }




    @Scheduled(fixedDelay = 1000)
    public void doSth(){
        LockSupport.parkNanos(Duration.ofSeconds(3).toNanos());
        logger.info("dosth at thread {}"+Thread.currentThread().getName());

    }


}
