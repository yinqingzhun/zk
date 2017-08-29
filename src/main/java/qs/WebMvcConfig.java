package qs;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Bean
    ErrorAttributes errorAttributes() {
        ErrorAttributes errorAttributes = new DefaultErrorAttributes();

        return errorAttributes;

    }

}
