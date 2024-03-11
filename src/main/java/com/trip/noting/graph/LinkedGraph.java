package com.trip.noting.graph;

import com.alibaba.fastjson2.JSON;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinkedGraph {
    // 节点的数量
    private final Integer nodeSize;
    // 边的数量
    private Integer edgeSize;
    // 是否为有向图
    private final Boolean isDirect;

    private final List<LinkedList<Boolean>> graph;

    // 遍历记录
    boolean[] visited;

    public LinkedGraph(int n, boolean isDirect) {
        if (n == 0) {
            throw new RuntimeException("the graph nodeSize can't be zero");
        }
        this.nodeSize = n;
        this.edgeSize = 0;
        this.isDirect = isDirect;
        this.graph = new ArrayList<>();
        for (int i = 0; i < nodeSize; i++) {
            LinkedList<Boolean> booleans = new LinkedList<>();
            for (int j = 0; j < nodeSize; j++) {
                booleans.add(false);
            }
            graph.add(booleans);
        }
        visited = new boolean[nodeSize];
    }

    public void addEdge(int x, int y) {
        if (x < 0 || x >= nodeSize) {
            throw new RuntimeException("the graph edge index x is illegal");
        }
        if (y < 0 || y >= nodeSize) {
            throw new RuntimeException("the graph edge index y is illegal");
        }
        if (hasEdge(x, y)) {
            return;
        }
        LinkedList<Boolean> booleans = graph.get(x);
        booleans.set(y, true);
        if (!isDirect) {
            graph.get(y).set(x, true);
        }
        this.edgeSize++;
    }

    public boolean hasEdge(int x, int y) {
        if (x < 0 || x >= nodeSize) {
            throw new RuntimeException("the graph edge index x is illegal");
        }
        if (y < 0 || y >= nodeSize) {
            throw new RuntimeException("the graph edge index y is illegal");
        }
        return graph.get(x).get(y);
    }

    public void show() {
        for (LinkedList<Boolean> booleans : graph) {
            System.out.println(JSON.toJSONString(booleans));
        }
    }
}
