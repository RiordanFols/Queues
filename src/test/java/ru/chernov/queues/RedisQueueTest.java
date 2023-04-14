package ru.chernov.queues;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.chernov.queues.consts.Profiles;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles({Profiles.REDIS_QUEUE})
class RedisQueueTest extends AbstractQueueTest {

    @Test
    @Override
    void produce() throws Exception {
        assertNull(produce(NEWS_TOPIC).call());
    }


    @Test
    @Override
    void consume() throws Exception {
        assertNull(consume(NEWS_TOPIC).call());
    }


    @Test
    @Override
    void produceWrongTopic() throws Exception {
        Exception exception = produce(WRONG_TOPIC).call();
        assertNull(exception);
        assertNotNull(consume(WRONG_TOPIC));
    }


    @Test
    @Override
    void consumeWrongTopic() throws Exception {
        Exception exception = consume(WRONG_TOPIC).call();
        assertNull(exception);
    }


    @Test
    @Override
    void produceAndConsume() throws Exception {
        assertNull(produce(NEWS_TOPIC).call());
        assertNull(consume(NEWS_TOPIC).call());
    }


    @Test
    @Override
    void produceAndConsumeOneTopicParallel() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Exception> produceFuture = executorService.submit(produce(NEWS_TOPIC));
        Future<Exception> consumeFuture = executorService.submit(consume(NEWS_TOPIC));

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertTrue(terminated);
        assertNull(produceFuture.get());
        assertNull(consumeFuture.get());
    }


    @Test
    @Override
    void produceAndConsumeTwoTopicsParallel() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<Exception> newsProduceFuture = executorService.submit(produce(NEWS_TOPIC));
        Future<Exception> receiptsProduceFuture = executorService.submit(produce(RECEIPTS_TOPIC));
        Future<Exception> newsConsumeFuture = executorService.submit(consume(NEWS_TOPIC));
        Future<Exception> receiptsConsumeFuture = executorService.submit(consume(RECEIPTS_TOPIC));

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertTrue(terminated);
        assertNull(newsProduceFuture.get());
        assertNull(receiptsProduceFuture.get());
        assertNull(newsConsumeFuture.get());
        assertNull(receiptsConsumeFuture.get());
    }


    @Test
    @Override
    void produceAndConsumeWrongTopicParallel() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Exception> produceFuture = executorService.submit(produce(WRONG_TOPIC));
        Future<Exception> consumeFuture = executorService.submit(consume(WRONG_TOPIC));

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertTrue(terminated);
        assertNull(produceFuture.get());
        assertNull(consumeFuture.get());
    }

}
