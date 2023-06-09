package ru.chernov.queues.service.queue.impl;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.chernov.queues.config.properties.RedissonProperties;
import ru.chernov.queues.service.TopicService;
import ru.chernov.queues.service.queue.QueueProvider;

import java.util.Optional;

import static ru.chernov.queues.consts.Profiles.REDIS_QUEUE;


@Profile(REDIS_QUEUE)
@Service
public class RedisQueueService implements QueueProvider {
    private final RedissonClient redissonClient;
    private final RedissonProperties redissonProperties;
    private final TopicService topicService;


    public RedisQueueService(RedissonClient redissonClient,
                             RedissonProperties redissonProperties,
                             TopicService topicService) {
        this.redissonClient = redissonClient;
        this.redissonProperties = redissonProperties;
        this.topicService = topicService;
    }


    @Override
    public Optional<Object> consume(String topic) {
        topicService.validate(topic);

        String key = key(topic);
        Object value = redissonClient.getBlockingQueue(key).poll();
        return Optional.ofNullable(value);
    }


    @Override
    public void produce(String topic, Object value) {
        topicService.validate(topic);

        String key = key(topic);
        redissonClient.getBlockingQueue(key).add(value);
    }


    private String key(String key) {
        return redissonProperties.getPrefix() + key;
    }

}
