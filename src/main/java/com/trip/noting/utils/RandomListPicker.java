package com.trip.noting.utils;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomListPicker {

    /**
     * 获取列表随机元素
     *
     * @param list 列表
     * @param <T>  泛型约束
     * @return 获取列表中的随机元素
     */
    public static <T> Optional<T> pickOne(List<T> list) {
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        Random random = new Random();
        int index = random.nextInt(list.size());
        return Optional.of(list.get(index));
    }
}