package tij.concurrents.part7;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CountDownLatchDemo {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss")));
        System.out.println(String.valueOf(LocalDateTime.now()));
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }
}
