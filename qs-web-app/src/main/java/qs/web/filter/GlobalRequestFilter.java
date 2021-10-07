//package qs.web.filter;
//
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.servlet.DispatcherServlet;
//import org.springframework.web.servlet.HandlerExecutionChain;
//import org.springframework.web.servlet.HandlerMapping;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpServletResponseWrapper;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@Slf4j
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//
//public class GlobalRequestFilter extends OncePerRequestFilter {
//    @Autowired
//    DispatcherServlet dispatcherServlet;
//
//    List<HandlerMapping> handlerMappings;
//
//    AtomicBoolean initialized = new AtomicBoolean(false);
//
//
//    private void init() {
//        try {
//            Field field = DispatcherServlet.class.getDeclaredField("handlerMappings");
//            field.setAccessible(true);
//            handlerMappings = (List<HandlerMapping>) field.get(dispatcherServlet);
//            handlerMappings.forEach(p -> log.info(p.toString()));
//        } catch (Exception e) {
//            log.warn("no HandlerMappings found", e);
//            throw new RuntimeException("no HandlerMappings found");
//        }
//
//    }
//
//    @SneakyThrows
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        if (!initialized.get()) {
//            synchronized (this) {
//                if (initialized.compareAndSet(false, true)) {
//                    init();
//                }
//            }
//        }
//
//
//        if (getHandler(httpServletRequest) == null) {
//
//            int status = httpServletRequest.getMethod().equalsIgnoreCase("get") ? HttpStatus.SEE_OTHER.value() : HttpStatus.TEMPORARY_REDIRECT.value();
//            httpServletResponse.setStatus(status);
//
//            String location = "http://localhost:8888" + httpServletRequest.getRequestURI();
//            httpServletResponse.setHeader("Location", location);
//
//            log.info("redirect: {}, status: {}", location, httpServletResponse.getStatus());
//            return;
//        }
//
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//
//        //        HttpServletResponse response = new HttpServletResponseExt(httpServletResponse);
//        //        filterChain.doFilter(httpServletRequest, httpServletResponse);
//        //        if (httpServletResponse.getStatus() == HttpStatus.NOT_FOUND.value()) {
//        //
//        //            int status = httpServletRequest.getMethod().equalsIgnoreCase("get") ? HttpStatus.SEE_OTHER.value() : HttpStatus.TEMPORARY_REDIRECT.value();
//        //            httpServletResponse.setStatus(status);
//        //
//        //            String location = "http://localhost:8888" + httpServletRequest.getRequestURI();
//        //            httpServletResponse.setHeader("Location", location);
//        //
//        //            log.info("redirect: {}, status: {}", location, httpServletResponse.getStatus());
//        //        }
//
//
//    }
//
//    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
//        for (HandlerMapping hm : this.handlerMappings) {
//            HandlerExecutionChain handler = hm.getHandler(request);
//            if (handler != null) {
//                return handler;
//            }
//        }
//        return null;
//    }
//
//
//    public static class HttpServletResponseExt extends HttpServletResponseWrapper {
//        HttpServletResponse response;
//        int status;
//        String msg;
//
//        public HttpServletResponseExt(HttpServletResponse response) {
//            super(response);
//            this.response = response;
//        }
//
//        @Override
//        public void setStatus(int sc) {
//            this.status = sc;
//        }
//
//        @Override
//        public int getStatus() {
//            return status;
//        }
//
//        @Override
//        public void sendError(int sc, String msg) throws IOException {
//            setStatus(sc);
//            this.msg = msg;
//        }
//    }
//}
