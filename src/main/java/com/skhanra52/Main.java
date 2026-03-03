package com.skhanra52;

public class Main {
    public static void main(String[] args) {

        Thread currentThread = Thread.currentThread();
        System.out.println(currentThread.getClass().getName()); // java.util.Thread
        currentThread.setName("MainThread");
        currentThread.setPriority(Thread.MAX_PRIORITY);

        System.out.println(currentThread);
        printThreadState(currentThread);



    }

    public static void printThreadState(Thread thread){
        System.out.println("---------------------------");
        System.out.println("Thread ID: "+thread.getId());
        System.out.println("Thread Name: "+thread.getName());
        System.out.println("Thread Priority: "+thread.getPriority());
        System.out.println("Thread Group: "+thread.getThreadGroup());
        System.out.println("Thread is Alive: "+thread.isAlive());
        System.out.println("-----------------------------");

    }
}