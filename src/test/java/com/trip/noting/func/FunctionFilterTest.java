package com.trip.noting.func;

import org.junit.Test;

public class FunctionFilterTest {

    @Test
    public void filterTest() {
        boolean filterRes = filterSomething(()->false);
        System.out.println(filterRes);
    }

    private boolean filterSomething(FunctionFilter filter) {
        if (filter == null) {
            return false;
        }
        return filter.skip();
    }
}
