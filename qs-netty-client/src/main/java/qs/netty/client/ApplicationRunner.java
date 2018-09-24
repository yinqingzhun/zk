package qs.netty.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
public class ApplicationRunner implements CommandLineRunner {
    @Value("${netty.server.port}")
    private int port;

    @Value("${netty.server.host}")
    private String host;


    @Override
    public void run(String... strings) {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 7000);
        String message = "hello";
        NettyClient nettyClient = null;
        try {
            nettyClient = new NettyClient(address);
            Object result = nettyClient.send(message);
            log.info("response : " + result);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            nettyClient.close();
        }
    }
}
