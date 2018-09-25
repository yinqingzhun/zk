package qs.netty.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder().web(false).sources(Application.class).build().run(args);
    }
   
}
