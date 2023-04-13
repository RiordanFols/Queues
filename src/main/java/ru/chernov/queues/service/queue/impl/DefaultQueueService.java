package ru.chernov.queues.service.queue.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.chernov.queues.config.properties.QueueProperties;
import ru.chernov.queues.config.properties.QueueProperties.QueueConfig;
import ru.chernov.queues.exception.QueueTopicNotFoundException;
import ru.chernov.queues.service.queue.QueueProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import static ru.chernov.queues.consts.Profiles.DEFAULT_QUEUE;


/**
 * Для топиков лучше не создавать enum, чтобы можно было докидывать новые топики без изменения кода приложения
 */
@Profile(DEFAULT_QUEUE)
@Service
public class DefaultQueueService implements QueueProvider, InitializingBean {
    private static final Logger logger = LogManager.getLogger(DefaultQueueService.class);
    private static final Map<String, Queue<Object>> QUEUES = new HashMap<>();

    private final QueueProperties queueProperties;


    public DefaultQueueService(QueueProperties queueProperties) {
        this.queueProperties = queueProperties;
    }


    @Override
    public void afterPropertiesSet() {
        Map<String, QueueConfig> queueConfigs = queueProperties.getConfigs();
        for (Map.Entry<String, QueueConfig> configEntry : queueConfigs.entrySet()) {
            var topic = configEntry.getKey();
            var config = configEntry.getValue();
            var queue = new ArrayBlockingQueue<>(config.getSize());
            QUEUES.put(topic, queue);
        }
    }


    @Override
    public Optional<Object> consume(String topic) {
        Queue<Object> queue = get(topic);
        Object value = queue.poll();
        return Optional.ofNullable(value);
    }


    @Override
    public void produce(String topic, Object object) {
        Queue<Object> queue = get(topic);
        queue.add(object);
    }


    private Queue<Object> get(String topic) {
        return Optional.ofNullable(QUEUES.get(topic)).orElseThrow(() -> {
            logger.error("Queue with topic [{}] not found", topic);
            throw new QueueTopicNotFoundException();
        });
    }

}
