package com.skhanra52;

import java.util.concurrent.TimeUnit;

public class CachedData {
//    private boolean flag = false;
// used volatile to indicate that the variable would be modified by multiple threads.
    private volatile boolean flag = false;

    public void toggleFlag(){
        flag = !flag;
    }

    public boolean isReady(){
        return flag;
    }

    public static void main(String[] args) {
        CachedData example = new CachedData();

        Thread writeThread = new Thread(() -> {
            try{
                TimeUnit.SECONDS.sleep(1); // placeholder for some task which may take sometime.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            example.toggleFlag(); // toggled the flag after sleeping one sec.
            System.out.println("A: Flag set to "+example.isReady());
        });

        Thread readerThread = new Thread(() -> {
            while (!example.isReady()){
                // Busy until flag become true.
            }
            System.out.println("B. Flag is "+example.isReady());
        });

        writeThread.start();
        readerThread.start();
        /*
         The second thread that is "readThread" will never start because the flag status will not reflect immediately
         after that toggle, it will go and get stored in thread's cache memory which is local to thread.
         So readThread local cache isn't getting updated with the modified flag value. will read the flag
         as if the state of it didn't change. This called memory inconsistency. As a result, the readThread will get
         stuck in its loop indefinitely waiting for the flag to become true.
         For More Detail check in MultipleThreadsThree class "Memory Consistency Error" section.

         Solution:
            To fix the situation we have added a modifier with the "flag", that is "volatile".

         -------------------------------------------------------------------------------
         Volatile Modifier:
            -> The "volatile" keyword used as a modifier to the class variables.
            -> It's an indicator that the variable value me be modified by multiple threads.
            -> This modifier ensures that the variable is always read from, and write to the main memory rather than
               from any thread specific caches.
            -> This provides memory consistency for this variable's value across the threads.

         Volatile Usages(When to use volatile):
            There are specific scenarios when you will want to use volatile.
            -> When a variable is used to track the state of a shared resources, such as a counter or a flag.
            -> When a variable used to communicate between threads.
         When not to use volatile:
            -> When a variable is only used by a single thread.
            -> When a variable used to store a large amount of data.
         */
    }
}
