package com.trip.noting.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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


    @Test
    public void mailCheckTest(){
        Optional.of(null);
        System.out.println("=====");
    }

    public boolean EmailCheck(String email) {
        if (email.endsWith(";") || email.endsWith(",") || email.endsWith("，") || email.endsWith("；")) {
            email = email.substring(0, email.length() - 1);
        }
        List<String> emailList = Arrays.asList(email.split("[;；,，]"));
        return emailList.stream().allMatch(a -> Pattern.matches("^[a-zA-Z0-9]{1}[a-zA-Z0-9\\-_.]+@([a-zA-Z0-9]{1}[a-zA-Z0-9\\-_]+\\.)+[a-zA-Z0-9]{1}[a-zA-Z0-9\\-_]+$", a));
    }

    public boolean checkMarkdownClosed(String str) {
        StringBuilder temp = new StringBuilder(); // 存放标签
        List<List<String>> unclosedTags = new ArrayList<>();
        unclosedTags.add(new ArrayList<>()); // 前不闭合，如有</div>而前面没有<div>
        unclosedTags.add(new ArrayList<>()); // 后不闭合，如有<div>而后面没有</div>
        boolean flag = false; // 记录双引号"或单引号'
        char currentJump = ' '; // 记录需要跳过''还是""
        char current = ' ', last = ' '; // 当前 & 上一个

        // 开始判断
        for (int i = 0; i < str.length(); ) {
            current = str.charAt(i++); // 读取一个字符
            if (current == '"' || current == '\'') {
                flag = !flag; // 若为引号，flag翻转
                currentJump = current;
            }
            if (!flag) {
                if (current == '<') { // 开始提取标签
                    current = str.charAt(i++);
                    if (current == '/') { // 标签的闭合部分，如</div>
                        current = str.charAt(i++);

                        // 读取标签
                        while (i < str.length() && current != '>') {
                            temp.append(current);
                            current = str.charAt(i++);
                        }

                        // 从tags_bottom移除一个闭合的标签
                        if (!unclosedTags.get(1).remove(temp.toString())) { // 若移除失败，说明前面没有需要闭合的标签
                            if (!temp.toString().toLowerCase().equals("br"))
                                unclosedTags.get(0).add(temp.toString()); // 此标签需要前闭合
                        }
                        temp.delete(0, temp.length()); // 清空temp
                    } else { // 标签的前部分，如<div>
                        last = current;
                        while (i < str.length() && current != ' ' && current != '>') {
                            temp.append(current);
                            last = current;
                            current = str.charAt(i++);
                        }

                        // 已经读取到标签，跳过其他内容，如<div id=test>跳过id=test
                        while (i < str.length() && current != '>') {
                            last = current;
                            current = str.charAt(i++);
                            if (current == '"' || current == '\'') { // 判断引号
                                flag = !flag;
                                currentJump = current;
                                if (flag) { // 若引号不闭合，跳过到下一个引号之间的内容
                                    while (i < str.length() && str.charAt(i++) != currentJump) ;
                                    current = str.charAt(i++);
                                    flag = false;
                                }
                            }
                        }
                        if (last != '/' && current == '>' && !temp.toString().toLowerCase().equals("br")) // 判断这种类型：<TagName />
                            unclosedTags.get(1).add(temp.toString());
                        temp.delete(0, temp.length());
                    }
                }
            } else {
                while (i < str.length() && str.charAt(i++) != currentJump) ; // 跳过引号之间的部分
                flag = false;
            }
        }
        return unclosedTags.get(0).isEmpty() && unclosedTags.get(1).isEmpty();
    }
}
