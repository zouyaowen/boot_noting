package com.trip.noting.func;

public interface Task {

    default TaskContext getContext() {
        return null;
    }

    String getName();

    boolean skip();

    void process();
}
