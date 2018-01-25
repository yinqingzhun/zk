package qs;

import de.invesdwin.instrument.DynamicInstrumentationLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.core.env.Environment;

import java.util.Optional;


/**
 * Created by yinqingzhun on 2017/08/29.
 */
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
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


}
