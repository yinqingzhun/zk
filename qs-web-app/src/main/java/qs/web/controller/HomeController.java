package qs.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qs.config.SlowLogCenter;
import qs.model.ReturnValue;
import qs.model.User;
import qs.model.po.Student;
import qs.model.po.Wind;
import qs.model.vo.CarPriceCard;
import qs.service.HelloService;
import qs.service.StudentService;
import qs.service.WindService;
import qs.util.DateHelper;
import qs.util.JsonHelper;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by yinqingzhun on 2017/08/29.
 */
@Controller
@Slf4j
public class HomeController    {


    @ResponseBody
    @PostMapping("user")
    public Mono<User> user(User user) {
        return Mono.justOrEmpty(user);
    }



}
