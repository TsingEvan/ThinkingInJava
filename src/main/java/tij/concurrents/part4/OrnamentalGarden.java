package tij.concurrents.part4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrnamentalGarden {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0;i<3;i++){
            exec.execute(new Entrance(i));
        }
        TimeUnit.MILLISECONDS.sleep(2);
        Entrance.cancel();
        exec.shutdown();
        if(!exec.awaitTermination(250,TimeUnit.MILLISECONDS)){
            System.out.println("Some tasks were not terminated!");
        }
        System.out.println("Total: "+Entrance.getTotalCount());
        System.out.println("Sum of Entrances: "+Entrance.sumEntrances());
    }
}
class Count{
    private int count = 0;
    private Random rand = new Random(47);
    public  int increment(){
        int temp = count;//走到这里停住了，然后其他人开始加加加，随后时间又回到该线程上，该线程拿着temp+1，然后傻了吧唧的又赋给了count
        if(rand.nextBoolean()){//约一半情况下，会进入
            Thread.yield();
        }
        return (count = ++temp);//那么其实就是count+=1；
//        return ++count;
    }
    public synchronized int value(){
        return count;
    }
}
class Entrance implements Runnable{
    private static Count count = new Count();
    private static List<Entrance> entrances = new ArrayList<Entrance>();
    private int number = 0;
    private final int id ;
    private static volatile boolean canceled = false;
    public static void cancel(){
        canceled = true;
    }
    public Entrance (int id){
        this.id = id;
        entrances.add(this);
    }
    @Override
    public void run() {
        while(!canceled){
            synchronized (this){
                ++number;
            }
            System.out.println(this+" Total: "+count.increment());
//            try{
//                TimeUnit.MILLISECONDS.sleep(100);
//            } catch (InterruptedException e) {
//                System.out.println("sleep interrupted");
//            }
        }
        System.out.println("Stopping "+this);
    }
    public synchronized int getValue(){
        return number;
    }
    public String toString(){
        return "Entrance "+id+": "+getValue();
    }
    public static int getTotalCount(){
        return count.value();
    }
    public static int sumEntrances(){
        int sum = 0;
        for (Entrance entrance : entrances){
            sum+=entrance.getValue();
        }
        return sum;
    }
}