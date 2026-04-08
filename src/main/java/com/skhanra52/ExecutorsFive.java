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

public class ExecutorsFive {

    public static void main(String[] args) {

    }
}
