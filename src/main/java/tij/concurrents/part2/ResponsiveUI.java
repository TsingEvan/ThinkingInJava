package tij.concurrents.part2;

public class ResponsiveUI extends Thread {
    private static volatile double d = 1;
    public ResponsiveUI(){
        setDaemon(true);
        start();
    }
    public void run(){
        while(true){
            d = d+(Math.PI+Math.E)/d;
            System.out.printf(" %.2f,",d);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            new ResponsiveUI();
        }catch(Exception e){
            //在这里捕获不到其他线程里的异常
        }
        for (int i = 0;i<10;i++) {
            Thread.sleep(1);
        }
        System.out.printf("\n%.2f\n",d);
        System.out.printf("\n%.2f\n",d);
    }
}

//问题是：主线程输出完毕，守护线程竟然又往下运行
