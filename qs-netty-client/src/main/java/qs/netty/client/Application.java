package qs.netty.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import qs.util.DateHelper;

import java.net.InetSocketAddress;

@EnableConfigurationProperties
@EnableScheduling
@SpringBootApplication
public class Application {
    @Value("${netty.server.port}")
    private int port;
    @Value("${netty.server.host}")
    private String host;
    @Autowired
    NettyClient nettyClient;

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(Application.class).run(args);
    }

    @Bean
    public NettyClient nettyClient() {
        InetSocketAddress address = new InetSocketAddress(host, port);
        return new NettyClient(address);
    }

    @Scheduled(fixedDelay = 5000)
    public void sendMsg() {
        nettyClient.send(DateHelper.getNowString());
    }


}
