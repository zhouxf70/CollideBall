package com.example.zhouxf.collideball.bean;

public class MyThread extends Thread {
    public boolean isRun=false;

    public MyThread() {
        super();
    }


    @Override
    public void run() {
        super.run();
        while (isRun){

        }
    }

}
