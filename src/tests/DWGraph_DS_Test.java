package tests;
import api.*;
import jdk.swing.interop.SwingInterOpUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class DWGraph_DS_Test {
    private static Random rnd = null;
    private static int errors = 0, tests = 0, numberException = 0;
    private static String log = "";

    @Test
    void nodeSize(){
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.addNode(new NodeData(4));
        g.removeNode(1);
        g.removeNode(3);
        g.removeNode(1);
        Assertions.assertEquals(2, g.nodeSize());
        Assertions.assertEquals(6, g.getMC());
    }

    @Test
    void edgeSize(){
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.connect(0,1,0.1);
        g.connect(0,2,3.0);
        g.connect(1,2,3.2);
        g.connect(1,3,0.2);
        g.connect(2,3,2.0);
        g.connect(3,0,12.0);
        g.removeNode(0);
        g.removeNode(1);
        Assertions.assertEquals(1,g.edgeSize());
        Assertions.assertEquals(17, g.getMC());
    }

    @Test
    void getV() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        for (node_data n : g.getV()) {
            Assertions.assertNotNull(n.getKey());
            Assertions.assertEquals(g.getV().size(),4 );
        }
        Assertions.assertEquals(4, g.getMC());
    }
    @Test
    void getE() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.connect(0, 2, 2.4);
        g.connect(0, 3, 1.53);
        g.connect(1, 3, 3.4);
        g.connect(2, 1, 5.0);
        for (edge_data e : g.getE(0)) {
            Assertions.assertNotNull(e.getSrc());
            Assertions.assertEquals(g.getE(0).size(), 2);
        }
        Assertions.assertEquals(8, g.getMC());
    }

    @Test
    void connect() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.connect(0, 2, 1.2);
        g.connect(0, 1, 2.3);
        g.connect(1, 3, 3.1);
        g.connect(3, 2, 0.3);
        g.removeEdge(0, 1);
        g.removeEdge(2, 1);
        g.removeEdge(0, 2);
        g.connect(0, 3, 1.56);
        Assertions.assertEquals(1,g.getE(1).size());
        Assertions.assertEquals(11, g.getMC());
    }

    @Test
    void removeNode() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.connect(0, 2, 1.2);
        g.connect(0, 1, 2.1);
        g.connect(1, 3, 3.3);
        g.connect(0, 3, 3.4);
        g.removeNode(4);
        g.removeNode(0);
        int v = g.nodeSize();
        Assertions.assertEquals(3, v);
        Assertions.assertEquals(12, g.getMC());
    }

    @Test
    void removeEdge() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.connect(0, 1, 1.2);
        g.connect(0, 2, 2.4);
        g.connect(2, 3, 3.2);
        g.connect(2, 1, 4.3);
        g.removeEdge(0, 3);
        g.removeEdge(1, 2);
        edge_data w = g.getEdge(0, 3);
        Assertions.assertEquals(w, null);
        Assertions.assertEquals(2,g.getE(2).size());
        Assertions.assertEquals(8, g.getMC());
    }
    @Test
    void getEdge(){
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.connect(0,1,2.3);
        g.connect(1,2,3.4);
        g.connect(2,3,0.1);
        Assertions.assertEquals(1, g.getE(0).size());
        Assertions.assertEquals(1,g.getE(2).size());
        Assertions.assertEquals(7, g.getMC());
    }
    @Test
    void getMC(){
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.connect(0,1,1.2);
        g.connect(1,2,4.5);
        g.removeEdge(1,2);
        g.removeEdge(0,1);
        g.removeNode(1);
        double w = g.getMC();
        Assertions.assertEquals(w,9.0);
    }

    @Test
    void more(){
        directed_weighted_graph graph = new DWGraph_DS();
        graph.addNode(new NodeData(0));
        graph.addNode(new NodeData(1));
        graph.addNode(new NodeData(2));
        graph.addNode(new NodeData(3));
        graph.addNode(new NodeData(22));
        graph.addNode(new NodeData(33));

        graph.connect(0,1,0.1);
        graph.connect(0,22,3.0);
        graph.connect(0,2,5.2);

        graph.connect(1,22,33.2);
        graph.connect(1,3,0.2);
        graph.connect(1,2,3.2);
        graph.connect(1,33,0.2);

        graph.connect(2,22,3.2);
        graph.connect(2,3,0.2);
        graph.connect(2,2,3.2);
        graph.connect(2,33,0.2);

        graph.connect(3,11,4.0);
        graph.connect(3,1,4.0);
        graph.connect(3,0,4.0);
        graph.connect(3,33,4.0);

        graph.connect(22,33,3.0);
        graph.connect(22,2,3.2);
        graph.connect(22,0,0.1);

        graph.connect(33,22,23.2);
        System.out.println(graph);
        Assertions.assertEquals(24, graph.getMC());
    }
}
