package tij.concurrents.part2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExceprionThread implements Runnable{
    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new ExceprionThread());
    }
}
