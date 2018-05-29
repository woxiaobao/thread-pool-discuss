package com.lv.study;

/**
 * Created by lvbaolin on 2018/3/8.
 */
public class Test {

    public static void chengeValue(String str){
        str = str + "5";
    }

    public static void main(String[] args){
        String str = "1234";
        chengeValue(str);
        System.out.println(str);
    }
}
