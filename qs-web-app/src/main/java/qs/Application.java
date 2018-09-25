package qs;

import de.invesdwin.instrument.DynamicInstrumentationLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import qs.web.controller.WelcomeController;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by yinqingzhun on 2017/08/29.
 */
@SpringBootApplication(exclude = { })
@EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
public class Application extends SpringBootServletInitializer {
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        DynamicInstrumentationLoader.waitForInitialized(); //dynamically attach java agent to jvm if not already present
        DynamicInstrumentationLoader.initLoadTimeWeavingContext(); //weave all classes before they are loaded as beans
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        DynamicInstrumentationLoader.waitForInitialized(); //dynamically attach java agent to jvm if not already present
        DynamicInstrumentationLoader.initLoadTimeWeavingContext(); //weave all classes before they are loaded as beans
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.sources(Application.class).run();
    }

    @Bean
    SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        Map<String, Object> urlMap = new LinkedHashMap<String, Object>();
        urlMap.put("welcome3","welcomeController");
        urlMap.put("welcome4","welcomeController");

        SimpleUrlHandlerMapping handlerMapping=new SimpleUrlHandlerMapping();
        handlerMapping.setUrlMap(urlMap);
        return handlerMapping;
    }

    @Bean WelcomeController welcomeController(){
        return new WelcomeController();
    }






}
