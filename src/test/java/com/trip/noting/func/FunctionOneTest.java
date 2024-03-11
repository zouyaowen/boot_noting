package com.trip.noting.func;

import org.junit.Test;

public class FunctionOneTest {

    @Test
    public void func() {
        String str = "hello";
        showSomething(str, (x) -> System.out.println("hello:" + x), () -> false);
    }

    private void showSomething(String str, FunctionOne func, FunctionFilter filter) {
        if (filter.skip()) {
            return;
        }
        func.method(str);
    }


}
