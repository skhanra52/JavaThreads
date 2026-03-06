package com.skhanra52;

import java.util.concurrent.TimeUnit;

/*
 Manipulate and communicate with running threads.
 */
public class RunningThreadsTwo {
    public static void main(String[] args) {
        System.out.println("Main thread is running...");
        try {
            System.out.println("Main thread is paused for one second...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> {
            String tName = Thread.currentThread().getName();
            System.out.println(tName + " should take 10 dots to run.");
            for (int i = 0; i < 10; i++){
                System.out.print(" . ");
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(" A. State = " + Thread.currentThread().getState());
                } catch (InterruptedException e) {
                    System.out.println("\nWhoops!! " +tName + " interrupted");
                    System.out.println("A1. state = "+ Thread.currentThread().getState());
                    return;
                }
            }
            System.out.println("\n" +tName+ " is completed.");
        });
        System.out.println(thread.getName() + " is starting");
        thread.start();
        System.out.println("Main thread would continue here...");

        // Interrupt the main thread by calling the interrupt() from the Thread.
//        thread.interrupt();

        /*
         We can imagine, if we are trying to download a file and cancel for some reason, this interrupt would stop
         the download and close any resources as necessary. In the below code we are trying to demonstrate how to
         interrupt a thread.
         */
        long now = System.currentTimeMillis(); // current time
        while (thread.isAlive()){
            System.out.println("\n waiting for thread to complete.");
            try {
                Thread.sleep(1000);
                System.out.println("B. State = "+ thread.getState());

                if(System.currentTimeMillis() - now > 3000){
                    thread.interrupt(); // Interrupt the main thread by calling the interrupt() from the Thread.
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("C. State = "+ thread.getState());

        /*
         Threads States on Thread.State -----------------------------------------------
         NEW            -> A thread that has not yet started is in this state.
         RUNNABLE       -> A thread executing in the Java virtual machine(JVM) is in this state.
         BLOCKED        -> A thread that is blocked waiting for a monitor lock is in this state.
         WAITING        -> A thread that is waiting for another thread to perform a particular action is in this state.
         TIMED_WAITING  -> A thread that is waiting for another thread to perform an action for up to a specific waiting
                           time is in this state.
         TERMINATED     -> A thread that has exited is in this state.
         */
    }
}
