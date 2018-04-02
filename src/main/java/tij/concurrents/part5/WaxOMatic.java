package tij.concurrents.part5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WaxOMatic {
    public static void main(String[] args) throws InterruptedException {
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOff(car));
//        exec.execute(new WaxOff(car));
        exec.execute(new WaxOn(car));
//        exec.execute(new WaxOff(car));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
class Car {
    private boolean waxOn = false;
    public synchronized void waxed() throws InterruptedException {
        waxOn = true;
        notify();
        Thread.sleep(2000);//验证如果notify之后当前线程还未释放锁，而被唤醒线程又来请求锁的话，是不是又会被挂起
                            //猜测这种挂起只是普通的阻塞，不需要新的notify来唤醒
                            /**
                             * 猜测是正确的。
                             * 2s之后，on释放锁，off可参与抢占
                             */
    }
    public synchronized void buffed(){
        waxOn = false;
        notify();//被唤醒的off1执行此句时，car的等待队列里有两个线程：on和off2，虽然条件不一样，但是它们都在等car
                //根据结果推测唤醒顺序为FIFO
    }
    public  synchronized void waitForWaxing() throws InterruptedException {
        while(waxOn == false){
            wait();
        }
    }
    public synchronized void waitForBuffing() throws InterruptedException {
//        System.out.println("Waiting for buffing");
        while(waxOn == true){
            wait();
        }
    }
}
class WaxOn implements Runnable{
    private Car car;
    public WaxOn(Car car){
        this.car = car;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                System.out.println("Wax On! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();//notify并不是把cpu时间直接转让给另一个等待线程，只是将它唤醒到就绪状态，
                            //因而，当前线程还是有机会继续执行的
                            /**
                             * 上面的结论是错的。
                             * tij里这样说：
                             *     为了使该任务从wait中唤醒，它必须首先重新获得当它进入wait()时释放的锁。
                             *     在这个锁变得可用之前，这个任务是不会被唤醒的。
                             * 也就是说，notify之后当前线程仍旧要主动释放锁
                             */
//                Thread.yield();
                car.waitForBuffing();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending Wax On task");
    }
}
class WaxOff implements Runnable{
    private Car car;
    public WaxOff(Car car){
        this.car = car;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                car.waitForWaxing();
                System.out.println("Wax Off! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending Wax Off task");
    }
}
