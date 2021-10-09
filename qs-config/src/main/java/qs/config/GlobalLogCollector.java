package qs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

@Configuration
public class GlobalLogCollector {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Scheduled(fixedDelay = 10000)
    public void print() {
        Map<String, CacheAppender.LogCounter> map = CacheAppender.empty();
        map.forEach((k, v) -> {
            System.out.println(k + "--->" + v);
            stringRedisTemplate.opsForValue().set(k + ":c", String.valueOf(v.counter.get()));
            stringRedisTemplate.opsForValue().set(k + ":t", String.valueOf(v.stackTrace));
        });
    }


}
