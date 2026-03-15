package com.skhanra52;

/*
 Thread accessing memory:
    -> Each Thread has its own stack for local variables and method calls.
    -> One thread does not have access to another thread's stack memory.
    -> Every concurrent thread additionally has access to the process memory, or heap.
       This is where objects and their data reside.
    -> This shared memory space allows all threads, to read and modify the same objects. When one thread changes an
       object in the heap, these changes are visible to other thread.
 */

import java.util.concurrent.TimeUnit;

public class MultipleThreadsThree {

    public static void main(String[] args) {

        /*
         Created three color threads, all tasked to run the countSown method, on a single instance of a stopWatch.
         Initially, when the loop variable "i" was the local variable, the thread has no conflict with each other.
         They all successfully count down from the initial starting point. When we used an instance field, however,
         for the loop variable, the concurrency of the application fell apart.
         */
        StopWatch stopWatch = new StopWatch(TimeUnit.SECONDS);
        // Thread constructor has been provided "Runnable -> run() -> stopWatch :: countDown" , and "threadName"
        Thread green = new Thread(stopWatch :: countDown, ThreadColor.ANSI_GREEN.name());
        Thread purple = new Thread(() -> stopWatch.countDown(7), ThreadColor.ANSI_PURPLE.name());
        Thread red = new Thread(() -> stopWatch.countDown(7), ThreadColor.ANSI_RED.name());
        green.start();
        purple.start();
        red.start();

        /*
         Time Slicing:
         ----------------------------------------------------------------------
         -> Time Slicing also known as time-sharing or time division.
         -> It's a technique used in multitasking operating systems, to allow multiple threads or processes to share a
            single CPU for execution.
         -> Available CPU time slice in to small-time intervals, which are divided out to the threads.
            Each thread gets that interval to attempt to make some progress on the task which it has to do.
            Whether completed the task or not in the slice time, doesn't matter to the thread management system.
            When the time is up it has to yield to another thread, and wait until the turn again.
         -> Unfortunately, when the threads are sharing the heap memory, things can change during that wait.
         -------------------------------------------------------------------------
         Java Memory Model(JMM):
            The Java Memory Model, is a specification that defines some rules and behaviors for threads to help control
            and manage shared access to data, and operation.
            -> Atomicity of Operation: Few operations are truly atomic.
            -> Synchronization is the process of controlling thread's access to share resources.
         */

    }

}


class StopWatch {
    private TimeUnit timeUnit;
//    int i; // instance of "i"

    public StopWatch(TimeUnit timeUnit){
        this.timeUnit = timeUnit;
    }

    public void countDown(){
        countDown(5);
    }

    public void countDown(int unitCount){
        String threadName = Thread.currentThread().getName();

        ThreadColor threadColor = ThreadColor.ANSI_RESET;
        try {
            /*
             String threadName = Thread.currentThread().getName();
              converting threadName to Enum,
              threadName = "ANSI_GREEN" becomes,
              "ThreadColor.valueOf("ANSI_GREEN")" which  "return ThreadColor.ANSI_GREEN"
              So now , threadColor = ANSI_GREEN
             */
            threadColor = ThreadColor.valueOf(threadName);
            System.out.println("Thread color: "+threadColor + threadColor.getColor());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // user may pass a bad color name, will just ignore this error.
        }

        String color = threadColor.getColor();
        for (int i=unitCount; i>0; i--){
            try{
                timeUnit.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s %s Thread : i= %d%n",color, threadName, i);
        }
    }
}
