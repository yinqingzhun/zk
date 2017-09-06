package qs.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.thymeleaf.Template;
import org.thymeleaf.spring4.view.ThymeleafView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import qs.ReturnValue;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Configuration
@EnableConfigurationProperties(ResourceProperties.class)
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


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:static/img/favicon.ico");
    }

    @Bean
    BeanNameViewResolver beanNameViewResolver(){
        BeanNameViewResolver beanNameViewResolver=new BeanNameViewResolver();
        beanNameViewResolver.setOrder(Ordered.LOWEST_PRECEDENCE);
        return beanNameViewResolver;
    }


    /* @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    MyErrorController errorController(ErrorAttributes errorAttributes, ServerProperties serverProperties, ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider, List<ErrorViewResolver> errorViewResolvers) {

        MyErrorController myErrorController = new MyErrorController(errorAttributes, serverProperties.getError(), errorViewResolvers);

        return myErrorController;

    }

    void errorPageRegistry(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage("/error"));
    }

    @Bean
    public DefaultErrorViewResolver conventionErrorViewResolver(ApplicationContext applicationContext, ResourceProperties resourceProperties) {
        return new DefaultErrorViewResolver(applicationContext, resourceProperties);
    }

    @Primary
    @Bean
    public MyErrorPageCustomizer errorPageCustomizer() {
        return new MyErrorPageCustomizer();
    }*/

    public static class MyErrorPageCustomizer implements ErrorPageRegistrar, Ordered {



        @Override
        public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
            ErrorPage errorPage = new ErrorPage("/error");
            errorPageRegistry.addErrorPages(errorPage);
        }

        @Override
        public int getOrder() {
            return 0;
        }

    }

    public class MyErrorController extends BasicErrorController {

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
