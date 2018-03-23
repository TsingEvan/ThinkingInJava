package tij.concurrents.part2;

import java.util.concurrent.*;

public class SimpleDaemons implements Runnable {

    @Override
    public void run() {
//        System.err.println(Thread.currentThread());
        //Thread.currentThread().setDaemon(true);//java.lang.IllegalThreadStateException
                                        //我猜测错误原因可能是daemon的设置必须要在线程的初始化方法（start()）中进行，而不能在任务里进行设置
                                                    //必须在线程启动之前配置
        try{
            while(true){
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(Thread.currentThread()+" "+this);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        threadFactory();
    }

    static void normal() throws InterruptedException {
        //        System.out.println(Thread.currentThread());
//        Thread daemon = new Thread(new SimpleDaemons());
//        daemon.setDaemon(true);
//        Executors.newCachedThreadPool().execute(daemon);//因为Thread也实现了Runnable，所以也可以以Thread为参数
        //**但是这里设置的setDaemon(true)无效
                                                        /*
                                                            下面是Thread类的run方法:
                                                            @Override
                                                            public void run() {
                                                                if (target != null) {
                                                                    target.run();
                                                                }
                                                            }
                                                         */
        for (int i = 0;i<10;i++){
            Thread daemon = new Thread(new SimpleDaemons());
            daemon.setDaemon(true);
            daemon.start();
        }
        System.out.println("all daemons started");
//        double d = 0;
//        for (int i = 1;i<1000000;i++){
//            d+=(Math.PI+Math.E)/(double)i;
//            if (i%1000==0){
//                Thread.yield();
//            }
//        }
//        while(true){
        TimeUnit.MILLISECONDS.sleep(200);
//        Thread.yield();//在while(true)循环中，yield和sleep的效果是一样的
        //无论是让步还是休眠，总之是一到main线程的执行期，main就会停止，
        //即使是让步，那么在让步之后，main也同样会进入while(true)循环中，导致再次抢占
//        }
    }

    static void threadFactory() throws InterruptedException {
//        ExecutorService exec = Executors.newCachedThreadPool(new ThreadFactory(){
//
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread t = new Thread(r);
//                t.setDaemon(true);
//                return t;
//            }
//        });
        ExecutorService exec = new DaemonThreadPoolExecutor();
        for (int i = 0;i<5;i++){
            exec.execute(new SimpleDaemons());
        }
        System.out.println("all daemons started");
        TimeUnit.MILLISECONDS.sleep(199);
    }

    //每个静态的ExecutorService创建方法都被重载为接受一个ThreadFactory对象，
    //甚至，ExecutorService（子类）的构造器也被这样重载了
    static class DaemonThreadPoolExecutor extends ThreadPoolExecutor {
        public DaemonThreadPoolExecutor() {//这里的意义就是有处可以用来配置线程池参数
            super(0, Integer.MAX_VALUE, 60L,
                    TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                    new ThreadFactory(){//该对象被用来创建线程
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setDaemon(true);
                            return t;
                        }
                    });
        }
    }
}
