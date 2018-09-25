package qs.netty.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {
   

    @Autowired
    private NettyServer socketServer;

    @Override
    public void run(String... strings) {
        socketServer.run();
    }
}
