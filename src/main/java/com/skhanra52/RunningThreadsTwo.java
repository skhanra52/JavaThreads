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
//-----------Checking Thread states-------------------------------------------------------------------------------------
        // The code inside () -> {} is a lambda expression implementing Runnable.
        Thread thread = new Thread(() -> {
            String tName = Thread.currentThread().getName();
            System.out.println(tName + " should take 10 dots to run."); // Thread-0 gets the CPU to execute.
            for (int i = 0; i < 10; i++){
                System.out.print(" . ");
                try {
                    TimeUnit.SECONDS.sleep(1); // free the CPU for main thread and goes in TIMED_WAITING
                    System.out.println("A. State of " +tName + " = " + Thread.currentThread().getState());
                } catch (InterruptedException e) {
                    System.out.println("\nWhoops!! " +tName + " interrupted");
                    System.out.println("A1. state = "+ Thread.currentThread().getState());
                    return;
                }
            }
            System.out.println("\n" +tName+ " is completed.");
        });
        System.out.println(thread.getName() + " is starting");
//        // Ready to run, waiting for CPU, Main thread continues to run as it was already running in the CPU.
        thread.start();
        System.out.println("Main thread would continue here...");

        /*
         We can imagine, if we are trying to download a file and cancel for some reason, this interrupt would stop
         the download and close any resources as necessary. In the below code we are trying to demonstrate how to
         interrupt a thread.
         */
        long now = System.currentTimeMillis(); // current time
        while (thread.isAlive()){
            System.out.println("\nWaiting for thread to complete.");
            try {
                // Main thread free the CPU and control goes to thread-0 created above. and goes in TIMED_WAITING.
                Thread.sleep(1000);
                // this is Thread-0 current state , should be RUNNABLE.
                System.out.println("B. State of "+ thread.getName()+" = " + thread.getState());

                if(System.currentTimeMillis() - now > 3000){
                    thread.interrupt(); // Interrupt the main thread by calling the interrupt() from the Thread.
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("C. State of "+ thread.getName()+" = " + thread.getState());

        /*
         Threads States on Thread.State -----------------------------------------------
         NEW            -> A thread that has not yet started is in this state.
         RUNNABLE       -> A thread executing in the Java virtual machine(JVM) is in this state. "thread executing".
         BLOCKED        -> A thread that is blocked waiting for a monitor lock is in this state. "waiting for lock".
         WAITING        -> A thread that is waiting for another thread to perform a particular action is in this state.
                           "waiting indefinitely".
         TIMED_WAITING  -> A thread that is waiting for another thread to perform an action for up to a specific waiting
                           time is in this state. "waiting for a specific time".
         TERMINATED     -> A thread that has exited is in this state.
         */


//------------------Example of Join()-----------------------------------------------------------------------------------
        /*
         Imagine that our current thread is downloading and installing a package. When it completes we want to start a
         separate installation thread but only if the download already completed.
         */

//        Thread thread = new Thread(() -> {
//            String tName = Thread.currentThread().getName();
//            System.out.println(tName + " should take 10 dots to run."); // Thread-0 gets the CPU to execute.
//            for (int i = 0; i < 10; i++){
//                System.out.print(" . ");
//                try {
//                    TimeUnit.SECONDS.sleep(1); // free the CPU for main thread and goes in TIMED_WAITING
//                } catch (InterruptedException e) {
//                    System.out.println("\nWhoops!! " +tName + " interrupted");
//                    Thread.currentThread().interrupt();
//                    return;
//                }
//            }
//            System.out.println("\n" +tName+ " is completed.");
//        });
//
//        Thread installThread = new Thread(() -> {
//            String tName = Thread.currentThread().getName();
//            System.out.println(tName + "installThread starting"); // Thread-1 gets the CPU to execute.
//           try{
//               for( int i= 0; i < 3; i++){
//                   Thread.sleep(1000);
//                   System.out.println("Installation step " + (i+1) +" is completed...");
//               }
//           } catch (InterruptedException e) {
//              e.printStackTrace();
//           }
//            System.out.println("\n" +tName+ " is completed.");
//        }, "InstallThread");
//
//        Thread threadMonitor = new Thread(() -> {
//            long now1 = System.currentTimeMillis(); // current time
//            while (thread.isAlive()){
//                try {
//                    // Main thread free the CPU and control goes to thread-0 created above. and goes in TIMED_WAITING.
//                    Thread.sleep(1000);
//                    if(System.currentTimeMillis() - now1 > 3000){
//                        thread.interrupt(); // Interrupt the main thread by calling the interrupt() from the Thread.
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        System.out.println(thread.getName() + " is starting");
//        // Ready to run, waiting for CPU, Main thread continues to run as it was already running in the CPU.
//        thread.start();
//        threadMonitor.start();
//       /*
//        We don't want to run this thread till the previous thread completed. This join method will wait for the main
//        thread to complete, and then run the dependent task.
//        -> join() tells the current thread to wait until another thread finishes.
//        -> MAIN THREAD WAITS HERE, subsequence lines in the main thread after thread.join() does not execute till
//           threadMonitor executes.
//        -> So the Main thread state become WAITING.
//
//        */
//        try {
//            thread.join(); // MAIN THREAD WAITS HERE.
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        if(!thread.isInterrupted()){
//            installThread.start();
//        }else {
//            System.out.println("Previous thread was interrupted "+
//                    installThread.getName() + " can't run");
//        }
        //--------------------------------------------------------------------------------------------------------------
    }
}
