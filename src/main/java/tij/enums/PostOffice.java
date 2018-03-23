package tij.enums;

import tij.enums.util.Enums;

import java.util.Iterator;

public class PostOffice {
    enum MailHandler{
        GENERAL_DELIVERY{
            @Override
            boolean handler(Mail m) {
                switch (m.generalDelivery){
                    case YES:
                        System.out.println("Using general delivery for "+m);
                        return true;
                    default:
                        return false;
                }
            }
        },
        MACHINE_SCAN{
            @Override
            boolean handler(Mail m) {
                switch (m.scannability){
                    case UNSCANNABLE: return false;
                    default:
                        switch (m.address){
                            case INCORRECT: return false;
                            default:
                                System.out.println("Delivering "+m+" automatically");
                                return true;
                        }
                }
            }
        },
        VISUL_INSPRCTION{
            @Override
            boolean handler(Mail m) {
                switch (m.readbility){
                    case ILLEGIBLE: return false;
                    default:
                        switch (m.address){
                            case INCORRECT: return false;
                            default:
                                System.out.println("Delivering "+m+" normally");
                                return true;
                        }
                }
            }
        },
        RETURN_TO_SENDER{
            @Override
            boolean handler(Mail m) {
                switch (m.returnAddress){
                    case MISSING: return false;
                    default:
                        System.out.println("Returning "+m+" to sender");
                        return true;
                }
            }
        };
        abstract boolean handler(Mail m);
    }
    static void handler(Mail m){
        for (MailHandler handler :
                MailHandler.values()) {
            if (handler.handler(m)) {
                return;
            }
        }
        System.out.println(m+" is a dead letter");
    }

    public static void main(String[] args) {
        for (Mail mail:Mail.generator(10)){
            System.out.println(mail.detial());
            handler(mail);
            System.out.println("***************");
        }
    }
}
class Mail{
    enum GeneralDelivery{YES,NO1,NO2,NO3,NO4,NO5}
    enum Scannability{UNSCANNABLE,YES1,YES2,YES3,YES4}
    enum Readbility{ILLEGIBLE,YES1,YES2,YES3,YES4}
    enum Address{INCORRECT,OK1,OK2,OK3,OK4,OK5}
    enum ReturnAddress{MISSING,OK1,OK2,OK3,OK4,OK5}
    GeneralDelivery generalDelivery;
    Scannability scannability;
    Readbility readbility;
    Address address;
    ReturnAddress returnAddress;
    static long counter = 0;
    long id = counter++;
    public String toString(){
        return "Mail "+id;
    }
    public String detial(){
        return toString()+
                ", GeneralDelivery: " +generalDelivery +
                ", Scannability: " +scannability +
                ", Readbility: " +readbility +
                ", Address: " +address +
                ", ReturnAddress: " +returnAddress;
    }
    public static Mail randomMail(){
        Mail m = new Mail();
        m.generalDelivery = Enums.random(GeneralDelivery.class);
        m.scannability = Enums.random(Scannability.class);
        m.readbility = Enums.random(Readbility.class);
        m.address = Enums.random(Address.class);
        m.returnAddress = Enums.random(ReturnAddress.class);
        return m;
    }
    public static Iterable<Mail> generator(final int count){
        return new Iterable<Mail>(){
            int n = count;
            @Override
            public Iterator<Mail> iterator() {
                return new Iterator<Mail>() {
                    @Override
                    public boolean hasNext() {
                        return n-->0;
                    }

                    @Override
                    public Mail next() {
                        return randomMail();
                    }
                };
            }
        };
    }
}
