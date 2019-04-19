package qs.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AppStartConsumer {
    @RabbitListener(queues = "follow.app.start")
    public void consume(Message message) throws InterruptedException {
        try {
            log.info("收到消息:{}","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        TimeUnit.SECONDS.sleep(1);
    }
}
