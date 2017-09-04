package qs.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.Template;
import org.thymeleaf.spring4.view.ThymeleafView;
import qs.ReturnValue;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


/*    @Primary
    @Bean
    public View errorView(ApplicationContext applicationContext, HttpServletRequest request) {
        TemplateAvailabilityProviders templateAvailabilityProviders = new TemplateAvailabilityProviders(
                applicationContext);
        TemplateAvailabilityProvider provider = templateAvailabilityProviders.getProvider("error", applicationContext);
        if (provider != null) {
            return new ModelAndView("error");
        }

    }*/


    @Bean
    MyErrorController errorController(ErrorAttributes errorAttributes, ServerProperties serverProperties, ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        List<ErrorViewResolver> errorViewResolvers = (List) errorViewResolversProvider.getIfAvailable();
        MyErrorController myErrorController = new MyErrorController(errorAttributes, serverProperties.getError(), errorViewResolvers);

        return myErrorController;

    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:static/img/favicon.ico");
    }

    public static class MyErrorController extends BasicErrorController {

        public MyErrorController(ErrorAttributes errorAttributes,
                                 ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
            super(errorAttributes, errorProperties, errorViewResolvers);
        }

        @Override
        public ResponseEntity error(HttpServletRequest request) {
            Map<String, Object> errorAttributes = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));


            HttpStatus status = getStatus(request);
            ReturnValue body = ReturnValue.buildErrorResult(status.value(), errorAttributes.get("error").toString());
            return new ResponseEntity(body, status);
        }
    }


}
