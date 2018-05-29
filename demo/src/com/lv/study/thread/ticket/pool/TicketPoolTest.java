package com.lv.study.thread.ticket.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lvbaolin on 2018/5/7.
 *
 * 使用线程池买票
 *
 */
public class TicketPoolTest {


    public volatile static int number = 100;

    public static void main(String[] arge){
       ExecutorService executorService = Executors.newFixedThreadPool(5);
       for (int i = 0; i<5;i++) {
           executorService.execute(() -> task());
       }
        executorService.shutdown();
    }

    public static void task(){
        for(;;){
            if (number > 0) {
                try {
                    System.out.println(Thread.currentThread().getName() + " : " + number--);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                }
            }else{
                break;
            }
        }
    }



}
