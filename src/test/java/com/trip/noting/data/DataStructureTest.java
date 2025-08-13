package com.trip.noting.data;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DataStructureTest {

    // 定义二叉树节点类
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int val) { this.val = val; }
    }

    @Test
    public void DynamicProgrammingDemo2(){
        // 动态规划示例1：斐波那契数列（自底向上）
        int n = 10;
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        System.out.println("斐波那契数列前" + (n+1) + "项: " + Arrays.toString(dp));
        System.out.println("第" + n + "项: " + dp[n]);

        // 动态规划示例2：零钱兑换（最少硬币数）
        int[] coins = {1, 2, 5};
        int amount = 11;
        int[] minCoins = new int[amount + 1];
        Arrays.fill(minCoins, amount + 1); // 初始化为不可能的大值
        minCoins[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i - coin >= 0) {
                    minCoins[i] = Math.min(minCoins[i], minCoins[i - coin] + 1);
                }
            }
        }
        System.out.println("零钱兑换，金额为" + amount + "时最少硬币数: " + (minCoins[amount] > amount ? -1 : minCoins[amount]));
    }

    @Test
    public void DynamicProgrammingDemo1(){
        // 动态规划示例：斐波那契列（自底向上）
        int n = 10;
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        System.out.println("斐波那契数列前" + (n+1) + "项: " + Arrays.toString(dp));
        // 输出第n项
        System.out.println("第" + n + "项: " + dp[n]);
    }

    @Test
    public void binaryTreeBFSDemo(){
        // 构建一棵简单的二叉树
        //      1
        //     / \
        //    2   3
        //   / \   \
        //  4   5   6
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.right = new TreeNode(6);
        // BFS遍历（层序遍历）
        List<Integer> result = new ArrayList<>();
        List<TreeNode> queue = new ArrayList<>();
        if (root != null) queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.remove(0);
            result.add(node.val);
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
        System.out.println("层序遍历: " + result);
    }

    @Test
    public void binaryTreeDFSDemo() {
        // 构建一棵简单的二叉树
        //      1
        //     / \
        //    2   3
        //   / \   \
        //  4   5   6
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.right = new TreeNode(6);

        System.out.print("前序遍历: ");
        preOrder(root);
        System.out.println();
        System.out.print("中序遍历: ");
        inOrder(root);
        System.out.println();
        System.out.print("后序遍历: ");
        postOrder(root);
        System.out.println();
    }

    // 前序遍历（根-左-右）
    private void preOrder(Object node) {
        TreeNode root = (TreeNode) node;
        if (root == null) return;
        System.out.print(root.val + " ");
        preOrder(root.left);
        preOrder(root.right);
    }
    // 中序遍历（左-根-右）
    private void inOrder(Object node) {
        TreeNode root = (TreeNode) node;
        if (root == null) return;
        inOrder(root.left);
        System.out.print(root.val + " ");
        inOrder(root.right);
    }
    // 后序遍历（左-右-根）
    private void postOrder(Object node) {
        TreeNode root = (TreeNode) node;
        if (root == null) return;
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.val + " ");
    }

    @Test
    public void quickSortDemo() {
        // 创建一个整数数组
        int[] arr = {5, 3, 8, 4, 2};
        // 执行快速排序
        quickSort(arr, 0, arr.length - 1);
        // 输出排序后的数组
        System.out.println("排序后的数: " + Arrays.toString(arr));
    }

    private void quickSort(int[] arr, int start, int end) {
        if (start < end) {
            int pivotIndex = partition(arr, start, end);
            quickSort(arr, start, pivotIndex - 1); // 递归排序左半部分
            quickSort(arr, pivotIndex + 1, end); // 递归排序右���部分
        }
    }

    private int partition(int[] arr, int start, int end) {
        int pivot = arr[end]; // 选择最后一个���素作为基准
        int j = start - 1; // j指向小于基准的最后一个元素
        for (int k = start; k < end; k++) {
            if (arr[k] <= pivot) { // 如果当前元素小于等于基准
                j++; // 增加小于基准的元素计数
                swap(arr, j, k); // 交换位置
            }
        }
        swap(arr, j + 1, end); // 将基准放到正确的位置
        return j + 1; // 返回基准的最终位置
    }

    @Test
    public void binarySearchDemo() {
        // 有序数组
        int[] arr = {1, 3, 5, 7, 9, 11, 13, 15};
        int target = 7;
        int left = 0, right = arr.length - 1;
        int foundIndex = -1;
        // 二分查找过程
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                foundIndex = mid;
                break;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (foundIndex != -1) {
            System.out.println("找到目标 " + target + ", 下标: " + foundIndex);
        } else {
            System.out.println("未找到目标 " + target);
        }
    }


    @Test
    public void mergeSortDemo() {
        // 创建一个整数数组
        int[] arr = {38, 27, 43, 3, 9, 82, 10};
        // 执行归并排序
        mergeSort(arr, 0, arr.length - 1);
        // 输出排序后的数组
        System.out.println("排序后的数组: " + Arrays.toString(arr));
    }

    private void mergeSort(int[] arr, int begin, int end) {
        // 如果数组长度小于2，则无需排序
        if (begin >= end) {
            return;
        }
        // 计算中间索引
        int mid = (begin + end) / 2;
        // 递归排序左半部分
        mergeSort(arr, begin, mid);
        // 递归排序右半部分
        mergeSort(arr, mid + 1, end);
        // 合并两个已排序的部分
        merge(arr, begin, mid, end);
    }

    private void merge(int[] arr, int begin, int mid, int end) {
        // 创建临时数组存储合并结果，长度为合并区间的元素个数
        int[] temp = new int[end - begin + 1];
        int i = begin;      // 左半部分的起始索引
        int j = mid + 1;    // 右半部分的起始索引
        int k = 0;          // 临时数组的索引
        // 合并两个有序区间
        while (i <= mid && j <= end) {
            // 比较左右两部分当前元素，较小的放入临时数组
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        // 如果左半部分还有剩余元素，全部复制到临时数组
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        // 如果右半部分还有剩余元素，全部复制到临时数组
        while (j <= end) {
            temp[k++] = arr[j++];
        }
        // 将临时数组中的元素复制回原数组对应区间
        for (int m = 0; m < temp.length; m++) {
            arr[begin + m] = temp[m];
        }
    }

    @Test
    public void bubbleSortDemo() {
        // 创建一个整数数组
        int[] arr = {5, 3, 8, 4, 2};
        // 执行冒泡排序
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换元素
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        // 输出排序后的数组
        System.out.println("排序后的数组: " + Arrays.toString(arr));
    }

    @Test
    public void queueDemo() {
        // 创建一个队列
        List<String> queue = new ArrayList<>();
        // 入队操作
        queue.add("A");
        queue.add("B");
        queue.add("C");
        // 出队操作
        while (!queue.isEmpty()) {
            String element = queue.remove(0); // 移除并返回队列的第一个元素
            System.out.println("出队元素: " + element);
        }
        // 输出队列是否为空
        System.out.println("队列是否为空: " + queue.isEmpty());
    }

    @Test
    public void stackDemo() {
        // 创建一个栈
        List<String> stack = new ArrayList<>();
        // 入��操作
        stack.add("A");
        stack.add("B");
        stack.add("C");
        // 出栈操作
        while (!stack.isEmpty()) {
            // 移除并返��栈顶元素
            String element = stack.remove(stack.size() - 1);
            System.out.println("出栈元素: " + element);
        }
        // 输出栈是否为空
        System.out.println("栈是否为空: " + stack.isEmpty());
    }

    @Test
    public void listNodeDemo() {
        // 创建一个链表节点类
        class ListNode {
            final int val;
            ListNode next;

            ListNode(int x) {
                val = x;
                next = null;
            }
        }
        // 创建链表
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        // 遍历链表
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val + " ");
            current = current.next;
        }
        System.out.println();
    }


    @Test
    public void arrDemo() {
        // 数组示例
        int[] intArray = {1, 2, 3, 4, 5};
        String[] strArray = {"A", "B", "C"};
        System.out.println("整数数组: " + Arrays.toString(intArray));
        System.out.println("字符串数组: " + Arrays.toString(strArray));

        // 二维数组示例
        int[][] twoDArray = {{1, 2, 3}, {4, 5, 6}};
        System.out.println("二维数组: " + Arrays.deepToString(twoDArray));
    }

    @Test
    public void JavaDataType() {
        // Java基本类型
        String[] types = {"byte", "short", "int", "long", "float", "double", "char", "boolean"};
        System.out.println("Java基本类型:");
        for (String type : types) {
            System.out.println(type);
        }
    }

    @Test
    public void JavaType() {
        // Java常见引用类型示例
        String str = "Hello, world!";
        Integer integer = 123;
        Double dbl = 3.14;
        Boolean bool = true;
        int[] arr = {1, 2, 3};
        List<String> list = Arrays.asList("A", "B", "C");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        System.out.println("String: " + str);
        System.out.println("Integer: " + integer);
        System.out.println("Double: " + dbl);
        System.out.println("Boolean: " + bool);
        System.out.println("int[]: " + Arrays.toString(arr));
        System.out.println("List: " + list);
        System.out.println("Map: " + map);
    }

    @Test
    public void methodDemo() {
        // 无参无返回值方法
        sayHello();
        // 有参无返回值方法
        printSum(3, 5);
        // 有参有返回值方法
        int result = multiply(4, 6);
        System.out.println("multiply(4, 6) = " + result);
    }

    // 无参无返回值方法
    private void sayHello() {
        System.out.println("Hello, method!");
    }

    // 有参无返回值方法
    private void printSum(int a, int b) {
        System.out.println(a + " + " + b + " = " + (a + b));
    }

    // 有参有返回值方法
    private int multiply(int a, int b) {
        return a * b;
    }


    @Test
    public void JSONDemo() {
        // 构建一���包含多种类型的JSON对象
        Map<String, Object> json = new HashMap<>();
        json.put("name", "张三"); // 字符串
        json.put("age", 28); // 数字
        json.put("married", false); // 布尔
        json.put("scores", Arrays.asList(95, 88, 76)); // 数组
        Map<String, Object> address = new HashMap<>(); // 嵌套对象
        address.put("city", "北京");
        address.put("zip", "100000");
        json.put("address", address);
        // 输出JSON字符串
        System.out.println(JSON.toJSONString(json, JSONWriter.Feature.PrettyFormat));
    }


    // 有三个人从左到右排成一行。我们知道：
    //
    //         1. A比B高。
    //         2. B不是最高的。
    //         3. C不是最矮的。
    //
    // 问这三个人的身高顺序是什么？
    @Test
    public void testTaller() {
        // 三个人分别��A、B、C，假设他们从左到右排列
        // 用数���表示身高顺序，0:A, 1:B, 2:C
        // 身高从高到矮排列
        String[] persons = {"A", "B", "C"};
        // 枚举所有排列
        List<List<String>> orders = new ArrayList<>();
        permute(persons, 0, orders);
        for (List<String> order : orders) {
            int a = order.indexOf("A");
            int b = order.indexOf("B");
            int c = order.indexOf("C");
            // 1. A比B高 => A在B左边（索引小）
            if (a >= b) continue;
            // 2. B不是最高的 => B���在最左边
            if (b == 0) continue;
            // 3. C不是最矮的 => C不在最右边
            if (c == 2) continue;
            System.out.println("身高从高到矮排列: " + order);
        }
    }

    // 全排列辅助方法
    private void permute(String[] arr, int start, List<List<String>> result) {
        if (start == arr.length) {
            result.add(new ArrayList<>(Arrays.asList(arr)));
            return;
        }
        for (int i = start; i < arr.length; i++) {
            swap(arr, start, i);
            permute(arr, start + 1, result);
            swap(arr, start, i);
        }
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private void swap(String[] arr, int i, int j) {
        String tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
