package qs.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qs.model.ReturnValue;
import qs.service.HelloService;
import qs.service.StudentService;
import qs.util.JsonHelper;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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



    @RequestMapping({"", "/"})
    public String index(Model model) {
        int i=0;
        System.out.println(1/i);
        //helloService.hello();

        //Student student = new Student();
        //student.setClassId(1);
        //student.setName(UUID.randomUUID().toString());
        //student.setEnabled(true);
        //
        //try {
        //    studentService.save(student);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        studentService.generate();

//        log.info("log info - {}", serviceList.stream().map(p -> p.getClass().getSimpleName()).reduce("", (a, b) -> a + "," + b));

        //model.addAttribute("message", JsonHelper.serialize(studentService.getList(), true));


        return "demo";
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

}
