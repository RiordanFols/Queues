package ru.chernov.queues.service.queue;

import java.util.Optional;


public interface QueueProvider {

    Optional<Object> consume(String topic);

    void produce(String topic, Object value);

}
