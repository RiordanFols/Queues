package ru.chernov.queues.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.chernov.queues.config.properties.TopicProperties;
import ru.chernov.queues.exception.QueueTopicNotFoundException;


@Service
public class TopicService {
    private static final Logger logger = LogManager.getLogger(TopicService.class);

    private final TopicProperties topicProperties;


    public TopicService(TopicProperties topicProperties) {
        this.topicProperties = topicProperties;
    }


    public void validate(String topic) {
        if (topicProperties.getConfig(topic).isEmpty()) {
            logger.error("Topic [{}] not found", topic);
            throw new QueueTopicNotFoundException();
        }
    }

}
