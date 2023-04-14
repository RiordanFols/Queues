package ru.chernov.queues;

import java.util.Random;
import java.util.concurrent.Callable;


public abstract class AbstractQueueTest extends AbstractTest {
    private static final Random RANDOM = new Random();
    private static final Integer ITERATIONS = 15;
    protected static final String RECEIPTS_TOPIC = "receipts";
    protected static final String NEWS_TOPIC = "news";
    protected static final String WRONG_TOPIC = "wrong_topic12345";


    abstract void produce() throws Exception;

    abstract void consume() throws Exception;

    abstract void produceWrongTopic() throws Exception;

    abstract void consumeWrongTopic() throws Exception;

    abstract void produceAndConsume() throws Exception;

    abstract void produceAndConsumeOneTopicParallel() throws Exception;

    abstract void produceAndConsumeTwoTopicsParallel() throws Exception;

    abstract void produceAndConsumeWrongTopicParallel() throws Exception;


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
