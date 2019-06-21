package qs.web.controller;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
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
public class HomeController implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean {
    @Autowired
    WindService windService;
    @Autowired
    HelloService helloService;
    @Autowired
    StudentService studentService;
    //@Autowired
    //Configuration cfg;
    //@Autowired
    //FreeMarkerViewResolver freeMarkerViewResolver;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Qualifier("dataSource2")
    DataSource dataSource2;

    @Bean
    public DataSource dataSource2(@Value("${app.datasource.mdb.url}") String url,
                                  @Value("${app.datasource.mdb.username}") String username,
                                  @Value("${app.datasource.mdb.password}") String password) {
        DataSource dataSource = DataSourceBuilder.create().url(url).username(username).password(password).build();
        return dataSource;
    }

    @RequestMapping({"", "/"})
    public String index(Model model, HttpServletRequest request, @RequestParam(value = "logType", required = true, defaultValue = "redis") String logType) throws Exception {
        model.addAttribute("name", "demo");
        int v = 00;
        CarPriceCard carPriceCard = new CarPriceCard() {
            {
                setPriceoffValue(v);
                if (v > 0)
                    setPriceoff(String.format("↓%.2f万", v * 1.0 / 10000));
                else if (v < 0)
                    setPriceoff(String.format("%.2f万", v * 1.0 / 10000));
                else
                    setPriceoff("暂无");

                setSpecName("2019款 东风悦达起亚K5");
                setDealPrice(String.format("%.2f万", 1234870 * 1.0 / 10000));
                setFactoryPrice(String.format("%.2f万", 1534870 * 1.0 / 10000));

                setPromotionCondition("店内上保险，店内加装饰,店内上保险，店内加装饰,店内上保险，店内加装饰");
            }
        };

        model.addAttribute("m", carPriceCard);
        //String name = uploadFile(request);
        //FreeMarkerView view = (FreeMarkerView) freeMarkerViewResolver.resolveViewName("demo", Locale.SIMPLIFIED_CHINESE);
        //createStaticPage(cfg, view.getUrl(), "demo", Collections.emptyMap());
        //throw new SQLException("db");
        
        /* Statement statement = dataSource2.getConnection().createStatement();
        statement.execute("update student set name='1'   where id=1");*/

        Student student = studentService.get(1);
        /*Student student = new Student();
        student.setId(1);
        student.setAge(6 + new Random().nextInt(20));
        studentService.save(student);*/

        /* HttpHelper.httpGet("http://www.google.com");*/

        Wind wind = windService.get(1);
        log.info("wind:{}", wind == null ? "null" : JsonHelper.serialize(wind));


        String key = ("SlowLogCenter:" + logType + ":" + DateHelper.serialize(new Date(), "MMdd")).toLowerCase();
        Set<ZSetOperations.TypedTuple<String>> sqlSet = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 100);
        List<SlowLogCenter.LogEvent> slowLogs = new LinkedList();
        sqlSet.forEach(p -> slowLogs.add(JsonHelper.deserialize(p.getValue(), SlowLogCenter.LogEvent.class)));

        model.addAttribute("logs", slowLogs);
        return "carSpecPrice";
    }

    @ExceptionHandler({SQLException.class})
    public String dr(Exception exception) {
        // Nothing to do. Return value 'databaseError' used as logical view name
        // of an error page, passed to view-resolver(s) in usual way.
        log.error("Request raised: " + exception.getClass().getSimpleName());
        return "error";
    }


    @ResponseBody
    @RequestMapping("/xml")
    public User xml(User user) {
        return user;
    }


   /* public static void createStaticPage(Configuration cfg
            , String templateFileName
            , String staticPageName
            , Map<String, Object> data
    ) {
        String staticPagePath = "e:";
        try {
            cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);// 设置标签
            Template temp = cfg.getTemplate(templateFileName);// 获取模板对象
            String target = staticPagePath + "/" + staticPageName + ".html";
            Writer out = new OutputStreamWriter(new FileOutputStream(target), "UTF-8");
            temp.process(data, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String uploadFile(HttpServletRequest request) throws IOException {
        String fileName = new Date().getTime() + "test";
        String path = getClass().getClassLoader().getResource("").getPath() + "freemarker/" + fileName + ".ftl";

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path.toString()), "utf-8")) {
            writer.write("success");
            writer.flush();
        }

        return fileName;
    }*/

    @RequestMapping("index")
    public String white() throws Exception {
        return "index";
    }

    @RequestMapping(value = "w", produces = "application/json")
    @ResponseBody
    public ReturnValue w() throws Exception {
        return ReturnValue.buildSuccessResult("json");
    }

    @RequestMapping(value = "json", produces = "application/json")
    @ResponseBody
    public ReturnValue json() throws Exception {
        Object o = helloService.hello();
        return ReturnValue.buildSuccessResult(o);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "not found")
    @RequestMapping("ex")
    public String exception(@RequestParam(value = "i", required = false, defaultValue = "-1") int i) throws Exception {
        if (i == 0)
            throw new Exception("ec");
        return "index";
    }

    //@ExceptionHandler(value = HttpStatus.BAD_REQUEST)

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("lifecycle:setBeanFactory");
    }

    @Override
    public void setBeanName(String name) {
        log.info("lifecycle:setBeanName," + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("lifecycle:setApplicationContext");
    }

    @PostConstruct
    public void init() {
        log.info("lifecycle:PostConstruct");
    }

    @PreDestroy
    public void destroy() {
        log.info("lifecycle:PreDestroy");
    }

    public void initMethod() {
        System.out.println("lifecycle:init-method");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("lifecycle:afterPropertiesSet");
    }

    @RequestMapping(value = "/helloadmin", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('admin')")
    public String helloAdmin() {
        return "helloAdmin";
    }

    @RequestMapping(value = "/hellouser", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public String helloUser() {
        return "helloUser";
    }


}
