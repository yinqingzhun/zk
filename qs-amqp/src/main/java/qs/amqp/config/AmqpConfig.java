package qs.amqp.config;

import brave.spring.rabbit.SpringRabbitTracing;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {
    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         SpringRabbitTracing springRabbitTracing) {
        RabbitTemplate rabbitTemplate = springRabbitTracing.newRabbitTemplate(connectionFactory);
        // other customizations as required
        return rabbitTemplate;
    }
/*
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SpringRabbitTracing springRabbitTracing
    ) {
        return springRabbitTracing.newSimpleRabbitListenerContainerFactory(connectionFactory);
    }*/
}
