package tij.concurrents.part5;

import java.util.concurrent.TimeUnit;

public class Test1{
    private Restaurant restaurant;
    class WaitPerson{
        public void run() throws InterruptedException {
            synchronized (this){
                while(restaurant.meal == null){ wait(); }
            }
//            System.out.println("Waitperson got "+restaurant.meal);
            synchronized (restaurant.chef){
                restaurant.meal = null;
                restaurant.chef.notifyAll();
            }
        }
    }
    class Chef {
        public void run() throws InterruptedException {
            synchronized (this){
                while(restaurant.meal != null){ wait(); }
            }
//            System.out.println("Order up! ");
            synchronized (restaurant.waitPerson){
//                restaurant.meal = new Meal(count);
                restaurant.waitPerson.notifyAll();
            }
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}
//如果同步块a的操作会影响到同步块b的判断条件，那么二者应当使用同一个锁

