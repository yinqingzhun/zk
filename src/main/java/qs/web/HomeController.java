package qs.web;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qs.model.ReturnValue;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Controller
public class HomeController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(HomeController.class);
//    @Autowired
//    List<Ysts8DownloadService> serviceList;

    @RequestMapping({"", "/"})
    public String index(Model model) {
//        logger.info("log info - {}", serviceList.stream().map(p -> p.getClass().getSimpleName()).reduce("", (a, b) -> a + "," + b));
        model.addAttribute("message","hi,gay!");
        return "index";
    }

    @RequestMapping("index")
    public String white() throws Exception {
        throw new Exception("ec");
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


}
