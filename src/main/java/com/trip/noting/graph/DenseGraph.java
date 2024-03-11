package com.trip.noting.graph;

import lombok.Data;

import java.util.Arrays;
import java.util.LinkedList;

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

    // 遍历记录
    boolean[] visited;

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

    public void show() {
        for (boolean[] link : graph) {
            System.out.println(Arrays.toString(link));
        }
    }

    private void DFS(int i) {
        visited[i] = true;
        System.out.println(i + " -> ");
        for (int j = 0; j < nodeSize; j++) {
            // 节点相邻的节点如果没有被遍历，开始遍历相邻的节点
            if (graph[i][j] && !visited[j]) {
                DFS(j);
            }
        }
    }

    /**
     * 深度优先遍历（Depth First Search），也称深度优先搜索，简称为 DFS。
     * 遍历规则: 不断沿着节点的邻接结点的深度方向遍历，由于可能会有环，所以要记录遍历节点是否遍历过
     * 补充：遍历的目的是为了找到节点，而不是找到节点于节点之间的关系
     */
    public void DFSTraverse() {
        // 初始化所有的节点都未遍历
        for (int i = 0; i < nodeSize; i++) {
            visited[i] = false;
        }
        for (int i = 0; i < nodeSize; i++) {
            if (!visited[i]) {
                DFS(i);
            }
        }
    }


    /**
     * 广度优先遍历（Breadth First Search），又称为广度优先搜索，简称 BFS。
     * 遍历规则: 以起始顶点v为起点, 由近至远, 依次访问和v路径相同切路径长度为1,2,3…的顶点，类似于树的层次遍历
     */
    public void BFSTraverse() {
        int i, j;
        LinkedList<Integer> queue = new LinkedList<>();
        for (i = 0; i < nodeSize; i++) {
            visited[i] = false;
        }
        for (i = 0; i < nodeSize; i++) {
            if (!visited[i]) {
                visited[i] = true;
                System.out.println(i + " -> ");
                queue.addLast(i);
                while (!queue.isEmpty()) {
                    Integer removedIndex = queue.removeFirst();
                    for (j = 0; j < nodeSize; j++) {
                        if (graph[removedIndex][j] && !visited[j]) {
                            visited[j] = true;
                            System.out.println(j + " -> ");
                            queue.addLast(j);
                        }
                    }
                }
            }
        }
    }
}
