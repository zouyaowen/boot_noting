package com.trip.noting;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.filter.NameFilter;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BeanTest {

    static final String JOB_PACKAGE_NAME = BeanTest.class.getPackage().getName();

    @Test
    public void beanCopyTest() {
        UserCopy userCopy = new UserCopy();
        userCopy.name = "zou";
        userCopy.age = 22;
        UserCopy newCopy = new UserCopy();
        System.out.println(JSON.toJSONString(newCopy));
        BeanUtils.copyProperties(userCopy, newCopy);
        System.out.println(JSON.toJSONString(newCopy));
    }

    @Test
    public void loaderCopyTest() throws IOException {
        String JOB_PACKAGE_NAME = BeanTest.class.getPackage().getName();
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ImmutableSet<ClassPath.ClassInfo> topLevelClasses = ClassPath.from(loader).getTopLevelClassesRecursive(JOB_PACKAGE_NAME);
        for (ClassPath.ClassInfo topLevelClass : topLevelClasses) {
            System.out.println(topLevelClass.getName());
        }
    }

    @Test
    public void jsonTest() throws IOException {
        String s = "111.2086";
        String string = rectifyFloat(s);
        // rectifyFloat("3330200");
        System.out.println(string);

        String s2 = "111.20868";
        String string2 = rectifyFloat(s2);
        // rectifyFloat("3330200");
        System.out.println(string2);

    }

    // 矫正float类型精度,直接截取保留最多3位小数
    private String rectifyFloat(String floatStr) {
        if (StringUtils.isBlank(floatStr)) {
            return floatStr;
        }
        // 最多保留三位小数
        if (floatStr.length() <= 5) {
            return floatStr;
        }
        int index = floatStr.indexOf(".");
        if (index == -1) {
            return floatStr;
        }
        if (floatStr.length() - index - 1 >= 4) {
            return floatStr.substring(0, index + 4);
        }
        return floatStr;
    }

    @Test
    public void dateTest() throws IOException {
        String dateStr = "2020-09-08 09:09:09";
        String substring = dateStr.substring(0, 10);
        System.out.println(substring);
        System.out.println(dateStr.substring(0, 12));
    }

    @Test
    public void futureTest() throws IOException, ExecutionException, InterruptedException {
        String url = "http://ws.downloadfile.fx.ctripcorp.com/files/6/vacations/0F63512000fg4e84lA2AA.xlsx";
        String expiration = "2h";
        String rand = "123456";
        String auth = "727c487229e62f17c9f72c2a35688ed8";
        String str = url + expiration + rand + auth;
        System.out.println(str);
        String string = DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
        System.out.println(string);
    }

    @Test
    public void jsonParse() throws IOException {
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        Resource resource = defaultResourceLoader.getResource("classpath:" + "temp.json");
        if (!resource.exists()) {
            throw new FileNotFoundException("File not found: " + "temp.json");
        }
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] buffer = new byte[inputStream.available()];
            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) {
                throw new IOException("Failed to read file: " + "temp.json");
            }
            String jsonContent = new String(buffer, StandardCharsets.UTF_8);
            List<SailingJson> sailings = JSON.parseArray(jsonContent, SailingJson.class);
            System.out.println(sailings.size());
            List<FlushJson> flushList = new ArrayList<>();
            for (SailingJson sailing : sailings) {
                flushList.add(new FlushJson() {{
                    setName(sailing.get_index());
                    setValue(sailing.get_id());
                }});
            }
            System.out.println(JSON.toJSONString(flushList, new UpperCaseNameFilter()));
        }
    }

    class UpperCaseNameFilter implements NameFilter {
        @Override
        public String process(Object object, String name, Object value) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }

    @Test
    public void nullTest() throws IOException {
        FlushJson json = new FlushJson();
        if (json.getAge() == 9) {
            System.out.println(json.getAge());
        }
    }

    @Test
    public void compareTest() throws IOException {
        int i1 = new BigDecimal("2.3").compareTo(BigDecimal.ZERO);
        int i2 = new BigDecimal("-1").compareTo(BigDecimal.ZERO);
        int i3 = new BigDecimal("0").compareTo(BigDecimal.ZERO);
        System.out.println(i1);
        System.out.println(i2);
        System.out.println(i3);
    }

    public static byte[] compressData(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(data.getBytes());
        }
        return bos.toByteArray();
    }

    public static String decompressData(byte[] compressedData) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData); GZIPInputStream gzipIS = new GZIPInputStream(bais)) {
            IOUtils.copy(gzipIS, baos);
        }
        return baos.toString();
    }

    @Test
    public void jsonSTest() throws IOException {
        String originalString = "这是一个12312 342423  4332423";
        String replacedString = originalString.replace(" ", "");
        System.out.println(replacedString);
    }

    public static String getRandomAlphanumeric(int length) {
        // 定义字母数字字符集合
        String alphanumericChars = "A0B1C2D3E4F5G6H7I8J9KLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            // 从字母数字字符集合中随机选择一个字符
            int index = random.nextInt(alphanumericChars.length());
            randomString.append(alphanumericChars.charAt(index));
        }

        return randomString.toString();
    }

    private String renameFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        // 找到最后一个点号的位置
        int dotIndex = fileName.lastIndexOf('.');
        // 如果点号存在且不是文件名的第一个字符，返回点号后的字符串
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(0, dotIndex) + "_" + getRandomAlphanumeric(10) + fileName.substring(dotIndex);
        }
        // 如果没有找到点号或者点号是文件名的第一个字符，返回空字符串
        return "";
    }

    @Test
    public void testName() throws UnsupportedEncodingException {
        String name = "http://ws.downloadfile.fx.ctripcorp.com/files/6/vacations/0F63512000fg4e84lA2AA.xlsx2h123456727c487229e62f17c9f72c2a35688ed8";
        String string2 = Base64.getUrlEncoder().encodeToString(name.getBytes());
        System.out.println(string2);

    }

    private String renameFileName_(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        // 找到最后一个点号的位置
        int dotIndex = fileName.lastIndexOf('.');
        // 如果点号存在且不是文件名的第一个字符，返回点号后的字符串
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(0, dotIndex) + "_" + getRandomAlphanumeric(10) + fileName.substring(dotIndex);
        }
        // 如果没有找到点号或者点号是文件名的第一个字符，返回空字符串
        return "";
    }

    @Test
    public void testCode() {
        int code = 100000000 + 2001 * 1000;
        System.out.println(code/100000000);
        int sharkcode = code % 100000000 / 1000;
        int sharktype = code / 100000000;
        System.out.println(sharkcode);
    }
    //   101001002

    @Test
    public void testCode1() {
        List<MapTest.User> users = new ArrayList<>();
        users.add(new MapTest.User(){{
            this.setId(1L);
            this.setName("1L");
        }});
        users.add(new MapTest.User(){{
            this.setId(3L);
            this.setName("1L");
        }});
        users.add(new MapTest.User(){{
            this.setId(2L);
            this.setName("1L");
        }});
        System.out.println(JSON.toJSONString(users));
        users.sort(Comparator.comparing(MapTest.User::getId));
        System.out.println(JSON.toJSONString(users));

    }
}
