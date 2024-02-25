package com.trip.noting;


import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// https://www.baeldung.com/java-testing-multithreaded
// https://www.digitalocean.com/community/tutorials/java-multithreading-concurrency-interview-questions-answers
public class BenchmarkTest {
    // https://juejin.cn/post/6914960426098917384
    public static void main(String[] args) {
        // https://medium.com/@himanshugaur1215/multithreading-in-java-4aaca48cfa5a
        int n = 1000;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        Executor executor = Executors.newFixedThreadPool(90000);

        List<CompletableFuture<Void>> futures = IntStream.range(0, n).boxed().map(i -> (CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(Math.min(1000 * i, 10000));
                for (int j = 0; j < i; j++)
                    ;
                System.out.println("job done by me " + i);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }, executor))).collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        stopWatch.stop();
        long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
        System.out.println(lastTaskTimeMillis);
    }
}
