package qs.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    
    @Bean
    RedisTemplate redisTemplate(JedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate=createRedisTemplate(redisConnectionFactory);
        return redisTemplate;
    }
    
    
    private static <T> RedisTemplate<String, T> createRedisTemplate(JedisConnectionFactory redisConnectionFactory) {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        RedisTemplate<String, T> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setStringSerializer(stringRedisSerializer);
        template.setDefaultSerializer(genericJackson2JsonRedisSerializer);
        template.setEnableDefaultSerializer(true);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
    
}
