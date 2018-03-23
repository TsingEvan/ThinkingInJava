package tij.concurrents.part2;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class ThreadVariations {

    public static void main(String[] args) {
        new InnerThread1("InnerThread1");
        new InnerThread2("InnerThread2");
        new InnerRunnable("InnerRunnable");
        new InnerRunnable2("InnerRunnable2");
        new ThreadMethod("ThreadMethod").runTask();
    }
}
//Using a named inner class
class InnerThread1{
    private int countDown = 5;
//    private Inner inner;
    private class Inner extends Thread{
        Inner(String name){
            super(name);
            start();
        }
        public void run(){
            try{
                while (true){
                    System.out.println(this);
                    if (--countDown==0) return ;
                    sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public String toString(){
            return getName()+": "+countDown+", ";
        }
    }
    public InnerThread1(String name){
        new Inner(name);
    }
}
//Using an anonymous inner class
class InnerThread2{
    private int countDown = 5;
    private Thread t;
    public InnerThread2(String name){
        t = new Thread(name){
            public void run(){
                try{
                    while (true){
                        System.out.println(this);
                        if (--countDown==0) return ;
                        sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            public String toString(){
                return getName()+": "+countDown+", ";
            }
        };
        t.start();
    }
}
class InnerRunnable{
    private int countDown = 5;
    private class Inner implements Runnable{
        Thread t;
        Inner (String name){
            t = new Thread(this, name);
            t.start();
        }
        @Override
        public void run() {
            try{
                while (true){
                    System.out.println(this);
                    if (--countDown==0) return ;
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public String toString(){
            return t.getName()+": "+countDown+", ";
        }
    }
    public InnerRunnable(String name){
        new Inner(name);
    }
}
class InnerRunnable2{
    private int countDown = 5;
    private Thread t;
    public InnerRunnable2(String name){
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (true){
                        System.out.println(this);
                        if (--countDown==0) return ;
                        TimeUnit.MILLISECONDS.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            public String toString(){
                return t.getName()+": "+countDown+", ";
            }
        }, name);
        t.start();
    }
}
class ThreadMethod{
    private int countDown = 5;
    private String name ;
    private Thread t;
    public ThreadMethod(String name) {
        this.name = name;
    }
    public void runTask(){
        t = new Thread(name){
            public void run(){
                try{
                    while (true){
                        System.out.println(this);
                        if (--countDown==0) return ;
                        sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            public String toString(){
                return getName()+": "+countDown+", ";
            }
        };
        t.start();
    }
}