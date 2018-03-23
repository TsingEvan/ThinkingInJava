package tij.concurrents.part2;

public class Joining {
    public static void main(String[] args) {
        Sleeper
            sleepy = new Sleeper("Sleepy", 5000),
            grumpy = new Sleeper("Grumpy", 5000);
        Joiner //共有5个线程启动
            dopey = new Joiner("Dopey", sleepy),
            doc = new Joiner("Doc", grumpy);
        grumpy.interrupt();
    }
}
class Sleeper extends Thread{
    private int duration;
    public Sleeper(String name, int sleepTime){
        super(name);
        duration = sleepTime;
        start();
    }
    public void run(){
        try{
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName()+" was interrupted. "
                +"isInterrupted(): "+isInterrupted());
            return ;//pay attention
        }
        System.out.println(getName()+" has awakened");
    }
}
class Joiner extends Thread{
    private Sleeper sleeper;
    public Joiner(String name, Sleeper sleeper){
        super(name);
        this.sleeper = sleeper;
        start();
    }
    public void run(){
        try{
            sleeper.join();
        } catch (InterruptedException e) {//因为在sleeper中已经处理过interruptedException了，故而这里是捕获不到的
            System.out.println("Interrupted");
        }
        System.out.println(getName()+" join completed");
    }
}