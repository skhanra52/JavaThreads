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
                System.out.print(".");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    System.out.println("\nWhoops!! " +tName + " interrupted");
                    return;
                }
            }
            System.out.println("\n" +tName+ " is completed.");
        });
        System.out.println(thread.getName() + " is starting");
        thread.start();
        System.out.println("Main thread would continue here...");
    }
}
