package tij.concurrents.part5;

import tij.concurrents.part2.LiftOff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;


public class TestBlockingQueues {
    public static void main(String[] args) {
        test("LinkedBlockingQueue",new LinkedBlockingQueue<LiftOff>());
        test("ArrayBlockingQueue",new ArrayBlockingQueue<LiftOff>(3));
        test("SynchronousQueue",new SynchronousQueue<LiftOff>());
    }
    static void getkey(){
        try{
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void getkey(String message){
        System.out.println(message);
        getkey();
    }
    static void test(String msg, BlockingQueue<LiftOff> queue){
        System.out.println(msg);
        LiftOffRunnable runnable = new LiftOffRunnable(queue);
        Thread t = new Thread(runnable);
        t.start();
        for (int i = 0;i<2;i++){
            runnable.add(new LiftOff(1));//启动之后添加元素
        }
        getkey("Press 'Enter' ("+msg+")");//io阻塞并发生在子线程中，而是发生在main中
        t.interrupt();
        System.out.println("Finished "+msg+" test");
    }
}
class LiftOffRunnable implements Runnable{
    private BlockingQueue<LiftOff> rockets;

    public LiftOffRunnable(BlockingQueue<LiftOff> rockets) {
        this.rockets = rockets;
    }
    public void add(LiftOff lo){
        try{
            rockets.put(lo);
        } catch (InterruptedException e) {
            System.out.println("Interrupted during put()");
        }
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
//                Thread.sleep(1000);//此时另一个线程启动，不共享资源
                LiftOff rocket = rockets.take();//边添加（add）边取出（take）
                rocket.run();//取出来的LiftOff不被当做新的线程来执行，而是直接调用run方法
            }
        } catch (InterruptedException e) {
            System.out.println("Waking from take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}
