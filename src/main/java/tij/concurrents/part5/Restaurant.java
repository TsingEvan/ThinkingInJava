package tij.concurrents.part5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Restaurant {
    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);
    public Restaurant (){
        exec.execute(waitPerson);
        exec.execute(chef);
    }

    public static void main(String[] args) {
        new Restaurant();
    }
}
class Meal{
    private final int orderNum;

    public Meal(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "Meal " + orderNum;
    }
}
class WaitPerson implements Runnable{
    private Restaurant restaurant;

    public WaitPerson(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                synchronized (this){
                    //p1
                    while(restaurant.meal == null){//因为它和chef使用了同一个资源：meal，
                                                   //因而需要使用同一个锁来保证它们不会同时操作此资源
                            /**
                             * 不对不对！应该是这样的：
                             * 如果同步块a的操作会影响到同步块b的判断条件，那么二者应当使用同一个锁
                             */
                        wait();
                    }
                }
                System.out.println("Waitperson got "+restaurant.meal);//此时meal不为空了
                synchronized (restaurant.chef){//在这里握手！！！
                    restaurant.meal = null;
                    restaurant.chef.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("WaitPerson interrupted");
        }
    }
}
class Chef implements Runnable{
    private Restaurant restaurant;
    private int count = 0;

    public Chef(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this){
                    while(restaurant.meal != null){
                        wait();
                    }
                }
                if(++count==10){
                    System.out.println("Out of food, closing");
                    restaurant.exec.shutdownNow();
                }
                System.out.println("Order up! ");
                synchronized (restaurant.waitPerson){//chef被阻塞在p1上
                    restaurant.meal = new Meal(count);
                    //Point
                    /**
                     * 看来只要遵守了一定的书写规范（套路），就不必再逐个Point的判断是否会导致死锁发生
                     */
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Chef interrupted");
        }
    }
}
/**
 * P-C模型特点：每个任务里都有一个 改变条件+唤醒的操作 和一个 判断条件+等待的操作
 * 所以任务中的两个同步块可以使用不同的锁
 *
 * 比如说我是一个notify的方法，我希望你（wait）在我改变完你需要的值之前不要去做判断，
 * 我并不关心另一个notify是否在我尚未执行完之前又改变了这个值？？？
 *
 * 也就是说，我不在乎你是否是根据我的改变结果做出的判断，我只在乎你是根据真实的当前值做出判断
 *  不用这样担心的核心原因在于wait操作会释放锁，因而notify终究会改变为恰当的值并唤醒同锁wait线程
 *
 *    I                    YOU
 *  sync I             sync YOU
 * meal = null        meal = new Meal()
 * notify             notify
 *
 *  sync YOU           sync I
 * if meal == null    if meal != null
 * wait               wait
 */