package qs.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import qs.exception.SupportInfoException;
import qs.model.ReturnValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@ControllerAdvice
@Slf4j
public class WebControllerAdvice {
    Logger logger = LoggerFactory.getLogger(WebControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(ReturnValue.buildErrorResult(status.value(), ex.getMessage()), status);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNoHandlerException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Throwable ex) {

        int status = httpServletRequest.getMethod().equalsIgnoreCase("get") ? HttpStatus.SEE_OTHER.value() : HttpStatus.TEMPORARY_REDIRECT.value();
        httpServletResponse.setStatus(status);

        String location;
        if (StringUtils.hasText(httpServletRequest.getQueryString())) {
            location = MessageFormat.format("http://localhost:8888{0}?{1}", httpServletRequest.getRequestURI(), httpServletRequest.getQueryString());
        } else {
            location = MessageFormat.format("http://localhost:8888{0}", httpServletRequest.getRequestURI());
        }

        httpServletResponse.setHeader("Location", location);

        log.info("redirect: {}, status: {}", location, httpServletResponse.getStatus());
        return;
    }


    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    /* . . . . . . . . . . . . . EXCEPTION HANDLERS . . . . . . . . . . . . .. */
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    /**
     * Convert a predefined exception to an HTTP Status code
     */
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")
    // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {
        logger.error("Request raised a DataIntegrityViolationException");
        // Nothing to do
    }

    // 500
    @ExceptionHandler(ArithmeticException.class)
    public String arithmetic() {
        logger.error("divide by zero");
        return "welcome";
        // Nothing to do
    }

    /**
     * Convert a predefined exception to an HTTP Status code and specify the
     * name of a specific view that will be used to display the error.
     *
     * @return exception view.
     */
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError(Exception exception) {
        // Nothing to do. Return value 'databaseError' used as logical view name
        // of an error page, passed to view-resolver(s) in usual way.
        logger.error("Request raised " + exception.getClass().getSimpleName());
        return "error";
    }

    /**
     * Demonstrates how to take total control - setup a model, add useful
     * information and return the "support" view name. This method explicitly
     * creates and returns
     *
     * @param req       Current HTTP request.
     * @param exception The exception thrown - always {@link SupportInfoException}.
     * @return The model and view used by the DispatcherServlet to generate
     * output.
     * @throws Exception
     */
    @ExceptionHandler(SupportInfoException.class)
    public ModelAndView handleError(HttpServletRequest req, Exception exception)
            throws Exception {

        // Rethrow annotated exceptions or they will be processed here instead.
        if (AnnotationUtils.findAnnotation(exception.getClass(),
                ResponseStatus.class) != null)
            throw exception;

        logger.error("Request: " + req.getRequestURI() + " raised " + exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("timestamp", new Date().toString());
        mav.addObject("status", 500);

        mav.setViewName("support");
        return mav;
    }

}
