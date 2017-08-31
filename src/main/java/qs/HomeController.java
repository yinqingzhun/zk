package qs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Controller
public class HomeController {

    @RequestMapping({"", "/"})
    public ResponseEntity index() {
        ResponseEntity responseEntity = new ResponseEntity("hi,gay!", HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping("index")
    public String white() throws  Exception{
       throw new Exception("ec");
    }

    @RequestMapping(value = "w",produces = "application/json")
    @ResponseBody
    public ReturnValue w() throws  Exception{
        throw new NotFoundException();
    }

    @RequestMapping(value = "json",produces = "application/json")
    @ResponseBody
    public ReturnValue json() throws  Exception{
        return ReturnValue.buildSuccessResult("json");
    }


}
