package com.lv.study;

/**
 * Created by lvbaolin on 2018/3/8.
 */
public class Demo {

    public static Boolean foo(char c){
        System.out.print(c);
        return true;
    }

    public static void main(String[] args){
        int i = 0;
        for (foo('A'); foo('B')&&( i < 2 );foo('C')){
            i++;
            foo('D');
        }
    }
}
