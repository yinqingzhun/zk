package qs.web.controller;

import org.springframework.stereotype.Controller;

import javax.servlet.*;
import java.io.IOException;

/**
 * BeanNameUrlHandlerMapping example
 */
@Controller("/welcome")
public class WelcomeController implements Servlet {


    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.getWriter().write("hello");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
