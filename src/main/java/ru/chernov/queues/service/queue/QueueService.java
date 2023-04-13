package ru.chernov.queues.service.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.chernov.queues.exception.QueueReadException;

import java.util.Optional;


@Service
public class QueueService {
    private static final Logger logger = LogManager.getLogger(QueueService.class);

    private final ObjectMapper objectMapper;
    private final QueueProvider queueProvider;


    public QueueService(ObjectMapper objectMapper, QueueProvider queueProvider) {
        this.objectMapper = objectMapper;
        this.queueProvider = queueProvider;
    }


    public <T> Optional<T> consume(String topic, Class<T> clazz) {
        Optional<Object> value = queueProvider.consume(topic);

        if (value.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(value.get().toString(), clazz));
        } catch (JsonProcessingException e) {
            logger.error("Wrong class for value [{}]", value);
            throw new QueueReadException();
        }
    }


    public void produce(String topic, Object value) {
        queueProvider.produce(topic, value);
    }

}
