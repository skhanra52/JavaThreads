package com.skhanra52.synchronizationFour;

/*
 Synchronized Methods:------------------
    -> Different invocations of synchronized methods, on same object are guaranteed not to interleave.
    -> When one thread is execution a synchronized method for an object, all other thread that invoke synchronized
       methods for the same object , block, and suspend their execution, until the first thread is done with the object.
    -> When synchronized method exists, it ensures that the state of object visible to all threads.

 If we're making threads sit around and wait, aren't we kind of defeating the purpose of a multi-threading environment?

    And the answer to that is yes, for that bit of code that is updating the balance.
    To be clear, if a class has three synchronized methods, then only one of these methods can ever run at a time,
    and only by one thread. This is why it's really important to ensure that the code, in your synchronized methods
    is limited, to just code that has access to the shared object.

 Critical section:------------------------
    This is called the critical section. The critical section is the code that's referencing a shared resource like
    a variable. Only one thread at a time should be able to execute a critical section. When all critical sections
    are synchronized, the class is thread safe.

    Let's now talk about what a critical section is, looking at this code, in the bank account class.
 Example of thread safe.
 */
public class Synchronization {
    public static void main(String[] args) {
        BankAccount companyAccount = new BankAccount(10000);

        Thread thread1 = new Thread(() -> companyAccount.withdraw(25000), "Thread-1");
        Thread thread2 = new Thread(() -> companyAccount.deposit(50000), "Thread-2");
        Thread thread3 = new Thread(() -> companyAccount.withdraw(25000), "Thread-3");
        Thread thread4 = new Thread(() -> companyAccount.withdraw(50000), "Thread-4");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try{
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nCompany's final balance: "+ companyAccount.getBalance());
     }
}
