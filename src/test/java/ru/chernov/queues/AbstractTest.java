package ru.chernov.queues;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.chernov.queues.service.queue.QueueService;


@SpringBootTest
public abstract class AbstractTest {
    @Autowired
    protected QueueService queueService;

}
