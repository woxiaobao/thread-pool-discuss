# thread-pool-discuss
Java线程设计原理

### 进程和线程

#### 进程

 所有运行中的任务通常对应一个进程,当一个程序进入内存运行时,即变成一个进程.进程是处于运行过程中的程序,并且具有一定独立的功能,进程是系统进行资源分配和调度的一个独立单位.

#### 进程的特性

- 1、动态性：进程是程序的一次执行，它有着创建、活动、暂停、终止等过程，具有一定的生命周期，是动态地产生、变化和消亡的。动态性是进程最基本的特征。

- 2、并发性：指多个进程实体，同存于内存中，能在一段时间内同时运行，并发性是进程的重要特征，同时也是操作系统的重要特征。引入进程的目的就是为了使程序能与其他进程的程序并发执行，以提高资源利用率。

- 3、独立性：指进程实体是一个能独立运行、独立获得资源和独立接受调度的基本单位。凡未建立PCB的程序都不能作为一个独立的单位参与运行。

- 4、异步性：由于进程的相互制约，使进程具有执行的间断性，即进程按各自独立的、 不可预知的速度向前推进。异步性会导致执行结果的不可再现性，为此，在操作系统中必须配置相应的进程同步机制。

- 5、结构性：每个进程都配置一个PCB对其进行描述。从结构上看，进程实体是由程序段、数据段和进程控制段三部分组成的。


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

```

#### 创建线程的三种方式对比

- 采用实现Runnable,Callable接口的方式创建多线程的优缺点:

1.线程类只是实现了Runnable接口或Callable接口,还可以继承其他类
2.多个线程可以共享同一个target对象,非常适合多个相同线程来处理同一份资源的情况,较好的体现了面向对象的思想
3.需要访问当前线程,则必须使用Thread.currentThread()方法

- 采用继承Thread类的方式创建多线程的优缺点:

1.因为该线程已经继承了Thread类,所以不能在继承其他父类
2.编写简单,如果需要访问当前线程,则无需使用Thread.currentThread()方法,直接使用this即可获得当前线程.


#### 为什么会执行run()方法
底层实现源码 https://download.java.net/openjdk/jdk8u40/ri/openjdk-8u40-src-b25-10_feb_2015.zip


#### 线程的生命周期

- 线程的生命周期中,需要经历新建(NEW),运行中(RUNNABLE),阻塞(BLOCKED),等待(WAITING),有限时间的等待(TIMED_WAITING),终止状态/死亡状态(TERMINATED)六种状态.



### 为什么使用线程池

    - 当我们在使用线程时，如果每次需要一个线程时都去创建一个线程，这样实现起来很简单，但是会有一个问题：当并发线程数过多时，并且每个线程都是执行一个时间很短的任务就结束时，这样创建和销毁线程的时间要比花在实际处理任务的时间要长的多，在一个JVM里创建太多的线程可能会导致由于系统过度消耗内存或切换过度导致系统资源不足而导致OOM问题；
    - 线程池为线程生命周期开销问题和资源不足问题提供了解决方案。通过对多个任务重用线程，线程创建的开销被分摊到了多个任务上。


### 使用线程池的优势

- 降低系统资源消耗，通过重用已存在的线程，降低线程创建和销毁造成的消耗。
- 提高系统响应速度，当有任务到达时，无需等待新线程的创建便能立即执行。
- 通过适当地调整线程池中的线程数目，可有效控制最大并发线程数，提高系统资源的使用率，同时避免过多资源竞争，避免堵塞。
- 更强大的功能，线程池提供了定时、定期以及可控线程数等功能的线程池，使用方便简单。

### 几种常用的线程池

```
//单线程池
ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

//固定线程池
ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

//缓存线程池
ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

//定时线程池
ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(5);
```

#### 结构图
   

### 参数解释

- corePoolSize ： 核心池大小，即线程的数量。在创建了线程池后，默认情况下，线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，除非调用了prestartAllCoreThreads()或者prestartCoreThread()方法，从这2个方法的名字就可以看出，是预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程。默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
- maximumPoolSize ： 线程池最大线程数，表示在线程池中最多能创建多少个线程；
- keepAliveTime ：表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，直到线程池中的线程数不大于corePoolSize：即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize；但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0；
- unit：参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性：

```
TimeUnit.DAYS; //天
TimeUnit.HOURS; //小时
TimeUnit.MINUTES; //分钟
TimeUnit.SECONDS; //秒
TimeUnit.MILLISECONDS; //毫秒
TimeUnit.MICROSECONDS; //微妙
TimeUnit.NANOSECONDS; //纳秒
```

- workQueue：线程池采用的缓冲队列，用来存储等待执行的任务，这个参数的选择会对线程池的运行过程产生重大影响，一般来说，有以下几种选择：ArrayBlockingQueue、LinkedBlockingQueue、SynchronousQueue。
- threadFactory：线程工厂，主要用来创建线程。默认使用Executors.defaultThreadFactory() 来创建线程。使用默认的ThreadFactory来创建线程时，会使新创建的线程具有相同的NORM_PRIORITY优先级并且是非守护线程，同时也设置了线程的名称。
- handler：当线程池中线程数量达到maximumPoolSize时，仍有任务需要创建线程来完成，则handler采取相应的策略，有以下几种策略：
adPoolExecutor.CallerRunsPolicy;//只用调用者所在线程来运行任务


结论：从ThreadPoolExecutor的源码我们可以看到，ThreadPoolExecutor类继承了抽象类AbstractExecutorService，而抽象类AbstractExecutorService实现了ExecutorService接口，ExecutorService接口又继承了Executor接口。
    Executor是最顶层接口，在它里面只声明了一个方法execute(Runnable)，返回值为void，参数为Runnable类型，用来执行传进去的任务的；


#### 线程池执行流程


#### 线程池的创建



```
public class PoolTest {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, 
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));
        for(int i=0;i<15;i++){
            MyTask myTask = new MyTask(i);
            executor.execute(myTask);
            System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                    executor.getQueue().size());
        }
        executor.shutdown();
    }
}

