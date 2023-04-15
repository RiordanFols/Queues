package ru.chernov.queues;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.chernov.queues.config.properties.TopicProperties;
import ru.chernov.queues.config.properties.RedissonProperties;


@EnableConfigurationProperties({
        TopicProperties.class,
        RedissonProperties.class
})
@SpringBootApplication
public class QueuesApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueuesApplication.class, args);
    }

}
