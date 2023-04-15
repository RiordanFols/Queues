package ru.chernov.queues;

import org.springframework.test.context.ActiveProfiles;
import ru.chernov.queues.consts.Profiles;


@ActiveProfiles(Profiles.REDIS_QUEUE)
public class RedisQueueTest extends AbstractQueueTest {
}
