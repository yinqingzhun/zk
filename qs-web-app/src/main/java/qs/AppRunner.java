package qs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import qs.config.SampleProperties;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class AppRunner implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(AppRunner.class);
//    @Autowired
//    SampleProperties sampleProperties;



    private DefaultRedisScript incrementAwardIdScript;

    @PostConstruct
    public void init() {
        DefaultRedisScript redisScriptForSetNx = new DefaultRedisScript();
        redisScriptForSetNx.setLocation(new DefaultResourceLoader().getResource("classpath:/redis-lua/increment_award_id.lua"));
        redisScriptForSetNx.setResultType(String.class);
        this.incrementAwardIdScript = redisScriptForSetNx;


    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        logger.info("sample host:{},port:{}", sampleProperties.getHost(), sampleProperties.getPort());




    }
}
