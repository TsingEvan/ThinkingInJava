package tij.concurrents.part3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtomicityTest implements Runnable {
    private int i = 0;
    public synchronized int getValue(){
        /**
         * 把getValue()也设置为同步方法就可以了，
         * this被a线程拿到以后，this中所有synchronized修饰的方法将统统不可被其他线程访问
         * 因而main也就不能再synchronized evenIncrement()返回之前访问getValue()
         */
        return i;
     }
     private synchronized void evenIncrement(){//synchronized修饰部分是说一旦进来就要执行完吗？
         //还是说其他线程不能访问this锁已被持有的synchronized方法？
         i++;
         i++;
     }
     @Override
     public void run() {
         while(true){
         evenIncrement();
         }
     }

     public static void main(String[] args) {
         ExecutorService exec = Executors.newCachedThreadPool();
         AtomicityTest at = new AtomicityTest();
         exec.execute(at);
         while(true){
             int val = at.getValue();//自增和验证使用的是同一个值
             if(val%2!=0){
                 System.out.println(val);
                 System.exit(0);
             }
        }
     }

 }