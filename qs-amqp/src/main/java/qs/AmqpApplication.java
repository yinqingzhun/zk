package qs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { })
@EnableScheduling
public class AmqpApplication {
    public static void main(String[] args) {
       new SpringApplicationBuilder().sources(AmqpApplication.class).run(args);
    }
}
