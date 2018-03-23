package tij.concurrents.part3.util;

import tij.concurrents.part3.EvenChecker;

public class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;
    @Override
    public int next() {
        ++currentEvenValue;//仔细想一想奇数是如何产生的？
        /**
         * 即使在上一句+1之后线程被切换了，但是EvenChecker是不会拿到值的，因为值是通过继续向下运行到return才返回的；
         * 那么奇数是怎么产生的呢？
         * Thread+1 切换到Thread2 Thread2+1+1return得到奇数
         */
        ++currentEvenValue;
//        currentEvenValue += 2;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator());
    }
}
