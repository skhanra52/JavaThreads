package com.skhanra52;
/*
Managing Threads:
These are the ExecutorService classes, and they exist to manage the creating and execution of threads.
Managing Individual thread:
When using the Thread class, we would have rudimentary(very Basic) control over the thread.
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

/*
 -----What Production-Grade really means here-------------------
 When we use ExecutorService in real systems, we care about:
    1. Thread Management(No Leaks)
    2. Proper pool sizing(Not random numbers)
    3. Graceful shutdown
    4. Error Handling(important)
    5. Avoiding shared mutable state issues
    6. Observability(logging, debugging)

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
    private int colorValue = 1;

    public ColorThreadFactory(ThreadColor color){
        this.threadName = color.name();
    }

    public ColorThreadFactory(){}

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        String name = threadName;
        if(name == null){
            name = ThreadColor.values()[colorValue].name();
        }
        if(++colorValue > (ThreadColor.values().length - 1)){
            colorValue = 1;
        }
//        thread.setName(threadName);
        thread.setName(name);
        return thread;
    }
}

/**
 * Implementation of singleThreaded executor service, where single execution service handling
 * one thread at a time. The threads have been passed to executor service one by one and waited till
 * the task is finished after executor service shutdown(stop taking task) using executor.shutdown().
 * executor.awaitTermination(500,MILLISECONDS) (wait till the existing tasks to complete),
 * awaitTermination does NOT stop threads.
 * ------------
 * How it actually works internally-----------
 * 1. shutdown() is called
 *      No new tasks allowed
 *      Existing tasks continue
 * 2. awaitTermination(...) is called
 *      Main thread goes into waiting state
 *  It checks:
 *      Have all worker threads finished?
 *      Has 500 ms elapsed?
 */

public class ExecutorsFive {

    public static void main(String[] args) {
        int count = 6; // number of tasks we would like to run
        // 3 threads would be created to execute 6 tasks. 3
        // When six tasks were submitted, the three threads were used to execute the first three tasks,
        // and then reused to execute the second three tasks. This is where the FixedThreadPool gets its name.
        // It will only ever create, at a maximum, the number of threads we specify,
        // regardless of the number of tasks submitted.
        var multiExecutor = Executors.newFixedThreadPool(3, new ColorThreadFactory());

        for (int i=0; i < count; i++){
            multiExecutor.execute(ExecutorsFive::countDown);
        }
        multiExecutor.shutdown();
    }

    /*
     if you want to run below main rename the "singleThreadMain" to main.
     this is example of how to execute multiple threads in sequence using
     serviceExecutor.shutdown() and executor.awaitTermination()
     */
    public static void singleThreadMain(String[] args) {
//        var blueExecutor = Executors.newSingleThreadExecutor(); // here we can not pass name directly.
        var blueExecutor = Executors.newSingleThreadExecutor(
                new ColorThreadFactory(ThreadColor.ANSI_BLUE));
        blueExecutor.execute(ExecutorsFive::countDown);

        // stop accepting new tasks
        blueExecutor.shutdown();
        boolean isDone = false;

        // Wait for existing tasks to complete. It works only after executor.shutdown() else no use.
        // It waits 500 milliseconds after termination of service. await termination returns boolean
        // It blocks calling other threads for 500 milliseconds, usually main thread.
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

    /*
     without executorService, if you want to run below main rename the "notMain" to main.
     This is example of how to execute multiple threads in sequence using thread.start() and thread.join()
     */
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
