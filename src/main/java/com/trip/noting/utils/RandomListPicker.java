package com.trip.noting.utils;

import com.trip.noting.biz.UtTest;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    @Test
    public void getDescriptionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UtTest obj = new UtTest();
        Class<UtTest> utTestClass = UtTest.class;
        // 获取私有方法
        Method method = utTestClass.getDeclaredMethod("getDescription",Integer.class,Boolean.class,List.class);
        method.setAccessible(true);
        method.invoke(obj, 1, false, new ArrayList<UtTest.Detail>() {{
            add(new UtTest.Detail(){{
                this.name="zou";
                this.countryName="china";
            }});
        }});
    }
}