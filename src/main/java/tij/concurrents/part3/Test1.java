package tij.concurrents.part3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test1{

    public static void main(String[] args) {
        f();
    }

    synchronized static void g(String name){
        for (int i = 5;i-->0;)
            System.out.println(name+": "+i+" / "+Thread.currentThread().getName());
        //这里的输出太神奇了太神奇了！！！
        //线程名经常有重复！想想这是为什么！
        //这是因为前面执行完成的线程池中的线程被重新分配了！执行器没有为新的任务创建线程！因此这在synchronized方法中更容易发生
    }

    static void f(){
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int j = 0;j<5;j++) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
//                    System.out.println(Thread.currentThread().getName());
                    g(Thread.currentThread().getName());
//                    g(Thread.currentThread());
                }
            });
        }
        exec.shutdown();
    }
}
