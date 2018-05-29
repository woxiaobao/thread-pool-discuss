package com.lv.study.thread.ticket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lvbaolin on 2018/5/3.
 *
 * 实现Runnable接口买票实例
 *
 */
public class TicketTest {
    public static void main(String[] args){
        TicketCare ticketCare = new TicketCare();
        for (int i = 0; i < 5; i++){
            new Thread(ticketCare).start();
        }
    }
}

class TicketCare implements Runnable {
    private AtomicInteger number = new AtomicInteger(100);

    @Override
    public void run() {
        while(true){
            if (number.get() > 0) {
                try {
                    String name = Thread.currentThread().getName();
                    System.out.println(name + " : " + number.getAndDecrement());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        }
    }
}
