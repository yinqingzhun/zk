package qs.web.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import qs.model.ReturnValue;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@ResponseBody
@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice implements ResponseBodyAdvice {


    @Value("${spring.profiles.active}")
    private String profiles;

    @Autowired
    ObjectMapper objectMapper;

    private HashSet<String> profileMap = new HashSet<>();

    @PostConstruct
    public void init() {
        if (StringUtils.hasText(profiles)) {
            Arrays.stream(profiles.split(",")).forEach(profileMap::add);
        }
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setFieldMarkerPrefix(null);
    }

    @ExceptionHandler(Exception.class)
    public Object exception(Exception ex,HttpServletRequest request) {
        String message = Optional.ofNullable(ex.getMessage()).orElse(ex.toString());


        ReturnValue returnValue = null;

        if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException missingServletRequestParameterException = (MissingServletRequestParameterException) ex;
            returnValue = ReturnValue.buildErrorResult(400, String.format("缺少入参%s", missingServletRequestParameterException.getParameterName()));
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException methodArgumentTypeMismatchException = (MethodArgumentTypeMismatchException) ex;
            returnValue = ReturnValue.buildErrorResult(400, String.format("入参%s的值类型错误", methodArgumentTypeMismatchException.getName()));
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            BindingResult exceptions = methodArgumentNotValidException.getBindingResult();
            if (exceptions.hasErrors()) {
                List<ObjectError> errors = exceptions.getAllErrors();
                if (!errors.isEmpty()) {
                    FieldError fieldError = (FieldError) errors.get(0);
                    returnValue = ReturnValue.buildErrorResult(400, fieldError.getDefaultMessage());
                }
            }

            if (returnValue == null) {
                returnValue = ReturnValue.buildErrorResult(400, message);
            }

        } else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;
            if (!CollectionUtils.isEmpty(constraintViolationException.getConstraintViolations())) {
                ConstraintViolation constraintViolation = constraintViolationException.getConstraintViolations().stream().findFirst().get();
                String fieldName = constraintViolation.getPropertyPath().toString();
                fieldName = fieldName.indexOf(".") != -1 ? fieldName.substring(fieldName.indexOf(".") + 1) : fieldName;
                returnValue = ReturnValue.buildErrorResult(400, String.format("参数%s错误：%s", fieldName, constraintViolation.getMessage()));
            }
        }

        if (returnValue == null) {
            log.error("全局异常", ex);
            if ("online".equalsIgnoreCase(profiles)) {
                return ReturnValue.buildErrorResult(500, "服务器内部错误，请稍后重试。");
            }
            return ReturnValue.buildErrorResult(500, message);
        }

        log.debug("全局异常", ex);
        return returnValue;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNoHandlerException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Throwable ex) {
        httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
    }


    @ExceptionHandler(BindException.class)
    public ReturnValue handleBindException(BindException e) {
        List<ObjectError> objectErrorList = e.getAllErrors();

        return ReturnValue.buildErrorResult(-1, objectErrorList.stream().findFirst()
                .orElse(new ObjectError("object", "参数错误")).getDefaultMessage());
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //add response header
        try {
            if (body instanceof ReturnValue) {
                ReturnValue returnValue = (ReturnValue) body;
                response.getHeaders().set("Content-Hash", DigestUtils.md5DigestAsHex(objectMapper.writeValueAsString(returnValue).getBytes(StandardCharsets.UTF_8)));
                response.getHeaders().set("lastupdate", String.valueOf(System.currentTimeMillis() / 1000));
            } else if (body instanceof String) {
                String returnValue = (String) body;
                response.getHeaders().set("Content-Hash", DigestUtils.md5DigestAsHex(returnValue.getBytes(StandardCharsets.UTF_8)));
                response.getHeaders().set("lastupdate", String.valueOf(System.currentTimeMillis() / 1000));
            }
        } catch (JsonProcessingException e) {
            log.warn("fail to add content-hash response header", e);
        }
        return body;
    }
}
