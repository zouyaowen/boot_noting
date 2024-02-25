package com.trip.noting.graph;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 稀疏图使用邻接表实现
 */
@Data
public class SparseGraph {
    // 节点的数量
    private Integer nodeSize;
    // 边的数量
    private Integer edgeSize;
    // 是否为有向图
    private Boolean isDirect;

    // 第一层数据为节点索引，第二层数据为第一层数据连接的其他节点索引
    private List<List<Integer>> graph = new ArrayList<>();

    public SparseGraph(int n, boolean isDirect) {
        this.nodeSize = n;
        this.edgeSize = 0;
        this.isDirect = isDirect;
        for (int i = 0; i < n; i++) {
            this.graph.add(new ArrayList<>());
        }
        graph.iterator();
    }

    public void addEdge(int x, int y) {
        if (x < 0 || x >= nodeSize) {
            throw new RuntimeException("the graph edge index x is illegal");
        }
        if (y < 0 || y >= nodeSize) {
            throw new RuntimeException("the graph edge index y is illegal");
        }
        // 此处应该判断平行边，但是时间复杂度最差是O(n),此处的另一种方案是不做处理，所有边的关系后续可以整体裁剪，不影响结果
        // 此处判断邻接表的平行变判断也是一个缺点，邻接表的优点是遍历邻边非常高效
        graph.get(x).add(y);
        if (x != y && !isDirect) {
            graph.get(y).add(x);
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
        for (int i = 0; i < graph.get(x).size(); i++) {
            if (graph.get(x).get(i) == y) {
                return true;
            }
        }
        return false;
    }

}
