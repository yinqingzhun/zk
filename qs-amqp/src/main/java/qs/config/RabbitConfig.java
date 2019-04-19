package qs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitConfig {
    
    /*@Bean
    AsyncRabbitTemplate asyncRabbitTemplate(CachingConnectionFactory factory, RabbitTemplate rabbitTemplate){
        AsyncRabbitTemplate asyncRabbitTemplate=new AsyncRabbitTemplate(factory,rabbitTemplate,);
        return asyncRabbitTemplate;
    }*/


    @Bean
    public ConnectionFactory cachingConnectionFactoryLocal(RabbitProperties rabbitMqProperties) throws Exception {
        return createCachingConnectionFactory(rabbitMqProperties);
    }


   /* @Bean
    public CachingConnectionFactory cachingConnectionFactoryAnother() {
        return new CachingConnectionFactory("localhost", 5672);
    }*/

    /*@Bean
    AbstractRoutingConnectionFactory simpleRoutingConnectionFactory() {
        Map<Object, ConnectionFactory> map = new HashMap() {
            {
                put("cachingConnectionFactoryLocal", cachingConnectionFactoryLocal());
                put("cachingConnectionFactoryAnother", cachingConnectionFactoryAnother());
            }
        };


        AbstractRoutingConnectionFactory connectionFactory = new SimpleRoutingConnectionFactory();
        connectionFactory.setTargetConnectionFactories(map);

        return connectionFactory;
    }
*/
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cachingConnectionFactoryLocal) throws Exception {
        //ConnectionFactory cachingConnectionFactoryLocal = cachingConnectionFactoryLocal();
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactoryLocal);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->
        {
            if (!ack) {
                log.warn("消息发送失败。cause: {}, correlationData:{}", cause, correlationData);
            } else {
                log.info("消息发送成功");
            }
        });
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
            log.info("消息发送成功。message:{}, replyCode:{}, replyText:{}, exchange:{}, routingKey:{}", message, replyCode, replyText, exchange, routingKey);

        });
        
        return rabbitTemplate;
    }

    private static CachingConnectionFactory createCachingConnectionFactory(RabbitProperties rabbitProperties) throws Exception {
        RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
        factory.setUseNio(true);
        
        if (rabbitProperties.determineHost() != null) {
            factory.setHost(rabbitProperties.determineHost());
        }
        factory.setPort(rabbitProperties.determinePort());
        if (rabbitProperties.determineUsername() != null) {
            factory.setUsername(rabbitProperties.determineUsername());
        }
        if (rabbitProperties.determinePassword() != null) {
            factory.setPassword(rabbitProperties.determinePassword());
        }
        if (rabbitProperties.determineVirtualHost() != null) {
            factory.setVirtualHost(rabbitProperties.determineVirtualHost());
        }
        if (rabbitProperties.getRequestedHeartbeat() != null) {
            factory.setRequestedHeartbeat(rabbitProperties.getRequestedHeartbeat());
        }
        RabbitProperties.Ssl ssl = rabbitProperties.getSsl();
        if (ssl.isEnabled()) {
            factory.setUseSSL(true);
            if (ssl.getAlgorithm() != null) {
                factory.setSslAlgorithm(ssl.getAlgorithm());
            }
            factory.setKeyStore(ssl.getKeyStore());
            factory.setKeyStorePassphrase(ssl.getKeyStorePassword());
            factory.setTrustStore(ssl.getTrustStore());
            factory.setTrustStorePassphrase(ssl.getTrustStorePassword());
        }
        if (rabbitProperties.getConnectionTimeout() != null) {
            factory.setConnectionTimeout(rabbitProperties.getConnectionTimeout());
        }
        factory.afterPropertiesSet();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
                factory.getObject());
        connectionFactory.setAddresses(rabbitProperties.determineAddresses());
        connectionFactory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
        connectionFactory.setPublisherReturns(rabbitProperties.isPublisherReturns());
        if (rabbitProperties.getCache().getChannel().getSize() != null) {
            connectionFactory
                    .setChannelCacheSize(rabbitProperties.getCache().getChannel().getSize());
        }
        if (rabbitProperties.getCache().getConnection().getMode() != null) {
            connectionFactory
                    .setCacheMode(rabbitProperties.getCache().getConnection().getMode());
        }
        if (rabbitProperties.getCache().getConnection().getSize() != null) {
            connectionFactory.setConnectionCacheSize(
                    rabbitProperties.getCache().getConnection().getSize());
        }
        if (rabbitProperties.getCache().getChannel().getCheckoutTimeout() != null) {
            connectionFactory.setChannelCheckoutTimeout(
                    rabbitProperties.getCache().getChannel().getCheckoutTimeout());
        }
        
        return connectionFactory;
    }
}
