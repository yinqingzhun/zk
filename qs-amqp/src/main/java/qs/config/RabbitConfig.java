package qs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

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
    public ConnectionFactory cachingConnectionFactoryLocal(RabbitProperties rabbitMqProperties, ObjectProvider<ConnectionNameStrategy> connectionNameStrategy) throws Exception {
        return createCachingConnectionFactory(rabbitMqProperties, connectionNameStrategy);
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

    private static RabbitConnectionFactoryBean getRabbitConnectionFactoryBean(RabbitProperties properties)
            throws Exception {
        PropertyMapper map = PropertyMapper.get();
        RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
        map.from(properties::determineHost).whenNonNull().to(factory::setHost);
        map.from(properties::determinePort).to(factory::setPort);
        map.from(properties::determineUsername).whenNonNull().to(factory::setUsername);
        map.from(properties::determinePassword).whenNonNull().to(factory::setPassword);
        map.from(properties::determineVirtualHost).whenNonNull().to(factory::setVirtualHost);
        map.from(properties::getRequestedHeartbeat).whenNonNull().asInt(Duration::getSeconds)
                .to(factory::setRequestedHeartbeat);
        RabbitProperties.Ssl ssl = properties.getSsl();
        if (ssl.isEnabled()) {
            factory.setUseSSL(true);
            map.from(ssl::getAlgorithm).whenNonNull().to(factory::setSslAlgorithm);
            map.from(ssl::getKeyStoreType).to(factory::setKeyStoreType);
            map.from(ssl::getKeyStore).to(factory::setKeyStore);
            map.from(ssl::getKeyStorePassword).to(factory::setKeyStorePassphrase);
            map.from(ssl::getTrustStoreType).to(factory::setTrustStoreType);
            map.from(ssl::getTrustStore).to(factory::setTrustStore);
            map.from(ssl::getTrustStorePassword).to(factory::setTrustStorePassphrase);
            map.from(ssl::isValidateServerCertificate)
                    .to((validate) -> factory.setSkipServerCertificateValidation(!validate));
            map.from(ssl::getVerifyHostname).to(factory::setEnableHostnameVerification);
        }
        map.from(properties::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis)
                .to(factory::setConnectionTimeout);
        factory.afterPropertiesSet();
        return factory;
    }


    private static CachingConnectionFactory createCachingConnectionFactory(RabbitProperties properties
            , ObjectProvider<ConnectionNameStrategy> connectionNameStrategy) throws Exception {
        PropertyMapper map = PropertyMapper.get();
        CachingConnectionFactory factory = new CachingConnectionFactory(
                getRabbitConnectionFactoryBean(properties).getObject());
        map.from(properties::determineAddresses).to(factory::setAddresses);
        map.from(properties::isPublisherConfirms).to(factory::setPublisherConfirms);
        map.from(properties::isPublisherReturns).to(factory::setPublisherReturns);
        RabbitProperties.Cache.Channel channel = properties.getCache().getChannel();
        map.from(channel::getSize).whenNonNull().to(factory::setChannelCacheSize);
        map.from(channel::getCheckoutTimeout).whenNonNull().as(Duration::toMillis)
                .to(factory::setChannelCheckoutTimeout);
        RabbitProperties.Cache.Connection connection = properties.getCache().getConnection();
        map.from(connection::getMode).whenNonNull().to(factory::setCacheMode);
        map.from(connection::getSize).whenNonNull().to(factory::setConnectionCacheSize);
        map.from(connectionNameStrategy::getIfUnique).whenNonNull().to(factory::setConnectionNameStrategy);
        return factory;
    }

}
