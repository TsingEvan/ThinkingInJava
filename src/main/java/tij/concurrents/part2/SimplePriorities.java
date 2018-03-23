package tij.concurrents.part2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplePriorities implements Runnable {
    private int countDown = 5;
    private volatile double d;
    private int priority;

    public SimplePriorities(int priority) {
        this.priority = priority;
//        Thread.currentThread().setPriority(priority);
        System.out.println("<<<"+Thread.currentThread());//拿到的是main线程
    }

    public String toString(){
        return Thread.currentThread()+": "+countDown;
    }

    @Override
    public void run() {
        System.out.println(">>>"+Thread.currentThread());
        Thread.currentThread().setPriority(priority);
        while(true){
            for (int i = 1;i<100000;i++){//为了充分利用起每一个线程，尽可能在任何线程打印结果之前多进行调度
                d+=(Math.PI+Math.E)/(double)i;
                if (i%1000==0){
                    Thread.yield();
                }
            }//计算10w次之后打印this，这期间在每1000次时必重新抢占
            System.out.println(this);
            if (--countDown==0) return ;
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0;i<5;i++){
            exec.execute(new SimplePriorities(Thread.MIN_PRIORITY));
//            exec.execute(new SimplePriorities(Thread.MAX_PRIORITY));
        }
        exec.execute(new SimplePriorities(Thread.MAX_PRIORITY));
        exec.shutdown();
    }
}
