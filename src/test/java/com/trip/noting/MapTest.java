package com.trip.noting;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.trip.noting.controller.HelloController;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


public class MapTest {

    public static void main(String[] args) {
        List<String> locales = new ArrayList<>();
        locales.add("zh-CN");
        locales.add("en-US");
        for (String locale : locales) {
            for (int i = 0; i < 20; i++) {
                new Thread(() -> {
                    System.out.println(locale);
                }).start();
            }
        }

    }


    @Data
    public static class User {
        private long id;
        private String name;
        private Integer age;
        private List<String> friends;
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
        for (int i = 0; i < strList.size(); i++) {
            if (strList.get(i).equals("hello1")) {
                strList.remove(i--);
            }
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

    @Test
    public void timeStop() throws InterruptedException {
        StopWatch sw = new StopWatch("test");
        sw.start("task1");
        // do something
        Thread.sleep(100);
        sw.stop();
        sw.start("task2");
        // do something
        Thread.sleep(200);
        sw.stop();
        System.out.println("sw.prettyPrint()~~~~~~~~~~~~~~~~~");
        System.out.println(sw.prettyPrint());
        System.out.println(sw.getLastTaskTimeMillis());
    }

    @Test
    public void mergeTest() {
        HashMap<String, Integer> map = new HashMap<>();
        ArrayList<String> strList = new ArrayList<>();
        strList.add("hello");
        strList.add("world");
        strList.forEach(x -> map.merge(x, 1, Integer::sum));
        System.out.println(JSON.toJSONString(map));

    }

    @Test
    public void chunkList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(JSON.toJSONString(list));
        int chunkSize = 2;
        List<List<Integer>> result = Lists.partition(list, chunkSize);
        System.out.println(JSON.toJSONString(result));

    }

