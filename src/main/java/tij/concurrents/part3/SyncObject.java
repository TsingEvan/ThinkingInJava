package tij.concurrents.part3;

public class SyncObject {
    public static void main(String[] args) {
        final DualSync ds = new DualSync();
        new Thread(){
            public void run(){
                ds.f();
            }
        }.start();
        ds.g();
    }
}
class DualSync{
    private Object sync = new Object();
    public synchronized void f(){
        for (int i = 0;i<5;i++){
            System.out.println("f()");
            Thread.yield();
        }
    }
    public void g(){
        synchronized (sync){
            for (int i = 0;i<5;i++){
                System.out.print("g()");
                Thread.yield();
            }
        }
    }
}