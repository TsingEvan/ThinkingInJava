package tij.concurrents.part7;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Test1 {
    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
        System.out.println(LocalDate.now());
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println(f());
    }
    static String f(){
        int j = 1/0;
        try{
            int i = 0;
            return "try";
        }catch(Exception e){
            return "caught";
        } finally {
            return "finnaly";
        }
    }
}