    @Test
    public void mapAbsentTest() {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("hello", false);
        System.out.println(JSON.toJSONString(map));
        map.computeIfAbsent("world", (k) -> {
            System.out.println(k);
            return null;
        });
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void mapComputeTest() {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("hello", false);
        System.out.println(JSON.toJSONString(map));
        map.compute("world", (k, v) -> {
            System.out.println(k);
            System.out.println(v);
            if (v == null) {
                return null;
            }
            return null;
        });
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void mapKeyTest() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User() {{
            setId(2L);
            setName("zou");
        }});
        users.add(new User() {{
            setId(2L);
            setName("wen");
        }});
        System.out.println(JSON.toJSONString(users));
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, x -> x, (k1, k2) -> k1));
        System.out.println(JSON.toJSONString(userMap));

    }

    @Test
    public void mapEachTest() {
        ArrayList<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);

    }

    @Test
    public void mapCompute2Test() {
        HashMap<String, String> map = new HashMap<>();
        map.put("hello", "hello");
        map.put("world", "world");
        System.out.println(JSON.toJSONString(map));
        map.compute("hello", (k, v) -> {
            if (v != null) {
                return null;
            }
            return null;
        });
        map.compute("hello1", (k, v) -> {
            if (v != null) {
                return null;
            }
            return null;
        });
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void map2Test() {
        HashMap<String, Integer> mapA = Maps.newHashMap();
        mapA.put("a", 1);
        mapA.put("b", 2);
        mapA.put("c", 3);
        HashMap<String, Integer> mapB = Maps.newHashMap();
        mapB.put("b", 20);
        mapB.put("c", 3);
        mapB.put("d", 4);
        MapDifference<String, Integer> mapDifference = Maps.difference(mapA, mapB);
        // mapA 和 mapB 相同的 entry
        System.out.println(mapDifference.entriesInCommon());
        // mapA 和 mapB key相同的value不同的 entry
        System.out.println(mapDifference.entriesDiffering());
        // 只存在 mapA 的 entry
        System.out.println(mapDifference.entriesOnlyOnLeft());
        // 只存在 mapB 的 entry
        System.out.println(mapDifference.entriesOnlyOnRight());
    }

    @Test
    public void setTest() {
        // set的交集, 并集, 差集
        HashSet<Integer> setA = Sets.newHashSet(1, 2, 3, 4, 5);
        HashSet<Integer> setB = Sets.newHashSet(4, 5, 6, 7, 8);
        // 并集
        Sets.SetView<Integer> union = Sets.union(setA, setB);
        System.out.println(JSON.toJSONString(union));
        // 差集 setA-setB
        Sets.SetView<Integer> difference = Sets.difference(setA, setB);
        System.out.println(JSON.toJSONString(difference));
        // 交集
        Sets.SetView<Integer> intersection = Sets.intersection(setA, setB);
        System.out.println(JSON.toJSONString(intersection));
    }

    @Test
    public void dateTest() {
        String date = "2024-09-08 00:00:00";
        String string = date.substring(0, 10) + "----";
        System.out.println(string);
    }

    @Test
    public void skipTest() {
        List<String> strList = new ArrayList<>();
        strList.add("hello");
        strList.add("world");
        strList.add("world2");
        System.out.println(JSON.toJSONString(strList));
        List<String> stringList = strList.stream().skip(2).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(stringList));
    }

    @Test
    public void timeNowTest() {
        long time = new Timestamp(System.currentTimeMillis()).getTime();
        System.out.println("当前时间为:" + time);

        String str = "预售舱房【%s】不存在";
        String format = String.format(str, 3L);
        System.out.println(format);
    }

    @Test
    public void longTest() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();

        // 任务一模拟休眠3秒钟
        stopWatch.start("TaskOneName");
        Thread.sleep(1000 * 3);
        System.out.println("当前任务名称：" + stopWatch.currentTaskName());
        stopWatch.stop();

        // 任务一模拟休眠10秒钟
        stopWatch.start("TaskTwoName");
        Thread.sleep(1000 * 10);
        System.out.println("当前任务名称：" + stopWatch.currentTaskName());
        stopWatch.stop();

        // 任务一模拟休眠10秒钟
        stopWatch.start("TaskThreeName");
        Thread.sleep(1000 * 10);
        System.out.println("当前任务名称：" + stopWatch.currentTaskName());
        stopWatch.stop();

        // 打印出耗时
        System.out.println(stopWatch.prettyPrint());
        System.out.println(stopWatch.shortSummary());
        // stop后它的值为null
        System.out.println(stopWatch.currentTaskName());

        // 最后一个任务的相关信息
        System.out.println(stopWatch.getLastTaskName());
        System.out.println(stopWatch.getLastTaskInfo());

        // 任务总的耗时  如果你想获取到每个任务详情（包括它的任务名、耗时等等）可使用
        System.out.println("所有任务总耗时：" + stopWatch.getTotalTimeMillis());
        System.out.println("任务总数：" + stopWatch.getTaskCount());
        System.out.println("所有任务详情：" + Arrays.toString(stopWatch.getTaskInfo()));
    }

    public interface Person {
        void sayHello();
    }

    @Test
    public void sortTest() {

        Person person = new Person() {
            private String name; // 私有属性

            // 初始化块
            {
                name = "Alice";
            }

            @Override
            public void sayHello() {
                System.out.println("Hello, my name is " + name);
            }
        };


        List<User> strList = new ArrayList<>();
        strList.add(new User() {{
            setName("2");
            setId(2);
        }});
        strList.add(new User() {{
            setName("3");
            setId(3);
        }});
        strList.add(new User() {{
            setName("4");
            setId(4);
        }});
        strList.add(new User() {{
            setName("1");
            setId(1);
        }});

        System.out.println(JSON.toJSONString(strList));
        strList = strList.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(strList));
    }

    @Test
    public void linkedListTest() {
        LinkedList<String> listNode = new LinkedList<>();
        listNode.add("hello");
        listNode.add("welcome");
        listNode.add("to");
        listNode.add("trip");
        listNode.add("zou");
        System.out.println(JSON.toJSONString(listNode));
        String name = "zou";
        String after = "welcome";
        int nameIndex = -1;
        int afterIndex = -1;
        for (int i = 0; i < listNode.size(); i++) {
            String str = listNode.get(i);
            if (name.equals(str)) {
                nameIndex = i;
            }
            if (after.equals(str)) {
                afterIndex = i;
            }
            if (nameIndex > -1 && afterIndex > -1) {
                if (nameIndex < listNode.size()) {
                    listNode.remove(nameIndex);
                }
                if (afterIndex + 1 <= listNode.size()) {
                    listNode.add(afterIndex + 1, str);
                }
            }
        }
        System.out.println(JSON.toJSONString(listNode));
    }

    @Test
    public void stringTest() {
        List<String> managerList = new ArrayList<>();
        String join = String.join(",", managerList);
        System.out.println(join);
    }

    @Data
    public static class Journey {
        public Integer theDay;
        public Integer staticVoyaJourneyId;
    }

    @Test
    public void sortListTest() {
        List<Journey> arrayList = new ArrayList<>();
        arrayList.add(new Journey() {{
            this.theDay = 1;
            this.staticVoyaJourneyId = 12;
        }});
        arrayList.add(new Journey() {{
            this.theDay = 1;
            this.staticVoyaJourneyId = 11;
        }});
        arrayList.add(new Journey() {{
            this.theDay = 3;
            this.staticVoyaJourneyId = 13;
        }});
        arrayList.add(new Journey() {{
            this.theDay = 2;
            this.staticVoyaJourneyId = 12;
        }});
        System.out.println(JSON.toJSONString(arrayList));
        arrayList.sort((a, b) -> {
            if (a.getTheDay() < b.getTheDay()) return -1;
            else if (a.getTheDay() > b.getTheDay()) return 1;
            else if (a.getStaticVoyaJourneyId() < b.getStaticVoyaJourneyId()) return -1;
            else return 1;
        });
        // arrayList.sort(
        //         Comparator.comparing(Journey::getTheDay)
        //                 .thenComparing(Journey::getStaticVoyaJourneyId)
        // );
        System.out.println(JSON.toJSONString(arrayList));


    }


    @Test
    public void listMaxTest() {
        List<Journey> arrayList = new ArrayList<>();
        arrayList.add(new Journey() {{
            this.theDay = 1;
            this.staticVoyaJourneyId = 12;
        }});
        arrayList.add(new Journey() {{
            this.theDay = 1;
            this.staticVoyaJourneyId = 11;
        }});
        arrayList.add(new Journey() {{
            this.theDay = 3;
            this.staticVoyaJourneyId = 13;
        }});
        int asInt = arrayList.stream().mapToInt(Journey::getTheDay).max().getAsInt();
        System.out.println(asInt);

    }

    public static class TripMessageConfig {
        public String accountId;
        public List<String> receivers;
        public String groupChatId;
        // 1 发送给人 2 发送至群
        public Integer sentType;
    }

    @Test
    public void testConfig() {
        TripMessageConfig tripMessageConfig = new TripMessageConfig();
        tripMessageConfig.accountId = "account-386ffe91";
        tripMessageConfig.receivers = new ArrayList<String>() {{
            add("TR037310");
            add("S37925");
        }};
        tripMessageConfig.groupChatId = "2385477130914889740";
        tripMessageConfig.sentType = 1;
        System.out.println(JSON.toJSONString(tripMessageConfig));
    }

    @Test
    public void testTime() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 毫秒数据
        System.out.println(calendar.getTime().getTime());
        // new Timestamp(Calendar.getInstance().getTimeInMillis());
        Timestamp timestamp = new Timestamp(date.getTime());
        // 毫秒数据
        System.out.println(timestamp.getTime());

    }

    @Test
    public void testSet() {
        String lockValue = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        System.out.println(lockValue);
    }

    @Test
    public void testSunList() {
        List<Long> idList = Collections.singletonList(3L);
        while (!idList.isEmpty()) {
            List<Long> a = new ArrayList<>();
            if (idList.size() > 50) {
                a = idList.subList(0, 50);
            } else {
                a = idList.subList(0, idList.size());
            }
            idList.removeAll(a);
        }
    }

    @Test
    public void testHeap() {
        String str = "16865," + "94291," + "47555," + "38067," + "36218," + "16831," + "107696," + "4222," + "62189," + "39647," + "80403," + "76917," + "39537," + "8320," + "56008," + "38313," + "56015," + "53609," + "3807," + "45727," + "51364," + "43218," + "39934," + "42615," + "43172," + "62189";
        String[] split = StringUtils.split(str, "");
        List<String> list1 = new ArrayList<>();
        for (String string : split) {
            list1.add(string);
        }
        String str2 = "62189,8320,47555,56008,56015,94291,80403,43218,39647,3807,45727,16865,51364,43172,38313,53609,107696,39537,38067,76917,42615,36218,4222,39934,16831";
        String[] split2 = StringUtils.split(str, "");
        List<String> list2 = new ArrayList<>();
        for (String string : split2) {
            list2.add(string);
        }
        list1.retainAll(list2);
        System.out.println(JSON.toJSONString(list1));
        list2.retainAll(list1);
        System.out.println(JSON.toJSONString(list2));

    }

    @Test
    public void equalTime() {
        LocalDateTime dateTime1 = LocalDateTime.parse("2024-01-02 00:00:01", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime dateTime2 = LocalDateTime.parse("2024-01-02 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(dateTime1.equals(dateTime2));
    }

    @Test
    public void testToMap() throws InterruptedException {
        List<User> users = new ArrayList<>();
        users.add(new User() {{
            this.setId(1);
            this.setFriends(new ArrayList<String>() {{
                add("zou");
                add("wen");
            }});
        }});
        users.add(new User() {{
            this.setId(1);
            this.setFriends(new ArrayList<String>() {{
                add("zou1");
                add("wen1");
            }});
        }});
        System.out.println(JSON.toJSONString(users));
        List<String> stringList = users.stream().flatMap(x -> x.friends.stream()).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(stringList));
    }

    private int addOne(Integer v) {
        return v + 3;
    }


    @Test
    public void testListRemove() throws InterruptedException {
        List<User> users = new ArrayList<>();
        users.add(new User() {{
            this.setId(1);
            this.setFriends(new ArrayList<String>() {{
                add("zou1");
                add("wen1");
            }});
        }});
        users.add(new User() {{
            this.setId(2);
            this.setFriends(new ArrayList<String>() {{
                add("zou2");
                add("wen2");
            }});
        }});
        users.add(new User() {{
            this.setId(3);
            this.setFriends(new ArrayList<String>() {{
                add("zou3");
                add("wen3");
            }});
        }});
        System.out.println(JSON.toJSONString(users));

        for (User user : users) {

        }
    }

    @Test
    public void listSort() {
        List<User> users = new ArrayList<>();
        users.add(new User() {{
            this.setId(2);
            this.setName("name2");
        }});
        users.add(new User() {{
            this.setId(3);
            this.setName("name31");
        }});
        users.add(new User() {{
            this.setId(3);
            this.setName("name32");
        }});
        users.add(new User() {{
            this.setId(4);
            this.setName("name4");
        }});
        System.out.println(JSON.toJSONString(users));
        users.sort((a, b) -> {
            if (a.id == 3 && b.id != 3) {
                return -1;
            } else if (a.id != 3 && b.id == 3) {
                return 1;
            }
            return 0;
        });
        System.out.println(JSON.toJSONString(users));

    }

    @Test
    public void testThread() {
        String str = "啊啊啊啊1啊啊";
        System.out.println(str.length());
    }

    @Test
    public void testBreak() {
        labelB:
        for (int i = 0; i < 3; i++) {
            System.out.println("i:" + i);
            for (int j = 0; j < 3; j++) {
                System.out.println("j:" + j);
                if (j == 1) {
                    break labelB;
                }
            }
        }
        System.out.println("over!");
    }


}
