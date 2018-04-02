package tij.concurrents.part6;

public class Chopstick {
    private boolean taken = false;
    public synchronized void take() throws InterruptedException {
        while(taken){
            wait();//并不真的占用资源，完全使用taken属性在模拟占用，所以synchronized并不能阻塞take被拿走筷子的线程，
                        //而需要wait来主动等待
                    //该场景下这样设计是完全合理的，因为哲学家吃多久饭的控制不在筷Chopstick对象里，
                            //而是在Philosopher对象里通过pause控制
        }
        taken = true;
    }
    public synchronized void drop(){
        taken = false;
        notifyAll();
    }
}
