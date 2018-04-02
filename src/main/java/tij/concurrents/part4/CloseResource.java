package tij.concurrents.part4;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CloseResource {
    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(8080);
        InputStream socketInput = new Socket("localhost",8080).getInputStream();
        InputStream in = System.in;
        exec.execute(new IOBlocked(socketInput));
        exec.execute(new IOBlocked(in));
        TimeUnit.MILLISECONDS.sleep(100);
//        System.out.println("Shutting down all threads");
        exec.shutdownNow();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing "+socketInput.getClass().getSimpleName());
        socketInput.close();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing "+System.in.getClass().getSimpleName());
        in.close();

//        exec.shutdownNow();
    }
}