class MyTask implements Runnable {
    private int taskNum;
    public MyTask(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task "+taskNum);
        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task "+taskNum+"执行完毕");
    }
}
```


#### 线程池大小设置



- IO密集型
- CPU密集型

- 简单的分析来看，如果是CPU密集型的任务，我们应该设置数目较小的线程数，比如CPU数目加1。如果是IO密集型的任务，则应该设置可能多的线程数，由于IO操作不占用CPU，所以，不能让CPU闲下来。当然，如果线程数目太多，那么线程切换所带来的开销又会对系统的响应时间带来影响。


```
如果线程池中的线程在执行任务时，密集计算所占的时间比重为P(0<P<=1)，而系统一共有C个CPU，为了让CPU跑满而又不过载，线程池的大小经验公式 T = C / P。在此，T只是一个参考，考虑到P的估计并不是很准确，T的最佳估值可以上下浮动50%。

这个经验公式的原理很简单，T个线程，每个线程占用P的CPU时间，如果刚好占满C个CPU,那么必有 T * P = C。

下面验证一下边界条件的正确性：

假设C = 8，P = 1.0，线程池的任务完全是密集计算，那么T = 8。只要8个活动线程就能让8个CPU饱和，再多也没用了，因为CPU资源已经耗光了。

假设C = 8，P = 0.5，线程池的任务有一半是计算，有一半在等IO上，那么T = 16.考虑操作系统能灵活，合理调度sleeping/writing/running线程，那么大概16个“50%繁忙的线程”能让8个CPU忙个不停。启动更多的线程并不能提高吞吐量，反而因为增加上下文切换的开销而降低性能。

如果P < 0.2，这个公式就不适用了，T可以取一个固定值，比如 5*C。另外公式里的C不一定是CPU总数，可以是“分配给这项任务的CPU数目”，比如在8核机器上分出4个核来做一项任务，那么C=4
```

#### 结论：
最佳线程数目 = （线程等待时间与线程CPU时间之比 + 1）* CPU数目

线程等待时间所占比例越高，需要越多线程。线程CPU时间所占比例越高，需要越少线程。



#### 各种任务队列（BlockingQueue）的区别

ArrayBlockingQueue: 基于数组实现的有界的阻塞队列,该队列按照FIFO（先进先出）原则对队列中的元素进行排序。
LinkedBlockingQueue：基于链表实现的阻塞队列，该队列按照FIFO（先进先出）原则对队列中的元素进行排序。吞吐量高于ArrayBlockingQueue，Executors.newFixedThreadPool()使用了该队列。
SynchronousQueue：内部没有任何容量的阻塞队列。在它内部没有任何的缓存空间。对于SynchronousQueue中的数据元素只有当我们试着取走的时候才可能存在。吞吐量高于LinkedBlockingQueue，Executors.newCachedThreadPool()使用了该队列。
PriorityBlockingQueue：具有优先级的无限阻塞队列。

#### RejectedExecutionHandler饱和策略

- ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常(默认)。
- ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列里前面的任务，并执行当前任务。
- ThreadPoolExecutor.DiscardPolicy：不处理，直接丢弃任务。
- ThreadPoolExecutor.CallerRunsPolicy：只用调用者所在线程来运行任务。
- 实现RejectedExecutionHandler接口：自定义处理方式




### 线程池的监控

- getTaskCount：线程池已经执行的和未执行的任务总数；
- getCompletedTaskCount：线程池已完成的任务数量，该值小于等于taskCount；
- getLargestPoolSize：线程池曾经创建过的最大线程数量。通过这个数据可以知道线程池是否满过，也就是达到了maximumPoolSize；
- getPoolSize：线程池当前的线程数量；
- getActiveCount：当前线程池中正在执行任务的线程数量。
- 通过这些方法，可以对线程池进行监控，在ThreadPoolExecutor类中提供了几个空方法，如beforeExecute方法，afterExecute方法和terminated方法，可以扩展这些方法在执行前或执行后增加一些新的操作，例如统计线程池的执行任务的时间等，可以继承自ThreadPoolExecutor来进行扩展。


### 线程池关闭

- shutdown()和shutdownNow()是用来关闭线程池的，都是调用了interruptIdleWorkers()方法去遍历线程池中的工作线程，然后去打断它们。
- shutdown原理：将线程池状态设置成SHUTDOWN状态，然后中断所有没有正在执行任务的线程。
- shutdownNow原理：将线程池的状态设置成STOP状态，然后中断所有任务(包括正在执行的)的线程，并返回等待执行任务的列表。
    中断采用interrupt方法，所以无法响应中断的任务可能永远无法终止。但调用上述的两个关闭之一，isShutdown()方法返回值为true，当所有任务都已关闭，表示线程池关闭完成，则isTerminated()方法返回值为true。当需要立刻中断所有的线程，不一定需要执行完任务，可直接调用shutdownNow()方法。




#### 实例

#### 线程池在消息中心的应用

#### 分析问题

#### 多线程安全性考虑和防御措施










































