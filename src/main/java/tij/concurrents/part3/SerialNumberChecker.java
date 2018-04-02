package tij.concurrents.part3;

import tij.concurrents.part3.util.SerialNumberGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SerialNumberChecker {
    private static final int SIZE = 10;
    private static CircularSet serials = new CircularSet(1000);
    private static ExecutorService exec = Executors.newCachedThreadPool();
    static class SerialChecker implements Runnable{
        @Override
        public void run() {
            while(true){
                int serial = SerialNumberGenerator.nextSerialNumber();
                /**
                 * 我不能理解的是：
                 * SerialNumberGenerator类中的synchronized和当前类中的synchronized用的是同一个锁吗？
                 * 如果是的话，那么也就是说，只要在当前类中访问同步方法，即使该方法并不属于该类，
                 * 它所锁定的仍然是该类实例this，故而其他线程也就不能访问该类当中定义的同步方法。
                 */
                if(serials.contains(serial)){
                    System.out.println("Duplicate: "+serial);
                    System.exit(0);
                }
                serials.add(serial);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0;i<SIZE;i++){
            exec.execute(new SerialChecker());
        }
        if(args.length>0){
            TimeUnit.SECONDS.sleep(new Integer(args[0]));
            System.out.println("No duplicates detected");
            System.exit(0);
        }
    }
}
class  CircularSet{
    private int[] arr;
    private int len;
    private int index = 0;
    public CircularSet(int size){
        arr = new int[size];
        len = size;
        for (int i = 0;i<size;i++){
            arr[i] = -1;
        }
    }
    public synchronized void add(int i){
        arr[index] = i;
        index = ++index%len;//index通过while(true)的循环会不断的增长，故而最后可能会比len更大
    }
    public synchronized boolean contains(int val){
        for (int i = 0;i<len;i++){
            if (arr[i] == val) {
                return true;
            }
        }
        return false;
    }
}
