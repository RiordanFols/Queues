package ru.chernov.queues.util;

import java.util.concurrent.Callable;


public class ThreadExceptionCatcher {

    public static void catchException(Callable<Exception> callable) throws Exception {
        Exception exception = callable.call();
        if (exception != null) {
            throw new Exception(exception);
        }
    }

}
