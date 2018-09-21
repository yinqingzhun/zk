package qs.web.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import qs.model.ReturnValue;
import qs.model.User;
import qs.service.HelloService;
import qs.service.StudentService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Controller
@Slf4j
public class HomeController implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean {

    @Autowired
    HelloService helloService;
    @Autowired
    StudentService studentService;
    @Autowired
    Configuration cfg;
    @Autowired
    FreeMarkerViewResolver freeMarkerViewResolver;

    @ExceptionHandler({SQLException.class})
    public String dr(Exception exception) {
        // Nothing to do. Return value 'databaseError' used as logical view name
        // of an error page, passed to view-resolver(s) in usual way.
        log.error("Request raised: " + exception.getClass().getSimpleName());
        return "error";
    }

    @ResponseBody
    @RequestMapping("/xml")
    public User xml(User user) {
        return user;
    }

    @RequestMapping({"", "/"})
    public String index(Model model, HttpServletRequest request) throws Exception {
        model.addAttribute("name", "demo");
        //String name = uploadFile(request);
        //FreeMarkerView view = (FreeMarkerView) freeMarkerViewResolver.resolveViewName("demo", Locale.SIMPLIFIED_CHINESE);
        //createStaticPage(cfg, view.getUrl(), "demo", Collections.emptyMap());
        throw new SQLException("db");
        //return "demo";
    }

    public static void createStaticPage(Configuration cfg
            , String templateFileName
            , String staticPageName
            , Map<String, Object> data
    ) {
        String staticPagePath = "e:";
        try {
            cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);// 设置标签
            Template temp = cfg.getTemplate(templateFileName);// 获取模板对象
            String target = staticPagePath + "/" + staticPageName + ".html";
            Writer out = new OutputStreamWriter(new FileOutputStream(target), "UTF-8");
            temp.process(data, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String uploadFile(HttpServletRequest request) throws IOException {
        String fileName = new Date().getTime() + "test";
        String path = getClass().getClassLoader().getResource("").getPath() + "freemarker/" + fileName + ".ftl";

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path.toString()), "utf-8")) {
            writer.write("success");
            writer.flush();
        }

        return fileName;
    }

    @RequestMapping("index")
    public String white() throws Exception {
        return "index";
    }

    @RequestMapping(value = "w", produces = "application/json")
    @ResponseBody
    public ReturnValue w() throws Exception {
        return ReturnValue.buildSuccessResult("json");
    }

    @RequestMapping(value = "json", produces = "application/json")
    @ResponseBody
    public ReturnValue json() throws Exception {
        return ReturnValue.buildSuccessResult("json");
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "not found")
    @RequestMapping("ex")
    public String exception(@RequestParam(value = "i", required = false, defaultValue = "-1") int i) throws Exception {
        if (i == 0)
            throw new Exception("ec");
        return "index";
    }

    //@ExceptionHandler(value = HttpStatus.BAD_REQUEST)

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("lifecycle:setBeanFactory");
    }

    @Override
    public void setBeanName(String name) {
        log.info("lifecycle:setBeanName," + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("lifecycle:setApplicationContext");
    }

    @PostConstruct
    public void init() {
        log.info("lifecycle:PostConstruct");
    }

    @PreDestroy
    public void destroy() {
        log.info("lifecycle:PreDestroy");
    }

    public void initMethod() {
        System.out.println("lifecycle:init-method");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("lifecycle:afterPropertiesSet");
    }

    @RequestMapping(value = "/helloadmin", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('admin')")
    public String helloAdmin() {
        return "helloAdmin";
    }

    @RequestMapping(value = "/hellouser", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public String helloUser() {
        return "helloUser";
    }


}
