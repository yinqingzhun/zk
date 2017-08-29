package qs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("w")
    @ResponseBody
    public ReturnValue w() throws  Exception{
        throw new Exception("w");
    }


}
