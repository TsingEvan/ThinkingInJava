package tij.concurrents.part6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeadlockingDiningPhilosophers {
    public static void main(String[] args) throws InterruptedException {
        int ponder = 5;
        int size = 5;
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];
        for (int i = 0;i < size;i++){
            sticks[i] = new Chopstick();
        }
        for (int i = 0; i < size; i++){
            exec.execute(new Philosopher(sticks[i],sticks[(i+1)%size],i,ponder));
        }
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
