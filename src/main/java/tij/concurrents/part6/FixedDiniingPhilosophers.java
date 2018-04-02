package tij.concurrents.part6;

import com.sun.rmi.rmid.ExecOptionPermission;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FixedDiniingPhilosophers {
    public static void main(String[] args) throws InterruptedException {
        int ponder = 5, size = 5;
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] chopsticks = new Chopstick[size];
        for (int i = 0;i < size; i++){
            chopsticks[i] = new Chopstick();
        }
        for (int i = 0; i < size; i++){
            if (i<(size-1)){
                exec.execute(new Philosopher(chopsticks[i], chopsticks[i+1],i,ponder));
            }else {
                exec.execute(new Philosopher(chopsticks[0], chopsticks[i],i,ponder));
            }
        }
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
