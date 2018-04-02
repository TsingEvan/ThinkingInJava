package tij.concurrents.part3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExplicitCriticalSection {
    public static void main(String[] args) {
        PairManager
                pman1 = new ExplicitPairManager1(),
                pman2 = new ExplicitPairManager1();
        CriticalSection.testApproaches(pman1, pman2);
    }
}
class ExplicitPairManager1 extends PairManager{//PairManager又不是内部类，不用导入
    private Lock lock = new ReentrantLock();
    @Override
    public void increment() {
        lock.lock();
        try{
            p.incrementX();
            p.incrementY();
            store(p);
        } finally{
            lock.unlock();
        }

    }
}
class ExplicitPairManager2 extends PairManager{
    private Lock lock = new ReentrantLock();
    @Override
    public void increment() {
        Pair temp;
        lock.lock();
        try{
            p.incrementX();
            p.incrementY();
            temp = p;
        } finally{
            lock.unlock();
        }
        store(temp);
    }
}
//注意：Lock没有起到同步的作用
