//package qs.config.redis;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisReactiveConfig {
//    @Bean
//    public ReactiveRedisTemplate<?, ?> reactiveRedisTemplate(            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory, ResourceLoader resourceLoader) {
//        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//
//        StringRedisSerializer srs = new StringRedisSerializer();
//
//        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer);
//        RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext.<String, Object>newSerializationContext()
//                .key(srs)
//                .value(pair)
//                .hashKey(srs)
//                .hashValue(genericJackson2JsonRedisSerializer)
//                .build();
//
//
//        return new ReactiveRedisTemplate(reactiveRedisConnectionFactory, serializationContext);
//    }
//
//    @Bean
//    ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory){
//        ReactiveStringRedisTemplate reactiveStringRedisTemplate=new ReactiveStringRedisTemplate(reactiveRedisConnectionFactory);
//        return reactiveStringRedisTemplate;
//    }
//
//
//}
