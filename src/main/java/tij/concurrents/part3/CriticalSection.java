package tij.concurrents.part3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CriticalSection {
    static void testApproaches(PairManager pman1, PairManager pman2){
        ExecutorService exec = Executors.newCachedThreadPool();
        PairManipulator
                pm1 = new PairManipulator(pman1),
                pm2 = new PairManipulator(pman2);
        PairChecker
                pcheck1 = new PairChecker(pman1),
                pcheck2 = new PairChecker(pman2);
        exec.execute(pm1);//用来增加Pair.x&y
//        try {
//            Thread.sleep(5000);//增加Pair大约2times/s
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        exec.execute(pm2);
        exec.execute(pcheck1);//用来校验同时增加校验次数
        exec.execute(pcheck2);
        try{
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted");
        }
        System.out.println("pm1: "+pm1+"\npm2: "+pm2);
        System.exit(0);//通过这句代码停下的
    }
    public static void main(String[] args) {
        PairManager
                pman1 = new PairManager1(),
                pman2 = new PairManager2();
        testApproaches(pman1,pman2);
    }
}
class Pair{
    private int x,y;
    public Pair(int x,int y){
        this.x = x;
        this.y = y;
    }
    public Pair(){
        this(0,0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void incrementX(){
        x++;
    }
    public void incrementY(){
        y++;
    }
    public String toString(){
        return "x: "+x+", y: "+y;
    }
    public class PairValuesNotEqualException extends RuntimeException{
        public PairValuesNotEqualException(){
            super("Pair values not equal: "+Pair.this);
        }
    }
    public void checkState(){
        if(x!=y){
            throw new PairValuesNotEqualException();
        }
    }
}
abstract class PairManager{
    AtomicInteger checkCounter = new AtomicInteger(0);
//    void addCheckCounter(){
//        /**
//         * this被锁究竟意味着什么？
//         * 难道连this中的普通方法也不能访问了吗？之所以这么说，是因为将addCheckCounter改为普通方法后，
//         * 结果两种同步方式所得的checkCounter还是差特别多
//         * tij上针对这里有句话：
//         *      后者采用同步控制块进行同步，所以对象不加锁的时间更长。
//         */
//
//        checkCounter.incrementAndGet();
//    }
    protected Pair p = new Pair();
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());
    public synchronized Pair getPair(){
        return new Pair(p.getX(), p.getY());//return a copy
    }
    protected void store(Pair p){
        storage.add(p);
        try{
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {}
    }
    public abstract void increment();
}
class PairManager1 extends PairManager{
    @Override
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}
class PairManager2 extends PairManager{
    @Override
    public void increment() {
        Pair temp;
        synchronized (this){//使用临界区竟然可能比pm1不使用同步的速度还要快
            p.incrementX();
            p.incrementY();
            temp = getPair();
        }
        store(temp);
    }
}
class PairManipulator implements Runnable{
    private PairManager pm;
    public PairManipulator(PairManager pm){
        this.pm = pm;
    }
    @Override
    public void run() {
        while(true){
            pm.increment();
        }
    }
    public String toString(){
        return "Pair: "+pm.getPair()+" checkCounter = "+pm.checkCounter.get();
    }
}
class PairChecker implements Runnable{
    private PairManager pm;
    public PairChecker(PairManager pm){
        this.pm = pm;
    }
    @Override
    public void run() {
        while(true){
//            pm.addCheckCounter();
            pm.checkCounter.incrementAndGet();
            pm.getPair().checkState();//结论：我特么的没注意到这里有getPair()，这是个同步方法啊!!!原地爆炸
        }
    }
}