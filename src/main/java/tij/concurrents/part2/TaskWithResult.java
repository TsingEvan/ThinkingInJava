package tij.concurrents.part2;

import java.util.ArrayList;
import java.util.concurrent.*;

public class TaskWithResult implements Callable<String> {
    private int id ;
    public TaskWithResult(int id){
        this.id = id;
    }
    @Override
    public String call() throws Exception {
        String str = "starting...";
        Thread.sleep(5);
        str+="loading...";
        Thread.sleep(5);
        return "ID:"+id+" "+str+"finished";
    }

    public static void main(String[] args) {
        callResult();
    }

    static void callResult(){
        ExecutorService exec = Executors.newCachedThreadPool();
        final ArrayList<Future<String>> results = new ArrayList<Future<String>>();
        for (int i=0;i<10;i++) {
            Future<String> future = exec.submit(new TaskWithResult(i));
            results.add(future);
        }
        exec.execute(new Runnable(){
            @Override
            public void run() {
                for (int i =0;i<1000;i++) {
//                    try {//测试何谓在Future未准备就绪时get()会阻塞。根据结果来看可能意味着阻塞当前线程
//                        results.get(0).get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
                    System.out.print(i+",");
                    Thread.yield();
                }
            }
        });
        for (Future<String> future : results) {
            try {
                String result = future.get();
                System.out.println(result);
            }catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                exec.shutdown();
            }
        }

    }
}
