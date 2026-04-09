package com.skhanra52;
/*
Managing Threads:
These are the ExecutorService classes, and they exist to manage the creating and execution of threads.
Managing Individual thread:
When using the Thread class, we would have rudimentary(very Basis) control over the thread.
-> We can interrupt a thread and join it to another thread.
-> We can name the thread, try to prioritize it, and start it manually one at a time.
-> We can also pass it an UncaughtExceptionHandler, to deal with exception that happen in a thread.
Managing individual threads manually could be challenging, complex and error-prone.
It can lead to a complex issues like resource contention, thread creation overhead, and scalability
changes. For these reasons, we'll want to use an ExecutorService, even when working with a single thread.

Benefits of managing threads with an implementation of ExecutorService:
The ExecutorService type in java is an interface. Java provides several implementations of this type
which provides the following benefits:
 -> Simplifies thread management by abstracting execution to the level of tasks which needs to be run.
 -> Use thread pools, reducing the cost of creating new threads.
 -> Efficient scaling by utilizing multiple processor cores.
 -> Built-in synchronization, reducing concurrency related errors.
 -> Graceful shutdown, preventing resource leaks.
 -> Scheduled implementations exist to further help with management workflows.
*/

import java.util.concurrent.Executors;

public class ExecutorsFive {

    public static void main(String[] args) {
        var blueExecutor = Executors.newSingleThreadExecutor();
        blueExecutor.execute(ExecutorsFive::countDown);
        blueExecutor.shutdown();
    }

    // without executorService, if you want to run below main rename the "notMain" to main.
    public static void notMain(String[] args) {

        Thread blue = new Thread(
                ExecutorsFive::countDown,  ThreadColor.ANSI_BLUE.name());

        Thread yellow = new Thread(
                ExecutorsFive::countDown, ThreadColor.ANSI_YELLOW.name());

        Thread red = new Thread(
                ExecutorsFive::countDown, ThreadColor.ANSI_RED.name());

        // Arranging start and join respectively will produce synchronous output, one after another.
        blue.start();
        try {
            blue.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        yellow.start();
        try {
            yellow.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        red.start();
        try {
            red.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void countDown(){
        String threadName = Thread.currentThread().getName();
        var threadColor = ThreadColor.ANSI_RESET;
        try{
            threadColor = ThreadColor.valueOf(threadName.toUpperCase());
        } catch (IllegalArgumentException ignore) {
            // User may pass a bad color name, will just ignore this kind of error.
        }

        String color = threadColor.getColor();
        for(int i=20; i>=0; i--){
            System.out.println(color + " " +
                    threadName.replace("ANSI_", "") + " " + i);
        }
    }
}
