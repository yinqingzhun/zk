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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qs.model.ReturnValue;
import qs.service.HelloService;
import qs.service.StudentService;
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

    @RequestMapping("ex")
    public String exception() throws Exception {
        throw new Exception("ec");
    }

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
        log.info("lifecycle:init");
    }

    @PreDestroy
    public void destroy() {
        log.info("lifecycle:destroy");
    }

    public void initMethod() {
        System.out.println("lifecycle:init-method");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("lifecycle:afterPropertiesSet");
    }
}
