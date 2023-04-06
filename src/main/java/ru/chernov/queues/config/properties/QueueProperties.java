package ru.chernov.queues.config.properties;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;


@Data
@ConfigurationProperties(prefix = "application.queue")
public class QueueProperties {
    private static final Logger logger = LogManager.getLogger(QueueProperties.class);
    private Map<String, QueueConfig> configs;


    @Data
    public static class QueueConfig {
        private Integer size;

    }

}
