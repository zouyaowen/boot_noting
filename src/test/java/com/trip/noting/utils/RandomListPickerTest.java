package com.trip.noting.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomListPickerTest {

    @Test
    public void randomListTest() {
        List<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(5);
        arrayList.add(6);
        Optional<Integer> integerOptional = RandomListPicker.pickOne(arrayList);
        integerOptional.ifPresent(System.out::println);
        RandomListPicker.pickOne(arrayList).ifPresent(System.out::println);
        RandomListPicker.pickOne(arrayList).ifPresent(System.out::println);
        RandomListPicker.pickOne(arrayList).ifPresent(System.out::println);
        RandomListPicker.pickOne(arrayList).ifPresent(System.out::println);
        RandomListPicker.pickOne(arrayList).ifPresent(System.out::println);
        RandomListPicker.pickOne(arrayList).ifPresent(System.out::println);
    }
}
