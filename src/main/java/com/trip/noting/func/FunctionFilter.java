package com.trip.noting.func;

@FunctionalInterface
public interface FunctionFilter {
    // 定义一个抽象的方法
    boolean skip();
}
