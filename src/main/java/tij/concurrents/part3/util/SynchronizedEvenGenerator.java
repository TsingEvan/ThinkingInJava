package tij.concurrents.part3.util;

import tij.concurrents.part3.EvenChecker;

public class SynchronizedEvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;
    @Override
    public synchronized int next() {
        ++currentEvenValue;
        for (int i = 0;i<2;i++)
            Thread.yield();//yield()会释放锁吗？看来它不会释放锁。那么这段代码的意义是什么？
                                //猜测是对的。TIJ原文也这样描述：
                                /**
                                 * 对Thread.yield()的调用被插入到了两个递增操作之间，以提高
                                 * 在currentEvenValue是奇数状态时上下文切换的可能性。因为
                                 * 互斥可以防止多个任务同时进入临界区，所以这不会产生任何失败。但是如果失败
                                 * 将会发生，调用yield()是一种促使其发生的有效方式。
                                 */
        ++currentEvenValue;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new SynchronizedEvenGenerator());
    }
}
