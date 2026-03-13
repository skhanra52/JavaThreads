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

        StopWatch stopWatch = new StopWatch(TimeUnit.SECONDS);
        // Thread constructor has been provided "Runnable -> run() -> stopWatch :: countDown" , and "threadName"
        Thread green = new Thread(stopWatch :: countDown, ThreadColor.ANSI_GREEN.name());
        Thread purple = new Thread(() -> stopWatch.countDown(7), ThreadColor.ANSI_PURPLE.name());
        Thread red = new Thread(() -> stopWatch.countDown(7), ThreadColor.ANSI_RED.name());
        green.start();
        purple.start();
        red.start();

    }

}


class StopWatch {
    private TimeUnit timeUnit;

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
            System.out.printf("%s%s Thread : i= %d%n",color, threadName, i);
        }
    }
}
