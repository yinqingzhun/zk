package qs.amqp;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import com.sun.deploy.trace.Trace;
import lombok.extern.slf4j.Slf4j;
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
import sun.rmi.runtime.Log;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

@EnableRabbit
@EnableConfigurationProperties
@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(Application.class).run(args);
    }

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    Tracing tracing;

    @Bean
    ApplicationRunner applicationRunner() {
        return (args) -> {
            Tracer tracer= tracing.tracer();
            Span span = tracer.newTrace().name("app.starter").start();
           try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
                rabbitTemplate.convertAndSend("gzExchange", "follow.app.start", "hi,java");
                log.info("send msg to app.starter");
            } finally {
                span.finish();
            }
        };
    }

}
