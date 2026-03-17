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
         Initially, when the loop variable "i" was the local variable, the threads had no conflict with each other.
         They all successfully count down from the initial starting point. That is because each local i variable will
         be there in Thread's stack memory, and Threads does not share their stack memories. This program works because
         stopWatch object is effectively stateless, meaning, No mutable object present.
         Only shared variable is timeUnit which is immutable.

         When we used an instance field of StopWatch class(All the threads using single instance of the StopWatch.),
         however, for the loop variable, the concurrency of the application fell apart. Because now the variable 'i' is
         a shared variable and can be modified by all the thread concurrently and create a Thread Interference.
         This problem called --------"Race condition"----------:
            -> When multiple threads access shared data and the final result depends on execution timing.
         We can solve this by using
         1. "Synchronization": public synchronize void countDown(TimeUnit timeUnit){}
            Now, only one thread can execute this method at a time.  However, threads will lose the parallelism.
            Code looks like below:

            public synchronized void countDown(int unitCount){
                this.i = unitCount;
                for(; i > 0; i--){
                    timeUnit.sleep(1);
                    System.out.println(i);
                }
            }

            2. "Synchronize" critical Section:
               -> Instead of locking the entire method lock the critical section.
                    public void countDown(int unitCount){
                        synchronized(this){
                            i = unitCount;
                            for(; i > 0; i--){
                                System.out.println(i);
                            }
                        }
                    }

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
         ----------------------------------------------------------------------------
         Interleaving:
            -> When multiple threads run concurrently, their instruction can overlap or interleave in the time.
            -> The execution of multiple thread happens in an arbitrary order.
            -> The order in which thread executes can't be guaranteed.

         Atomic Action:
            -> In programming, atomic action is the one, that effectively happens all at once.
            -> An atomic action either happen completely, or it does not happen.
            -> Side effect of atomic action are never visible until action completion.
         Thread safe:
            -> An object or a block of code is thread safe, if it isn't compromised by the execution of concurrent thread.
            -> This means, the correctness and consistency of the program's output or its visible state, is unaffected
               by other threads.
            -> Atomic operations and immutable objects are example of thread safe code.
         Memory consistency Error:
            -> The operating system may read from heap variable and make a copy of the value in each thread's own
               storage cache.
            -> Each thread has its own small and fast memory storage, that holds its own copy of a shared resource's value.
            -> One thread can modify the shared variable, but this change might not be immediately reflected or visible.
            -> Instead, it's first updated in the thread's local cache.
            -> The Operating system may not flash the first thread's changes to the heap, until the thread has finished
               executing.
               To Demonstrate run the "CachedDataFour.java" file.

         */

    }

}


/*
  1. Solution 1: It solved race condition by declaring 'i' inside the method due to which each thread will get
     their individual 'i' instance in the stack.
 */
//class StopWatch {
//    private TimeUnit timeUnit;
////    int i; // instance of 'i' --> this creates "Race Condition" because in this case all threads shared single
//             // instance of 'i'
//
//    public StopWatch(TimeUnit timeUnit){
//        this.timeUnit = timeUnit;
//    }
//
//    public void countDown(){
//        countDown(5);
//    }
//
//    public void countDown(int unitCount){
//        String threadName = Thread.currentThread().getName();
//
//        ThreadColor threadColor = ThreadColor.ANSI_RESET;
//        try {
//            /*
//             String threadName = Thread.currentThread().getName();
//              converting threadName to Enum,
//              threadName = "ANSI_GREEN" becomes,
//              "ThreadColor.valueOf("ANSI_GREEN")" which  "return ThreadColor.ANSI_GREEN"
//              So now , threadColor = ANSI_GREEN
//             */
//            threadColor = ThreadColor.valueOf(threadName);
//            System.out.println("Thread color: "+threadColor + threadColor.getColor());
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            // user may pass a bad color name, will just ignore this error.
//        }
//
//        String color = threadColor.getColor();
//        for (int i=unitCount; i>0; i--){
//            try{
//                timeUnit.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.printf("%s %s Thread : i= %d%n",color, threadName, i);
//        }
//    }
//}

/*
 Solution 2: Using Synchronization for the method, however,threads parallelism will be lost here. Sequentially all the
 thread will be executed.
 */
//class StopWatch {
//    private TimeUnit timeUnit;
//    int i;
//    public StopWatch(TimeUnit timeUnit){
//        this.timeUnit = timeUnit;
//    }
//
//    public void countDown(){
//        countDown(5);
//    }
//
//    public synchronized void countDown(int unitCount){
//        String threadName = Thread.currentThread().getName();
//
//        ThreadColor threadColor = ThreadColor.ANSI_RESET;
//        try {
//            /*
//             String threadName = Thread.currentThread().getName();
//              converting threadName to Enum,
//              threadName = "ANSI_GREEN" becomes,
//              "ThreadColor.valueOf("ANSI_GREEN")" which  "return ThreadColor.ANSI_GREEN"
//              So now , threadColor = ANSI_GREEN
//             */
//            threadColor = ThreadColor.valueOf(threadName);
//            System.out.println("Thread color: "+threadColor + threadColor.getColor());
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            // user may pass a bad color name, will just ignore this error.
//        }
//
//        String color = threadColor.getColor();
//        i=unitCount;
//        for (; i>0; i--){
//            try{
//                timeUnit.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.printf("%s %s Thread : i= %d%n",color, threadName, i);
//        }
//    }
//}

/*
 Solution 3: Using Synchronization for the critical logic.
 */

class StopWatch {
    TimeUnit timeUnit;

    StopWatch(TimeUnit timeUnit){
        this.timeUnit = timeUnit;
    }

    void countDown(){
        this.countDown(5);
    }

    void countDown(int unitCount ){
        String threadName = Thread.currentThread().getName();
        ThreadColor threadColor = ThreadColor.ANSI_RESET;
        try {
            threadColor = ThreadColor.valueOf(threadName);
            System.out.println("Thread color: "+threadColor + threadColor.getColor());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        String color = threadColor.getColor();
        synchronized(this){
            int i = unitCount;
            for(; i > 0; i--){
                try{
                    timeUnit.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.printf("%s %s Thread : i= %d%n",color, threadName, i);
            }
        }
    }
}