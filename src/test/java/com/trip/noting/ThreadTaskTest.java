package com.trip.noting;

import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.concurrent.*;

import static net.openhft.affinity.AffinityStrategies.*;

// 参考 https://my.oschina.net/flydean/blog/5524916
public class ThreadTaskTest {

    // AffinityThreadFactory 中支持多种策略，配置到同一个 Core 上直接new AffinityThreadFactory("bg", SAME_CORE)，全部不同直接new AffinityThreadFactory("bg", DIFFERENT_CORE)
    private static final ExecutorService ES = Executors.newFixedThreadPool(4, new AffinityThreadFactory("bg", SAME_CORE, DIFFERENT_SOCKET, ANY));

    public static void main(String[] args) throws InterruptedException {
        int cupNum = Runtime.getRuntime().availableProcessors();

        System.out.println("cupNum:" + cupNum);

//        // acquireLock 方法可以为线程获得任何可用的 cpu
//        try (AffinityLock al = AffinityLock.acquireLock()) {
//            System.out.println(al);
//        }
//        // acquireLock 方法可以为线程获得任何可用的 cpu。这个是一个粗粒度的 lock。如果想要获得细粒度的 core，可以用 acquireCore
//        try (AffinityLock al = AffinityLock.acquireCore()) {
//            // do some work while locked to a CPU.
//            System.out.println(al);
//        }

        for (int i = 0; i < 12; i++) {
            ES.submit((Callable<Void>) () -> {
                Thread.sleep(100);
                return null;
            });
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("\nThe assignment of CPUs is\n" + AffinityLock.dumpLocks());
        ES.shutdown();
        boolean awaitTermination = ES.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("awaitTermination:" + awaitTermination);
    }
}
