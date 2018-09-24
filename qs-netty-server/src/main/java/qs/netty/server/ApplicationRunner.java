package qs.netty.server;

import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class ApplicationRunner implements CommandLineRunner {
    @Value("${netty.server.port}")
    private int port;

    @Value("${netty.server.host}")
    private String host;

    @Autowired
    private NettyServer socketServer;

    @Override
    public void run(String... strings) {
        InetSocketAddress address = new InetSocketAddress(host, port);
        socketServer.run(port);
         
         
    }
}
