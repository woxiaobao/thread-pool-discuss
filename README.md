# thread-pool-discuss
Java线程设计原理

### 进程和线程

#### 进程

 所有运行中的任务通常对应一个进程,当一个程序进入内存运行时,即变成一个进程.进程是处于运行过程中的程序,并且具有一定独立的功能,进程是系统进行资源分配和调度的一个独立单位.

#### 进程的特性

- 独立性
- 动态性
- 并发性:多个进程可以在单个处理器上并发执行,多个进程之间不会相互影响.

#### 并发和并行的区别

 并行(parellel)指的是在同一时刻,有多条指令在多个处理器上同时被执行;
 并发指的是在同一时刻只能有一条指令执行,但多个进程指令被快速轮换执行,使得宏观上具有多个进程同时执行的结果.


#### 总结:

1.操作系统可以同时执行多个任务,每个任务就是进程;

2.进程可以同时执行多个任务,每个任务就是线程，线程是进程的最小单位.


```
```

#### 线程的创建和启动

Java使用Thread类代表线程,每个线程对象都必须是Thread类或其子类的实例.每个线程的作用是完成一定的任务,实际上是执行一段程序流.

继承Thread类创建线程类

1.定义Thread类的子类,并重写该类的run()方法,该run()方法的方法体就代表了线程需要完成的任务.因此把run()方法称为线程执行体
2.创建Thread子类的实例,即创建了线程对象
3.调用线程对象的start()方法来启动该线程.

```
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
                    System.out.println(Thread.currentThread().getName() + " : " + number--);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        }
    }
}

```

#### 实现Runnable接口创建线程类

1.定义Runnable接口的实现类,并重写该接口的run()方法,该run()方法的方法体同样是该线程的线程执行体.
2.创建Runnable实现类的实例,并以此实例作为Thread的target来创建Thread对象,该Thread对象才是真正的线程对象
3.调用线程对象的start()方法来启动该线程

```
public class TicketTest {

    public static void main(String[] args){
        TicketCare ticketCare = new TicketCare();

        for (int i = 0; i < 5; i++){
            new Thread(ticketCare).start();
        }
    }
}


class TicketCare implements Runnable {

    private volatile int number = 100;
    Object object = new Object();

    @Override
    public void run() {
        while(true){
            if (number > 0) {
                try {
                    System.out.println(Thread.currentThread().getName() + " : " + number--);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        }
    }
}

```

#### 创建线程的三种方式对比

- 采用实现Runnable,Callable接口的方式创建多线程的优缺点:

1.线程类只是实现了Runnable接口或Callable接口,还可以继承其他类
2.多个线程可以共享同一个target对象,非常适合多个相同线程来处理同一份资源的情况,较好的体现了面向对象的思想
3.需要访问当前线程,则必须使用Thread.currentThread()方法

- 采用继承Thread类的方式创建多线程的优缺点:

1.因为该线程已经继承了Thread类,所以不能在继承其他父类
2.编写简单,如果需要访问当前线程,则无需使用Thread.currentThread()方法,直接使用this即可获得当前线程.



#### 线程的生命周期

- 线程的生命周期中,需要经历新建(New),就绪(Runnable),运行(Running),堵塞(Blocked),死亡(Dead)5种状态.
















































