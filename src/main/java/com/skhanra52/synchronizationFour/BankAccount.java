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

    public synchronized void deposit(double amount){
        try{
            System.out.println("Deposit - talking to the taller at the bank...");
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double originalBalance = balance;
        balance += amount;
        System.out.printf("Starting balance: %.0f, DEPOSIT(%.0f)" +
                ": NEW BALANCE =%.0f%n", originalBalance, amount, balance);
        System.out.println();
    }

    public synchronized void withdraw(double amount){
        try{
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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
