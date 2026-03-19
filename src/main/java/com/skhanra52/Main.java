package com.skhanra52;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        Thread currentThread = Thread.currentThread();
        System.out.println(currentThread.getClass().getName()); // java.util.Thread
        currentThread.setName("MainThread");
        currentThread.setPriority(Thread.MAX_PRIORITY);

        System.out.println(currentThread);
        printThreadState(currentThread);

        /*------------------------------------------------------------------------------------------------
         -> Running the customThread here, TimeUnit.SECONDS.sleep(1); another way to make the thread sleep.
         -> TimeUnit enum gives us options to provide time in millisecond, second, minute, and hour.
         -> We are starting the thread using start() method, it gives us concurrent execution of multiple thread.
         -> If we use "customThread.run()" then it will execute the threads synchronously, meaning, the current thread
            will execute first and followed by next thread.
         */

        Thread customThread = new CustomThreadOne();
        // Custom thread running here.
        customThread.start();

        // Main thread running here.
        for (int i=0; i<=3; i++){
            System.out.print(" 0 ");
            try {
                TimeUnit.SECONDS.sleep(1); // another way to make the thread sleep. TimeUnit gives us
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        /*----------------------------------------------------------------------------------------------------
         Second way to create a thread, which is by implementing the Runnable interface.
         The code in the lambda ultimately will execute inside the run() method in Runnable interface.

         -> Runnable is a functional interface.
         -> It's functional method, or it's single access method, is the run method.
         -> Anywhere you see a Runnable type, it's a target for a lambda expression.
         -> We can have any class implement the Runnable interface, and then pass it to the thread constructor
            to run asynchronously.
         */

        Runnable myRunnable = () -> {
            for (int i = 0; i <= 8; i++){
                System.out.print(" 2 ");
                try {
                    TimeUnit.MICROSECONDS.sleep(250);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        };
        Thread newThread = new Thread(myRunnable);
        newThread.start();

        /*
         The difference between running Thread without passing any parameter in the Thread constructor
         (ie, subclass extends Thread class ), and by creating Runnable interface.

         Extending the Thread class:--------------
             The new subclass (customThreadOne class) overrides the Thread's run method to provide concurrent
             thread's task. To use this thread, we can create a new instance of the subclass with no argument
             constructor, and execute the start method on that instance.

         Advantages:
            -> We have more control over the thread's behavior and properties.
            -> We can access the thread's methods and fields directly from the subclass.
            -> We can create new thread for each task.
         Disadvantages:
            -> Since, we can extend single class in java, so the subclass can't extend any other classes.
               Meaning, a subclass of thread is external to any domain specific hierarchy that we might also want for
               this subclass.
            -> Class is tightly coupled to the thread class, which makes it more difficult to maintain.

         Implementing the Runnable interface:---------------------------
            We can create threads by implementing the Runnable interface. This method allows any class, to implement
            Runnable, meaning any class at all can be used in a thread. This class is passed to the thread constructor
            that accept Runnable.
            We can also pass any anonymous class, lambda expression, or a method reference to this constructor to
            create any instance of the thread.
            We can again call start of the new instance to execute the code asynchronously.

         Advantages:
            -> We can extend any class, but still implement Runnable.
            -> Our class(if we create a class) is loosely coupled to the thread class, which makes it easier to maintain
            -> We can use anonymous classes, lambda expression, or method references, to very quickly describe thread
               behaviour.
         */

        // Instantiated the CustomThreadTwo here.
        Thread threadOne = new Thread(new CustomThreadTwo());
        Thread threadTwo = getThread();
        System.out.println();
        System.out.println("Thread one is starting...");
        threadOne.start();
        System.out.println("Thread two is starting...");
        threadTwo.start();
        try {
            threadTwo.join(); // main thread will wait till threadTwo complete
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Main thread waits till threadTwo gets completed.");
    }

    private static Thread getThread() {
        Runnable runnableForSecond= new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<10; i++){
                    if (i%2!= 0){
                        System.out.println("Odd number: "+i);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                            if(i >= 7){
                                Thread.currentThread().interrupt();
                            }
                        } catch (InterruptedException e) {
                            System.out.println("Thread two has been interrupted...");
                        }
                    }
                }
            }
        };

        // created second thread by passing runnable to Thread constructor.
        return new Thread(runnableForSecond);
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