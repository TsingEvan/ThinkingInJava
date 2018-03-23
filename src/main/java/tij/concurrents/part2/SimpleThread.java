package tij.concurrents.part2;

public class SimpleThread extends Thread {

    private int countDown = 10;
    private static int threadCount = 0;
    public SimpleThread(){
        super(Integer.toString(++threadCount));
//        start();
    }
    public String toString(){
        return "#"+getName()+"("+countDown+")";
    }

    public void run(){
        while (true){
            System.out.println(this);
            if (--countDown==0){
                return;
            }
//            Thread.yield();
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        Thread t = new Thread();
//        t.start();
//        for (int i = 0; i<5;i++){
//            new SimpleThread();
//        }

        Thread t1 = new Thread(new SimpleThread());
        Thread t2 = new Thread(new SimpleThread());
//        t2.join();//要知道，现在可是在main线程上调用的join()
        t1.start();
                //t2.sleep(3000);//别忘了，t1和t2可都是继承了Thread的，这里，不管谁调，其实都是掉的同一个静态方法，休眠的是当前线程
                t1.join(1);//1ms内t1没能执行完，之后一切照旧
                //Thread.currentThread().join();//这意味着什么！我的天啊！这样的话不就是让main挂起，却又等待main执行完毕吗！
                            //t1.join();//必须要在调用join()的线程开始后该方法才会起作用（比如，如果这里变为t2.join()就不会有影响）
                                //而这里的输出也是很有意思的：1-main-2
                                //为什么2也在1执行完成之后才启动呢？
                                    //因为此时t2还没有被启动，而main已经被挂起，所以只有等到t1结束后main执行时才会启动t2
        t2.start();//t1.join();//只挂起了当前线程，也就是main，而t1和t2则仍然需要抢占资源
        int i = 0;
        while(true){
            System.out.println(0.0);
            if (i++==10)
                return;
        }
    }
}
