package com.trip.noting;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class MapTest {

    @Data
    public static class User {
        private long id;
        private String name;
    }


    @Test
    public void test() {
        List<User> userList = new ArrayList<User>();
        userList.add(new User() {{
            setName("1L");
        }});
        userList.add(new User() {{
            setId(1L);
            setName("");
        }});
        Map<Long, String> stringMap = userList.stream().collect(Collectors.toMap(User::getId, User::getName));
        System.out.println(JSON.toJSONString(stringMap));

    }

    @Test
    public void testRemove() {
        List<String> strList = new ArrayList<>();
        strList.add("hello1");
        strList.add("hello1");
        strList.add("hello2");
        strList.add("hello3");
        strList.add("hello4");
        Iterator<String> iterator = strList.iterator();
        System.out.println(JSON.toJSONString(strList));
        while (iterator.hasNext()) {
            String next = iterator.next();
            if ("hello1".equals(next)) {
                iterator.remove();
            }
            System.out.println(next);
        }
        System.out.println(JSON.toJSONString(strList));
    }

    @Test
    public void testRemove2() {
        List<String> strList = new ArrayList<>();
        strList.add("hello1");
        strList.add("hello1");
        strList.add("hello2");
        strList.add("hello3");
        strList.add("hello4");
        for (String item : strList) {
            if ("hello1".equals(item)) {
                strList.remove(item);
            }
            System.out.println(item);
        }
        System.out.println(JSON.toJSONString(strList));
    }

    @Test
    public void testCompute() {
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("hello", new ArrayList<String>() {{
            add("hello1");
            add("hello2");
        }});
        System.out.println(JSON.toJSONString(map));
        map.compute("hello", (k, v) -> {
            if (v != null) {
                v.add("hello3");
            } else {
                v = new ArrayList<String>() {{
                    add("hello3");
                }};
            }
            return v;
        });
        map.compute("world", (k, v) -> {
            if (v != null) {
                v.add("world1");
            } else {
                v = new ArrayList<String>() {{
                    add("world1");
                }};
            }
            return v;
        });
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void timeTest() throws InterruptedException {
        LocalDateTime start = LocalDateTime.now();
        TimeUnit.SECONDS.sleep(2);
        LocalDateTime end = LocalDateTime.now();
        Duration between = Duration.between(start, end);
        long millis = between.toMillis();
        System.out.println(millis);

    }
}
