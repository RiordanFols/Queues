package ru.chernov.queues;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;


// TODO: отлавливать эксепшены в потоках
public class QueueTest extends AbstractTest {
    private static final String RECEIPTS_TOPIC = "receipts";
    private static final String NEWS_TOPIC = "news";
    private static final Random RANDOM = new Random();
    private static final Integer ITERATIONS = 15;


    @Test
    void produce() {
        produce(NEWS_TOPIC).run();
    }


    @Test
    void consumeEmpty() {
        consume(NEWS_TOPIC).run();
    }


    @Test
    void produceAndConsume() {
        produce(NEWS_TOPIC).run();
        consume(NEWS_TOPIC).run();
    }


    @Test
    void produceAndConsumeOneTopicParallel() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(produce(NEWS_TOPIC));
        executorService.submit(consume(NEWS_TOPIC));

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(1, TimeUnit.MINUTES);
        assertTrue(terminated);
    }


    @Test
    void produceAndConsumeTwoTopicsParallel() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(produce(NEWS_TOPIC));
        executorService.submit(produce(RECEIPTS_TOPIC));
        executorService.submit(consume(NEWS_TOPIC));
        executorService.submit(consume(RECEIPTS_TOPIC));

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(1, TimeUnit.MINUTES);
        assertTrue(terminated);
    }


    private Runnable produce(String topic) {
        return () -> {
            for (var i = 0; i < ITERATIONS; i++) {
                queueService.produce(topic, RANDOM.nextInt(10));
            }
        };
    }


    private Runnable consume(String topic) {
        return () -> {
            for (var i = 0; i < ITERATIONS; i++) {
                queueService.consume(topic, Integer.class).ifPresent(System.out::println);
            }
        };
    }

}
