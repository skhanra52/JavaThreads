package com.skhanra52.synchronizationFour;

/*
 This class would be accessed by multiple thread concurrently.
 */
public class BankAccount {

    private double balance;

    public BankAccount(double balance){
        this.balance = balance;
    }

    public double getBalance(){
        return balance;
    }

    // Kept entire method under synchronized block which will hold entire method to execute till the previous
    // thread finished executing.
//    public synchronized void deposit(double amount){
//        try{
//            System.out.println("Deposit - talking to the taller at the bank...");
//            Thread.sleep(7000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        double originalBalance = balance;
//        balance += amount;
//        System.out.printf("Starting balance: %.0f, DEPOSIT(%.0f)" +
//                ": NEW BALANCE =%.0f%n", originalBalance, amount, balance);
//        System.out.println();
//    }
//
//    public synchronized void withdraw(double amount){
//        try{
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        double originalBalance = balance;
//        if(balance >= amount){
//            balance -= amount;
//            System.out.printf("Starting balance: %.0f, WITHDRAWAL(%.0f)" +
//                    ": NEW BALANCE =%.0f%n", originalBalance, amount, balance);
//            System.out.println();
//        }else{
//            System.out.printf("Starting balance: %.0f, WITHDRAWAL(%.0f)" +
//                    ": INSUFFICIENT FUNDS", originalBalance, amount);
//            System.out.println();
//        }
//    }

    public void deposit(double amount){
        try{
            System.out.println("Deposit - talking to the taller at the bank...");
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized(this){
            double originalBalance = balance;
            balance += amount;
            System.out.printf("Starting balance: %.0f, DEPOSIT(%.0f)" +
                    ": NEW BALANCE =%.0f%n", originalBalance, amount, balance);
            System.out.println();
        }
    }

    public void withdraw(double amount){
        try{
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // kept critical section in the synchronized block rather than declaring entire method inside synchronized.
        synchronized(this){
            double originalBalance = balance;
            if(balance >= amount){
                balance -= amount;
                System.out.printf("Starting balance: %.0f, WITHDRAWAL(%.0f)" +
                        ": NEW BALANCE =%.0f%n", originalBalance, amount, balance);
                System.out.println();
            }else{
                System.out.printf("Starting balance: %.0f, WITHDRAWAL(%.0f)" +
                        ": INSUFFICIENT FUNDS", originalBalance, amount);
                System.out.println();
            }
        }
    }

    /*
     The Object instance monitor:-(Synchronized method, how it works)---------------------------------------

     -> Every object instance in Java has a built-in intrinsic lock, also known as a monitor lock.
     -> A thread acquires a lock by executing a synchronized method on the instance, or by using the instance as the
        parameter to a synchronized statement.
     -> A thread releases a lock when it exits from a synchronized block or method, even if it throws an exception.
        Only one thread at a time can acquire this lock, which prevents all other threads from accessing the instance's
        state, until the lock is released.
     -> All other threads, which want access to the instance's state through synchronized code, will block, and wait,
        until they can acquire a lock.

     The synchronized statement can be applied to a more granular code block:----------------------------
        -> The Synchronized statement, usually the better option in most circumstances since, it limits the scope of
           synchronization, to the critical section of code.
           In other word, It gives you more granular control over when you want other threads to block.
     */
}
