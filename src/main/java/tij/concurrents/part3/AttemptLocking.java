package tij.concurrents.part3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();
    public void untimed(){
        boolean captured = lock.tryLock();
        /**
         * 如果锁被另一个线程保持，则此方法将立即返回 false 值。
         * @from api
         */

        try{
            System.out.println("tryLock(): "+captured);
        } finally {
            if (captured){
                lock.unlock();
            }
        }
    }
    public void time(){
        boolean captured = false;
        try{
            captured = lock.tryLock(2, TimeUnit.SECONDS);
            /**
             * 如果锁被另一个线程保持，则出于线程调度目的，禁用当前线程，并且在发生以下三种情况之一以前，该线程将一直处于休眠状态：
                - 锁由当前线程获得；或者
                - 其他某个线程中断当前线程；或者
                - 超过指定的等待时间
             * 如果超出了指定的等待时间，则返回值为 false。如果该时间小于等于 0，则此方法根本不会等待。
             * @from api
             */

        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        try{
            System.out.println("tryLock(2,TimeUnit.SECONDS): "+captured);
        } finally {
            if (captured){
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final AttemptLocking al = new AttemptLocking();
        al.untimed();
        al.time();
        //**Pro.1有疑问，可能错误**Lock接口的锁不会随着程序运行完成而自动释放。new启动的时候，time肯定是运行完成的，但是输出总是false，即没有拿到锁
        //我觉得是这样的：lock对象被调用lock()时，保持计数+1，而被调用unlock()时，保持计数-1，故而是根据保持计数来判断当前线程是否可获得该锁对象，不调用unlock，保持计数不会减少，锁不会被其他线程获得
        new Thread(){
            {setDaemon(true);}
            public void run(){
//                System.out.println("acquire before lock");
//                boolean b = al.lock.tryLock();//这个线程如果grad到cpu时间的话，是可以拿到锁的
                //如果这里改成lock()你知道会发生什么吗?
                //new thread当然获取不到锁，于是休眠，直到前台线程运行完毕，被kill
//                System.out.println("acquire lock: "+b);
                //new Thread拿到锁以后没有释放，那么它的运行结果将说明Lock接口释放锁可不可以通过程序的自然结束
                //时刻注意：拿到锁并不是独占了CPU时间！！！只是独占锁资源！因此这个线程还是可能切回main
//                double d = 10000;
//                while(true){
//                    d*=Math.E*Math.PI/(3.14*Math.PI);
//                    if ((int)d==0){
//                        break ;
//                    }
//                    System.out.println(al.lock.tryLock());
//                }
//                while(true){
////                    al.lock.lock();
////                    System.out.println(">>");
//                    System.out.println(al.lock.tryLock());
//                    Thread.yield();
//                }
                System.out.println(al.lock.tryLock());/**
                                            其实只需要这么一行代码就能验证锁是否会随着线程运行完毕而被自动释放
                                            main.untimed 和 main.time 输出都为false 故而得出结论：不能
                                                            */

                /**
                 * 按道理的确是主线程结束，子线程也都结束，但主线程销毁运行时环境，kill掉子线程这都是需要时间的
                 * @from 知乎
                 * 这样看来，将线程设置为daemon=true并不能保证在主线程结束之后它立即被销毁
                 */
            }
        }.start();//匿名类无法shutdown
        Thread.sleep(10);//意思就是说，你先等等。然后 new Thread 和 main 重新grab cpu-time,
                        // 因为new Thread之前可能没有开始执行任务，或者还没有尝试获取锁
                        //当然了，yield之后new仍然不一定能grab（抢占）
//        System.out.println("back to main");
        al.untimed();//锁被别的线程占用时，tryLock并不会报错，只是尝试
                    //**对比Pro1**此处输出为true，说明拿到锁了，表示，锁被释放了
                    //卧槽！！！！我特么傻逼了吧？！这是同一个线程（main）啊！！！锁本来就在main这儿，当然能请求到！！！
        al.time();
    }
}

//最重要的问题是：程序运行结束，是否释放锁  >>>  看Line.90  我傻逼了 不会释放》》》不对，我说的是线程执行完毕是否释放锁，而main还没有执行完毕
