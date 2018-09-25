package qs.amqp.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessgeConsumer {
    @RabbitListener(queues = "follow.app.start",containerFactory = "rabbitListenerContainerFactory")
    public void process(String msg) {
        log.info("msg: {}",msg);
    }
}
