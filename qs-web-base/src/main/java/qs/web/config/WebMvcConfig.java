/*
package qs.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import java.util.List;

*/
/**
 * Created by yinqingzhun on 2017/08/29.
 *//*

//@Configuration
//@EnableConfigurationProperties(ResourceProperties.class)
//@ComponentScan(value = "qs")
public class WebMvcConfig extends WebMvcConfigurationSupport {


    */
/*    @Primary
        @Bean
        public View errorView(ApplicationContext applicationContext, HttpServletRequest request) {
            TemplateAvailabilityProviders templateAvailabilityProviders = new TemplateAvailabilityProviders(
                    applicationContext);
            TemplateAvailabilityProvider provider = templateAvailabilityProviders.getProvider("error", applicationContext);
            if (provider != null) {
                return new ModelAndView("error");
            }

        }*//*


    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:static/img/favicon.ico");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
        registry.addResourceHandler("/layui/**").addResourceLocations("classpath:/static/layui/");
        registry.addResourceHandler("/lodop/**").addResourceLocations("classpath:/static/lodop/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/logon").setViewName("login");
        registry.addViewController("/h").setViewName("h");
    }

    @Bean
    BeanNameViewResolver beanNameViewResolver() {
        BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
        beanNameViewResolver.setOrder(Ordered.LOWEST_PRECEDENCE);
        return beanNameViewResolver;
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.extendMessageConverters(converters);
    }

    @Bean
    SimpleServletHandlerAdapter simpleServletHandlerAdapter() {
        return new SimpleServletHandlerAdapter();
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

//    @Bean
//    ErrorController errorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
//                                    ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider,
//                                    List<ErrorViewResolver> errorViewResolvers) {
//
//        ErrorController myErrorController = new MyErrorController(errorAttributes, serverProperties.getError(),
//                        errorViewResolvers);
//
//        return myErrorController;
//
//    }


//    public class MyErrorController extends BasicErrorController {
//
//        public MyErrorController(ErrorAttributes errorAttributes,
//                                 ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
//            super(errorAttributes, errorProperties, errorViewResolvers);
//        }
//
//
//        @Override
//        public ResponseEntity error(HttpServletRequest request) {
//            Map<String, Object> errorAttributes = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
//
//
//            HttpStatus status = getStatus(request);
//            ReturnValue body = ReturnValue.buildErrorResult(status.value(), errorAttributes.get("error").toString());
//            return new ResponseEntity(body, status);
//        }
//    }


}
*/
