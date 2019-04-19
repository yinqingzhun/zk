package qs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Slf4j
@Configuration
public class AmqpAppRunner implements ApplicationRunner {
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        sendMsg();
    }

    //@Scheduled(fixedDelay = 1000)
    public void sendMsg() {
        try {
            rabbitTemplate.convertAndSend("gzExchange", "follow.app.start", new Date().getTime());
            log.info("send a message to mq");
        } catch (AmqpException e) {
            log.warn("failed to send a message to mq,{}", e.getMessage());
        }
    }

}
