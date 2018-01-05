package qs.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qs.model.ReturnValue;
import qs.model.Student;
import qs.service.HelloService;
import qs.service.StudentService;
import qs.util.JsonHelper;

import java.util.UUID;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Controller
public class HomeController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    HelloService helloService;
    @Autowired
    StudentService studentService;

    @RequestMapping({"", "/"})
    public String index(Model model) {

        helloService.hello();

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

//        logger.info("log info - {}", serviceList.stream().map(p -> p.getClass().getSimpleName()).reduce("", (a, b) -> a + "," + b));

        model.addAttribute("message", JsonHelper.serialize(studentService.getList(), true));



        return "index";
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


}
