package tij.concurrents.part5;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ToastOMatic {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue
                dryQueue = new ToastQueue(),
                butteredQueue = new ToastQueue(),
                finishedQueue = new ToastQueue(),
                commonQueue = new ToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue, commonQueue));
        exec.execute(new Butterer(dryQueue, butteredQueue, commonQueue));
        exec.execute(new Jammer(butteredQueue, finishedQueue));
        exec.execute(new Eater(finishedQueue, commonQueue));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
class Toast{
    public enum Status { DRY,BUTTERED,JAMMED}
    private Status status = Status.DRY;
    private final int id;
    public Toast(int idn){
        id = idn;
    }
    public void butter(){
        status = Status.BUTTERED;
    }
    public void jam(){
        status = Status.JAMMED;
    }
    public Status getStatus(){
        return status;
    }
    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "Toast "+id+": "+status;
    }
}
class ToastQueue extends LinkedBlockingQueue<Toast>{}

class Toaster implements Runnable{
    private ToastQueue toastQueue ,commonQueue;
    private int count = 0;
    private Random rand = new Random(47);
    public Toaster (ToastQueue tq, ToastQueue cq){
        toastQueue = tq;
        commonQueue = cq;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(100+rand.nextInt(500));
                Toast t = new Toast(count++);
                System.out.println(t);
                toastQueue.put(t);
                commonQueue.put(new Toast(0));
            }
        } catch (InterruptedException e) {
            System.out.println("Toaster interrupted");
        }
        System.out.println("Toaster off");
    }
}

class Butterer implements Runnable{
    private ToastQueue toastQueue, butteredQueue, commonQueue;
    private Random rand = new Random(47);
    public Butterer (ToastQueue tq, ToastQueue bq, ToastQueue cq){
        toastQueue = tq;
        butteredQueue = bq;
        commonQueue = cq;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(100+rand.nextInt(500));
                Toast toast = toastQueue.take();
                toast.butter();
                System.out.println(toast);
                butteredQueue.put(toast);
                commonQueue.put(new Toast(1));
            }
        } catch (InterruptedException e) {
            System.out.println("Butterer interrupted");
        }
        System.out.println("Butterer off");
    }
}

class Jammer implements Runnable{
    private ToastQueue butteredQueue, finishedQueue;
    private Random rand = new Random(47);
    public Jammer (ToastQueue bq, ToastQueue fq){
        butteredQueue = bq;
        finishedQueue = fq;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
                Toast toast = butteredQueue.take();
                toast.jam();
                System.out.println(toast);
                finishedQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.out.println("Jammer interrupted");
        }
        System.out.println("Jammer off");
    }
}

class Eater implements Runnable{
    private ToastQueue finishedQueue ,commonQueue;
    private int counter = 0;
    public Eater(ToastQueue finished, ToastQueue common){
        finishedQueue = finished;
        commonQueue = common;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                System.out.println(">>>"+commonQueue.take());
                Toast t = finishedQueue.take();
                if(t.getId()!=counter++||t.getStatus()!=Toast.Status.JAMMED){
                    System.out.println(">>>> Error: "+t);
                    System.exit(1);
                }else {
                    System.out.println("Chomp! "+t);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Eater interrupted");
        }
        System.out.println("Eater off");
    }
}