package com.lv.study.thread.ticket;

/**
 * Created by lvbaolin on 2018/5/7.
 *
 * 继承Thread 买票实例
 *
 */
public class ThreadTest {
    public static void main(String[] arge){
        ThreadTask ticketCare = new ThreadTask();
        for (int i = 0; i < 5; i++){
            new Thread(ticketCare).start();
        }
    }
}

class ThreadTask extends Thread{
    private volatile int number = 100;

    @Override
    public void run() {
        for(;;){
            if (number > 0) {
                try {
                    String name = Thread.currentThread().getName();
                    System.out.println(name + " : " + number--);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        }
    }
}
