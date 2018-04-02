package tij.concurrents.part3;

public class Atomicity {
    int i ;
    void f(){i++;}
    void g(){i+=3;}
}
