package tij.concurrents.part3;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocalVariableHolder {
//    private static ThreadLocal<Integer>  value = new ThreadLocal<Integer>(){
//        Random rand = new Random(47);
//        protected synchronized Integer initialValue(){
//            return rand.nextInt(10);
//        }
//    };
//    public static void increment(){
//        value.set(value.get()+1);
//    }
//    public static int get(){
//        return value.get();
//    }
    private  int value = new Random(47).nextInt(10);
    public  void increment(){
        value++;
    }
    public  int get(){
        return value;
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0;i<5;i++){
            exec.execute(new Accessor(i));//因为是同一个Accessor对象，所以会是同一个Holder对象、同一个value值
        }
        TimeUnit.MILLISECONDS.sleep(2);
        exec.shutdownNow();
    }
}
class Accessor implements Runnable{
    private final int id ;
    public Accessor(int idn){
        id = idn;
    }
    ThreadLocalVariableHolder holder = new ThreadLocalVariableHolder();
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            holder.increment();
            System.out.println(this);
            Thread.yield();
        }
    }
    public String toString(){
        return "#"+id+": "+holder.get();
    }
}
