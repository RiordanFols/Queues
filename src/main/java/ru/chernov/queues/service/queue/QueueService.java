package ru.chernov.queues.service.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import ru.chernov.queues.config.properties.QueueProperties;
import ru.chernov.queues.config.properties.QueueProperties.QueueConfig;
import ru.chernov.queues.exception.QueueReadException;
import ru.chernov.queues.exception.QueueTopicNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Для топиков лучше не создавать enum, чтобы можно было докидывать новые топики без изменения кода приложения
 */
@Service
public class QueueService implements InitializingBean {
    private static final Logger logger = LogManager.getLogger(QueueService.class);
    private static final Map<String, Queue<Object>> QUEUES = new HashMap<>();

    private final QueueProperties queueProperties;
    private final ObjectMapper objectMapper;


    public QueueService(QueueProperties queueProperties, ObjectMapper objectMapper) {
        this.queueProperties = queueProperties;
        this.objectMapper = objectMapper;
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


    public <T> Optional<T> consume(String topic, Class<T> clazz) {
        Queue<Object> queue = get(topic);
        var object = queue.poll();

        if (object == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(object.toString(), clazz));
        } catch (JsonProcessingException e) {
            logger.error("Wrong class for value [{}]", object);
            throw new QueueReadException();
        }
    }


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
