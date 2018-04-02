package tij.concurrents.part4;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//先声明一点概念：此处所说的中断，指的是使线程中断阻塞状态，返回到良好的运行状态
public class Interrupting {
    private static ExecutorService exec = Executors.newCachedThreadPool();
    static void test(Runnable r) throws InterruptedException {
        Future<?> f = exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Interrupting "+r.getClass().getSimpleName());
        f.cancel(true);
        System.out.println("Interrupted sent to "+r.getClass().getSimpleName());
    }

    public static void main(String[] args) throws InterruptedException {
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Aborting with System.exit(0)");
        System.exit(0);
    }
}
class SleepBlocked implements Runnable{
    @Override
    public void run() {
        try{
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
        System.err.println("Exiting SleepingBlocked.run()");//为什么这句话还会被打印出来
    }
}
class IOBlocked implements Runnable{
    private InputStream in ;
    public IOBlocked(InputStream is){
        in = is;
    }
    @Override
    public void run() {
        try{
            System.out.println("Waiting for read()");
            in.read();
        } catch (IOException e) {
//            System.out.println(">>>"+e);
            if(Thread.currentThread().isInterrupted()){
                System.out.println("Interrupted form blocked I/O");
            }else{
//                throw new RuntimeException(e);
                System.out.println(e);
            }
        }
        System.out.println("Exiting IOBlocked.run()");//为什么这里不被打印呢
    }
}
class SynchronizedBlocked implements Runnable{
    public synchronized void f(){
        while(true){
            Thread.yield();
        }
    }
    public SynchronizedBlocked(){
        new Thread(){
            public void run(){
                f();
            }
        }.start();
    }
    @Override
    public void run() {
        System.out.println("Trying to call f()");
        f();
        System.out.println("Exiting SynchronizedBlocked.run()");
    }
}