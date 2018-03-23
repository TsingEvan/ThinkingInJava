package tij.annotations;

import org.junit.Test;

public class JUnitTest {

    String na(String world){
        return "hello "+world;
    }

    @Test
    public void f(){
        System.out.println(na("world").equals("hello world"));
    }
}
