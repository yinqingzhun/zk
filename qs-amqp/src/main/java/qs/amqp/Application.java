package qs.amqp;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import qs.util.DateHelper;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
@EnableRabbit
@EnableConfigurationProperties
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(Application.class).run(args);
    }

    @Autowired
    RabbitTemplate rabbitTemplate;
    
    @Bean
    ApplicationRunner applicationRunner(){
        return (args)->{
            rabbitTemplate.convertAndSend("gzExchange","follow.app.start","hi,java");
        };
    }

}
