package qs.amqp.config;

import brave.Tracing;
import brave.spring.rabbit.SpringRabbitTracing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceConfig {

    @Bean
    public Tracing tracing() {
        return Tracing.newBuilder()
                .localServiceName("spring-amqp-producer")
                .build();
    }

    @Bean
    public SpringRabbitTracing springRabbitTracing(Tracing tracing) {
        return SpringRabbitTracing.newBuilder(tracing)
                //.writeB3SingleFormat(true) // for more efficient propagation
                .remoteServiceName("my-mq-service")
                .build();
    }
    
    
}


