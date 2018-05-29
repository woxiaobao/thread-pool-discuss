package com.lv.study.thread.ticket.pool.lock;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lvbaolin on 2018/5/17.
 *
 * 偏向锁测试
 *
 */
public class BiasedLockTest {

    public static List<Integer> numberList = new Vector<Integer>();
    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        int count = 0;
        int startnum = 0;
        while(count<10000000){
            numberList.add(startnum);
            startnum+=2;
            count++;
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时："+ (end-begin) + " ms");
    }



}
