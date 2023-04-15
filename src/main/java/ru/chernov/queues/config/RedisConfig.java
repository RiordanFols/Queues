package ru.chernov.queues.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import ru.chernov.queues.config.properties.RedissonProperties;

import java.io.IOException;


@Configuration
public class RedisConfig {
    private static final String CONFIG_PATH = "classpath:/redisson.yaml";


    @Bean
    public RedissonClient redissonClient(@Value(CONFIG_PATH) Resource configFile,
                                         RedissonProperties redissonProperties) throws IOException {
        Config config = Config.fromYAML(configFile.getInputStream());

        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress(redissonProperties.getUrl());

        if (redissonProperties.getPassword() != null) {
            serverConfig.setPassword(redissonProperties.getPassword());
        }

        return Redisson.create(config);
    }

}