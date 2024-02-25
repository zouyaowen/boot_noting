package com.trip.noting.graph;

import lombok.Data;

import java.util.Arrays;

/**
 * 稠密图使用邻接矩阵实现
 */
@Data
public class DenseGraph {
    // 节点的数量
    private final Integer nodeSize;
    // 边的数量
    private Integer edgeSize;
    // 是否为有向图
    private final Boolean isDirect;

    private final boolean[][] graph;

    public DenseGraph(int n, boolean isDirect) {
        if (n == 0) {
            throw new RuntimeException("the graph nodeSize can't be zero");
        }
        this.nodeSize = n;
        this.edgeSize = 0;
        this.isDirect = isDirect;
        this.graph = new boolean[n][n];
        for (boolean[] graphRow : this.graph) {
            Arrays.fill(graphRow, false);
        }
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
        graph[x][y] = true;
        if (!isDirect) {
            graph[y][x] = true;
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
        return graph[x][y];
    }
}
