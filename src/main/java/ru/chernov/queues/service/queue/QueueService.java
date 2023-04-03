package ru.chernov.queues.service.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.chernov.queues.exception.QueueReadException;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Временная заглушка вместо Редиса
 */
@Service
public class QueueService {
    private static final Logger logger = LogManager.getLogger(QueueService.class);
    private static final Integer QUEUE_SIZE = 1_000;
    private static final Queue<Object> QUEUE = new ArrayBlockingQueue<>(QUEUE_SIZE);

    private final ObjectMapper objectMapper;


    public QueueService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public <T> Optional<T> consume(Class<T> clazz) {
        Object object = QUEUE.poll();
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


    public void produce(Object object) {
        QUEUE.add(object);
    }


}
