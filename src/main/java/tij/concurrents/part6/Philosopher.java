package tij.concurrents.part6;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {
    private Chopstick left;
    private Chopstick right;
    private final int id;
    private final int ponderFactor;
    private Random rand = new Random(47);
    private void pause() throws InterruptedException {//代表一小段时间
        if (ponderFactor==0) return;//很快死锁
//        TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor*250));
        TimeUnit.MILLISECONDS.sleep(0);
    }

    public Philosopher(Chopstick left, Chopstick right, int id, int ponderFactor) {
        this.left = left;
        this.right = right;
        this.id = id;
        this.ponderFactor = ponderFactor;
    }

    @Override
    public String toString() {
        return "Philosopher "+id;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                System.out.println(this+" thinking");
                pause();
                //becomes hungry
                System.out.println(this+" grabbing right");
                right.take();
//                Thread.sleep(100);//这样一来就准能死锁了
                //该场景下死锁的含义就是：
                //每个人都拿起了右边的筷子，在去拿左边的筷子时，发现都被拿走了
                System.out.println(this+" grabbing left");
                left.take();
                System.out.println(this+" eating");
                pause();//用pause方法的sleep来代替吃饭的过程
                right.drop();
                left.drop();
            }
        } catch (InterruptedException e) {
            System.out.println(this+" exiting via interrupt");
        }

    }
}
