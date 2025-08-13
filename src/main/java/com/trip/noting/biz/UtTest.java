package com.trip.noting.biz;

import com.alibaba.fastjson2.JSON;
import com.trip.noting.controller.HelloController;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UtTest {

    public static class Detail {
        public String name;
        public String countryName;
    }

    //
    private String getDescription(Integer voyageType, Boolean isPort, List<Detail> poiDetails) {
        System.out.println("=========");
        System.out.println("voyageType" + voyageType);
        System.out.println("isPort" + isPort);
        System.out.println("poiDetails" + JSON.toJSONString(poiDetails));
        return "";
    }

    @Test
    public void getDescriptionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UtTest obj = new UtTest();
        Class<UtTest> utTestClass = UtTest.class;
        // 获取私有方法
        Method method = utTestClass.getDeclaredMethod("getDescription", Integer.class, Boolean.class, List.class);
        method.invoke(obj, 1, false, new ArrayList<Detail>() {{
            add(new Detail() {{
                this.name = "zou";
                this.countryName = "china";
            }});
        }});
    }

    public void printArray(int... array) {
        for (int number : array) {
            System.out.println(number);
        }
    }

    @Test
    public void testArrArgs() {
        LocalDateTime now = LocalDateTime.now();
        // LocalDateTime newNow = LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);

        String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(format);
        System.out.println(format.substring(11,16));

    }

    @Test
    public void testImage() {


    }

}
