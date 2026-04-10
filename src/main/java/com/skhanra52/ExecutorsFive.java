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
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Class to pass the thread name in newSingleThreadExecutor(). Created the factory method to pass the name.
 * SingleThreadExecutor can be used to create a multithreaded environment by executing multiple of them.
 *  blueExecutor.shutdown() and other shutdown() will wait till the all the thread execution or till
 *  we get any exception.
 */
class ColorThreadFactory implements ThreadFactory{
    private String threadName;

    public ColorThreadFactory(ThreadColor color){
        this.threadName = color.name();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(threadName);
        return thread;
    }
}

public class ExecutorsFive {

    public static void main(String[] args) {
//        var blueExecutor = Executors.newSingleThreadExecutor(); // here we can not pass name directly.
        var blueExecutor = Executors.newSingleThreadExecutor(
                new ColorThreadFactory(ThreadColor.ANSI_BLUE));
        blueExecutor.execute(ExecutorsFive::countDown);
        blueExecutor.shutdown();
        boolean isDone = false;
        try {
            isDone = blueExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(isDone){
            System.out.println("Blue thread executed successfully, yellow thread started running");
            var yellowExecutor = Executors.newSingleThreadExecutor(
                    new ColorThreadFactory(ThreadColor.ANSI_YELLOW));
            yellowExecutor.execute(ExecutorsFive::countDown);
            yellowExecutor.shutdown();
            try {
                isDone = yellowExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (isDone){
                System.out.println("Yellow thread executed successfully, Red thread started running");
                var redExecutor = Executors.newSingleThreadExecutor(
                        new ColorThreadFactory(ThreadColor.ANSI_RED));
                redExecutor.execute(ExecutorsFive::countDown);
                redExecutor.shutdown();
                try {
                    isDone = redExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(isDone){
                    System.out.println("All the threads executed successfully");
                }
            }
        }
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
