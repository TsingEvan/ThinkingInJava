package tij.concurrents.part2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class CaptureUncaughtException {
    public static void main(String[] args) {
////        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
//        ExecutorService exec = Executors.newCachedThreadPool(new HandleThreadFactory());
////        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
//        exec.execute(new ExceptionThread2());
//        //下面这个很神奇，有时候是1个线程能捕获异常，有时候是2个线程不能捕获异常，明显，不能捕获是因为main没有抢占到CPU时间，因而没有设置异常处理器
//                    //困惑的是2个线程是哪来的？发生异常之后就会再产生一个线程吗？
//        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
//        exec.shutdown();

        Thread t = new Thread(new ExceptionThread2());
        t.start();
        Thread.yield();
        //直接创建线程的话，当然也可能捕获不到异常，但是它不会尝试创建一个新的线程
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        /**
         * 神奇的是，注掉shutdown()之后，输出变为如下：
         * created Thread-0
         * created Thread-1 //创建两个线程
         * caught RuntimeException from Thread-0
         * 并且，程序不会退出(exit with code 0)
         *
         * 大概情况是这样的：
         * 不抛异常的，也没有shutdown的话，
         *      创建1个线程，程序不退出
         * 不抛异常，有shutdown
         *      创建1个线程，退出
         * 抛异常，有shutdown
         *      创建1个线程，退出
         * 抛异常，无shutdown
         *      创建2个线程，不退出
         */
    }
}
class ExceptionThread2 implements  Runnable{
    @Override
    public void run() {
//        Thread t = Thread.currentThread();
//        System.out.println(t.getName());
//        System.out.println("eh = "+t.getUncaughtExceptionHandler());
        throw new RuntimeException();
    }
}
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("caught "+e.getClass().getSimpleName()+" from "+t.getName());
    }
}
class HandleThreadFactory implements ThreadFactory{

    @Override
    public Thread newThread(Runnable r) {
//        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
//        System.out.println(this+" creating new Thread");//创建线程的是同一个工厂
        Thread t = new Thread(r);
//        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        System.out.println("created "+t.getName());
//        t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
//        System.out.println("eh = "+t.getUncaughtExceptionHandler());//每个线程都有一个新的异常处理器
        return t;
    }
}

//问题是：如果使用执行器，而又没有成功设置异常处理器的话，会创建2个线程，//这是什么原因？
