package ru.chernov.queues.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "application.redisson")
public class RedissonProperties {
    private String password;
    private String url;
    private String prefix;

}
