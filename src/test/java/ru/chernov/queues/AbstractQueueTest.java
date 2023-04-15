package ru.chernov.queues;

import org.junit.jupiter.api.Test;
import ru.chernov.queues.exception.QueueTopicNotFoundException;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


abstract class AbstractQueueTest extends AbstractTest {
    private static final Random RANDOM = new Random();
    private static final Integer ITERATIONS = 15;
    protected static final String RECEIPTS_TOPIC = "receipts";
    protected static final String NEWS_TOPIC = "news";
    protected static final String WRONG_TOPIC = "wrong_topic12345";


    @Test
    void produce() throws Exception {
        assertNull(produce(NEWS_TOPIC).call());
    }


    @Test
    void consume() throws Exception {
        assertNull(consume(NEWS_TOPIC).call());
    }


    @Test
    void produceWrongTopic() throws Exception {
        Exception exception = produce(WRONG_TOPIC).call();
        assertNotNull(exception);
        assertTrue(exception instanceof QueueTopicNotFoundException);
    }


    @Test
    void consumeWrongTopic() throws Exception {
        Exception exception = consume(WRONG_TOPIC).call();
        assertNotNull(exception);
        assertTrue(exception instanceof QueueTopicNotFoundException);
    }


    @Test
    void produceAndConsume() throws Exception {
        assertNull(produce(NEWS_TOPIC).call());
        assertNull(consume(NEWS_TOPIC).call());
    }


    @Test
    void produceAndConsumeOneTopicParallel() throws InterruptedException, ExecutionException {
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
    void produceAndConsumeTwoTopicsParallel() throws InterruptedException, ExecutionException {
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
    void produceAndConsumeWrongTopicParallel() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Exception> produceFuture = executorService.submit(produce(WRONG_TOPIC));
        Future<Exception> consumeFuture = executorService.submit(consume(WRONG_TOPIC));

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertTrue(terminated);
        Exception produceException = produceFuture.get();
        assertNotNull(produceException);
        assertTrue(produceException instanceof QueueTopicNotFoundException);

        Exception consumeException = consumeFuture.get();
        assertNotNull(consumeException);
        assertTrue(consumeException instanceof QueueTopicNotFoundException);
    }


    protected Callable<Exception> produce(String topic) {
        return () -> {
            try {
                for (var i = 0; i < ITERATIONS; i++) {
                    queueService.produce(topic, RANDOM.nextInt(10));
                }
            } catch (Exception e) {
                return e;
            }

            return null;
        };
    }


    protected Callable<Exception> consume(String topic) {
        return () -> {
            try {
                for (var i = 0; i < ITERATIONS; i++) {
                    queueService.consume(topic, Integer.class).ifPresent(System.out::println);
                }
            } catch (Exception e) {
                return e;
            }

            return null;
        };
    }

}
