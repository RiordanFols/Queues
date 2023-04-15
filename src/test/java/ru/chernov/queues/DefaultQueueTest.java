package ru.chernov.queues;

import org.springframework.test.context.ActiveProfiles;
import ru.chernov.queues.consts.Profiles;


@ActiveProfiles(Profiles.DEFAULT_QUEUE)
public class DefaultQueueTest extends AbstractQueueTest {
}
