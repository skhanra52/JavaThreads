package com.skhanra52;

public class CustomThreadTwo extends Thread{

    @Override
    public void run() {
        for (int i=0; i<20; i++){
            if(i%2==0){
                System.out.println("Even number: "+(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
