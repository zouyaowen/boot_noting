package com.trip.noting.graph;

import com.alibaba.fastjson2.JSON;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;

public class GraphTaskTest {


    private final DirectedAcyclicGraph graphTask = new DirectedAcyclicGraph();

    @Before
    public void before() {
        graphTask.executor = new ThreadPoolExecutor(2, 5, 5L, TimeUnit.MINUTES, new ArrayBlockingQueue<>(500), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @After
    public void after() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) graphTask.executor;
        executor.shutdown();
    }

    @Test
    public void taskExec() {
        User user = new User();
        user.name = "zuo";
        user.age = 99;

        System.out.println(JSON.toJSONString(user));

        graphTask.addTask(new DirectedAcyclicGraph.Task() {
            @Override
            public String getName() {
                return "task1";
            }

            @Override
            public boolean skip(DirectedAcyclicGraph.TaskContext context) {
                return false;
            }

            @Override
            public void process(DirectedAcyclicGraph.TaskContext context) {
                System.out.println("task1 execute start");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                user.name = "yao";
                System.out.println("task1 execute finish");
            }
        }, true);

        graphTask.addTask(new DirectedAcyclicGraph.Task() {
            @Override
            public String getName() {
                return "task2";
            }

            @Override
            public boolean skip(DirectedAcyclicGraph.TaskContext context) {
                return false;
            }

            @Override
            public void process(DirectedAcyclicGraph.TaskContext context) {
                System.out.println("task2 execute start");
                user.name = "wen";
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("task2 execute finish");
            }
        }, true);

        graphTask.build().execute(new TaskContextBiz());
        System.out.println(JSON.toJSONString(user));
    }

    @Test
    public void dagTest() {
        User user = new User();
        user.name = "zuo";
        user.age = 99;
        System.out.println(JSON.toJSONString(user));

        graphTask.addTask("task1", (c) -> {
            System.out.println("task1 execute start");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            user.name = "yao";
            System.out.println("task1 execute finish");
        });
        graphTask.addTask("task2", (c) -> {
            System.out.println("task2 execute start");
            user.name = "wen";
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("task2 execute finish");
        }, "task1");
        graphTask.build().execute(new TaskContextBiz());
        System.out.println(JSON.toJSONString(user));
    }


    @Test
    public void dependencyExecTest() throws InterruptedException {
        graphTask.addTask("task1", (c) -> {
            System.out.println("task1");
        }, "task2", "task3").addTask("task2", (c) -> {
            System.out.println("task2");
            throw new RuntimeException("task2 exec error");
        }, true).addTask("task3", (c) -> {
            System.out.println("task3");
        });
        graphTask.build().execute(new TaskContextBiz());
        System.out.println("----main thread finish----");
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void dependencyTest() throws InterruptedException {
        graphTask.addTask("task1", (c) -> {
            System.out.println("task1");
        }, "task2", "task3").addTask("task2", (c) -> {
            System.out.println("task2");
        }, true);
        Assert.assertThrows(Exception.class, () -> {
            graphTask.build().execute(new TaskContextBiz());
        });
        System.out.println("----main thread finish----");
    }

    @Test
    public void taskParamTest() throws InterruptedException {
        final String str = "hello";
        graphTask.addTask("task1", (c) -> {
            System.out.println("task1");
            String newStr = str + str;
            System.out.println("newStr:" + newStr);
        }, (c) -> {
            // dag对象直接赋值
            return false;
        }, false, "task2").addTask("task2", (c) -> {
            System.out.println("task2");
        }, true);
        TaskContextBiz taskContext = new TaskContextBiz();
        graphTask.build().execute(taskContext);
        System.out.println("----main thread finish----");
    }

    public static class TaskContextBiz implements DirectedAcyclicGraph.TaskContext {

    }


    public static class User {
        public String name;
        public Integer age;
    }

}
