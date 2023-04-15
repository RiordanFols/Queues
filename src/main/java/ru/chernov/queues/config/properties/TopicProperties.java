package ru.chernov.queues.config.properties;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Optional;


@Data
@ConfigurationProperties(prefix = "application.queue")
public class TopicProperties {
    private static final Logger logger = LogManager.getLogger(TopicProperties.class);
    private Map<String, QueueConfig> configs;


    public Optional<QueueConfig> getConfig(String topic) {
        return Optional.ofNullable(configs.get(topic));
    }


    @Data
    public static class QueueConfig {
        private Integer size;

    }

}
