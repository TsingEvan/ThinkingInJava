package tij.concurrents.part4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Interrupting2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Blocked2());
        t.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Issuing t.interrupt()");
        t.interrupt();
        /**
         ***interrupt并不能关闭一个运行良好的线程,shutdownNow也不行，这和我之前的理解完全相悖
         */


//        ExecutorService exec = Executors.newCachedThreadPool();
//        exec.execute(new Blocked2());
////        TimeUnit.MILLISECONDS.sleep(1000);
//        System.out.println("Issuing exec.shutdownNow()");
//        Thread.yield();
//        exec.shutdownNow();
    }
}
class BlockedMutex{
    private Lock lock = new ReentrantLock();
    public BlockedMutex(){
        lock.lock();
    }
    public void f(){
        try{
//            lock.unlock();
            lock.lockInterruptibly();
            System.out.println("lock acquired in f()");
        } catch (InterruptedException e) {
            System.out.println("Interrupted from lock acquisition in f()");
        }
    }
}
class Blocked2 implements Runnable{
    BlockedMutex blocked = new BlockedMutex();//在这里new的话，锁会被main拿到
    @Override
    public void run() {
//        BlockedMutex blocked = new BlockedMutex();
        System.out.println("Waiting for f() in BlockedMutex");
        blocked.f();
        System.out.println("Broken out of blocked call");
//        while(true){
//            System.out.print(0);
//            Thread.yield();
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
