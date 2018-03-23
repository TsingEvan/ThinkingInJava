package tij.concurrents.part2;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Daemons {
    public static void main(String[] args) throws InterruptedException {
        isDaemonThread();
    }

    static void isDaemonThread() throws InterruptedException {
        Thread d = new Thread(new Daemon());
        d.setDaemon(true);
        d.start();
//        Thread.yield();
        System.out.println("d.isDaemon() = "+d.isDaemon()+", ");
        TimeUnit.SECONDS.sleep(1);
    }
}

class Daemon implements Runnable{
    private Thread[] t = new Thread[10];
    @Override
    public void run() {
        for (int i = 0;i<t.length;i++){
            t[i] = new Thread(new DaemonSpawn());
            t[i].start();
            System.out.println("DaemonSpawn "+i+" started,");
        }
        for (int i = 0;i<t.length;i++){
            System.out.println("t["+i+"].isDaemon() "+t[i].isDaemon()+",");
        }
        while(true){//这样的话，该线程就不会死掉！
                    // 它能确保线程是因为main执行完毕而被丢弃的，而不是自身执行完毕被丢弃的
            //这么小一点技巧，真是处处有惊喜啊~
            Thread.yield();
        }

    }
}

class DaemonSpawn implements Runnable{

    @Override
    public void run() {
        while (true)
            Thread.yield();
    }
}