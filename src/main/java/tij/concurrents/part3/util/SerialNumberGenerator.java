package tij.concurrents.part3.util;

public class SerialNumberGenerator {
    private static volatile int serialNumber = 0;
    public static synchronized int nextSerialNumber(){
        return serialNumber++;
    }
}
