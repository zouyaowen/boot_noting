package com.trip.noting.func;

@FunctionalInterface
public interface FunctionOne {
    // 定义一个抽象的方法，重写的是method方法
    void method(String name);

    // void show();
    default void show() {
        System.out.println("show");
    }
}
