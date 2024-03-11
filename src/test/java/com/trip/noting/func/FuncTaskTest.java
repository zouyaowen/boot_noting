package com.trip.noting.func;

import com.trip.noting.graph.DirectedAcyclicGraph;
import org.junit.Test;

public class FuncTaskTest {

    @Test
    public void funcTaskTest() {

        addTask("hello", () -> {
            System.out.println("I am coming");
        });

        addTask("world", () -> {
            System.out.println("I am coming ,world");
        });

        addTask(() -> {
            System.out.println("I am coming ,world");
        });

        addTask(() -> {
            System.out.println("I am coming ,world");
        },"world");


    }

    private void addTask(String name, ProcessFunc processFunc, SkipFunc skipFunc) {

    }

    private void addTask(ProcessFunc processFunc) {

    }

    private void addTask(ProcessFunc processFunc, String... funcName) {

    }

    private void addTask(String name, ProcessFunc processFunc) {
        NameFunc nameFunc = new NameFunc() {
            @Override
            public String getName() {
                return name;
            }
        };
        DirectedAcyclicGraph.Task task = new DirectedAcyclicGraph.Task() {
            @Override
            public String getName() {
                return nameFunc.getName();
            }

            @Override
            public boolean skip(DirectedAcyclicGraph.TaskContext context) {
                return false;
            }

            @Override
            public void process(DirectedAcyclicGraph.TaskContext context) {
                processFunc.process();
            }
        };


    }
}
