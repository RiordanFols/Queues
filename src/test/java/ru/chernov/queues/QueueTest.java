package ru.chernov.queues;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class QueueTest extends AbstractTest {
    private static final Random RANDOM = new Random();
    private static final Integer ITERATIONS = 15;


    @Test
    void simulation() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(produce());
        executorService.submit(consume());
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(1, TimeUnit.MINUTES);
        assertTrue(terminated);
    }


    private Runnable produce() {
        return () -> {
            for (int i = 0; i < ITERATIONS; i++) {
                queueService.produce(RANDOM.nextInt(10));
            }
        };
    }


    private Runnable consume() {
        return () -> {
            for (int i = 0; i < ITERATIONS; i++) {
                queueService.consume(Integer.class).ifPresent(System.out::println);
            }
        };
    }

}
