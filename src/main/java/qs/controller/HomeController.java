package qs.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qs.model.ReturnValue;
import qs.service.HelloService;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Controller
public class HomeController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    HelloService helloService;

    @RequestMapping({"", "/"})
    public String index(Model model) {
        helloService.hello();
//        logger.info("log info - {}", serviceList.stream().map(p -> p.getClass().getSimpleName()).reduce("", (a, b) -> a + "," + b));
        model.addAttribute("message", "hi,gay!");
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
