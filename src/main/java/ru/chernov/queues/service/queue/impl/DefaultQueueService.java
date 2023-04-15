package ru.chernov.queues.service.queue.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.chernov.queues.config.properties.TopicProperties;
import ru.chernov.queues.config.properties.TopicProperties.QueueConfig;
import ru.chernov.queues.service.TopicStore;
import ru.chernov.queues.service.queue.QueueProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import static ru.chernov.queues.consts.Profiles.DEFAULT_QUEUE;


@Profile(DEFAULT_QUEUE)
@Service
public class DefaultQueueService implements QueueProvider, InitializingBean {
    private static final Map<String, Queue<Object>> QUEUES = new HashMap<>();

    private final TopicProperties topicProperties;
    private final TopicStore topicStore;


    public DefaultQueueService(TopicProperties topicProperties, TopicStore topicStore) {
        this.topicProperties = topicProperties;
        this.topicStore = topicStore;
    }


    @Override
    public void afterPropertiesSet() {
        Map<String, QueueConfig> queueConfigs = topicProperties.getConfigs();
        for (Map.Entry<String, QueueConfig> configEntry : queueConfigs.entrySet()) {
            String topic = configEntry.getKey();
            Integer queueSize = configEntry.getValue().getSize();
            QUEUES.put(topic, new ArrayBlockingQueue<>(queueSize));
        }
    }


    @Override
    public Optional<Object> consume(String topic) {
        topicStore.validate(topic);

        Queue<Object> queue = QUEUES.get(topic);
        Object value = queue.poll();
        return Optional.ofNullable(value);
    }


    @Override
    public void produce(String topic, Object object) {
        topicStore.validate(topic);

        Queue<Object> queue = QUEUES.get(topic);
        queue.add(object);
    }

}
