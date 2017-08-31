package qs;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by yinqingzhun on 2017/08/31.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "not found")
public class NotFoundException extends  RuntimeException {
}
