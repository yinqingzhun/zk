package qs.web;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qs.ReturnValue;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Controller
public class HomeController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping({"", "/"})
    public ResponseEntity index() {
        logger.info("log info - {}", "new visit to index page");
        ResponseEntity responseEntity = new ResponseEntity("hi,gay!", HttpStatus.OK);
        return responseEntity;
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
