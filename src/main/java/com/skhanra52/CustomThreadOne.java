package com.skhanra52;

public class CustomThreadOne extends Thread {

    /*
     There run() method does not take any parameter and does not return anything. It just runs the target. Since, we are
     creating custom Thread, we have to provide the custom implementation for this run().
     */
    @Override
    public void run() {
//        super.run();
        for(int i=0; i<=5; i++){
            System.out.println("1");
            try{
                Thread.sleep(1000); // adding 1-sec delay between each count.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
