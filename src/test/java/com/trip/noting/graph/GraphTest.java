package com.trip.noting.graph;

import org.junit.Test;

public class GraphTest {

    @Test
    public void DenseGraphTest() {
        DenseGraph denseGraph = new DenseGraph(5, false);
        denseGraph.addEdge(0, 2);
        denseGraph.addEdge(3, 4);
        denseGraph.show();
        System.out.println("------------");
        denseGraph.DFSTraverse();
        denseGraph.BFSTraverse();
    }


    @Test
    public void LinkedGraphTest() {
        LinkedGraph linkedGraph = new LinkedGraph(5, true);
        linkedGraph.addEdge(0, 2);
        linkedGraph.addEdge(4, 1);
        linkedGraph.addEdge(3, 4);
        linkedGraph.show();
    }

}
