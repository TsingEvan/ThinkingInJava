package tij.concurrents.part2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LiftOff implements Runnable {

    protected int countDown = 10;
    private static int taskCount = 0;
    private final int id = taskCount++;

    public LiftOff() {}

    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status(){
        return "#"+id+"("+(countDown>0?countDown:"LiftOff!")+") ";
    }

    @Override
    public void run() {
        while(countDown-->0){
            System.out.println(status());
            try {
//                Thread.sleep(100);//之后又重新抢占，而不是说自己就退出竞争了
                TimeUnit.MILLISECONDS.sleep(1000);//这样之后，它的输出是很值得玩味的
                            //2-9,0-9,1-9；2-8,0-8，1-8；...
                            //仔细想想为什么会出现上面这样两个方向上的固定顺序
                            //首先，9-8-7...是为什么？因为刚刚输出了9的线程处于休眠状态，而输出操作所经历的时间又极短，因而它无法参与下次抢占，也就是说，在最后抢占到CPU时间的线程输出时，其他线程其实还处在休眠状态！
                            //那么，2-0-1,2-0-1,2,-0-1...的循环节又是怎么回事呢？2（和0,1）抢占-2休眠-0（和1）抢占-0休眠-1（只剩1未进入休眠）抢占-1休眠-稍等，然后2是最先醒来的！
//                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class BasicThreads{
        public static void main(String[] args) {
            executorManage();
        }
    }

    static void normal(){
        Thread thread ;
        for (int i = 0; i<5; i++){
            thread = new Thread(new LiftOff());
            thread.start();
        }
        new LiftOff().run();
    }

    static void executorManage(){
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i=0;i<3;i++){
            exec.execute(new LiftOff());
        }
        exec.shutdown();
    }
}

