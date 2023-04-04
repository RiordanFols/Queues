package ru.chernov.queues;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.chernov.queues.config.properties.QueueProperties;


@EnableConfigurationProperties({
        QueueProperties.class
})
@SpringBootApplication
public class QueuesApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueuesApplication.class, args);
    }

}
