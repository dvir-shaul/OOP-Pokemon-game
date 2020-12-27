package tests;

import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    private directed_weighted_graph graphDS;
    private dw_graph_algorithms graphAlg;
    private static int limit = 1000000;


    @BeforeEach
    public void Start() {
        graphDS = createGraph();
        graphAlg = new DWGraph_Algo();
        graphAlg.init(graphDS);
    }

    public directed_weighted_graph createGraph() {
        directed_weighted_graph graph = new DWGraph_DS();

        graph.addNode(new NodeData(0));
        graph.addNode(new NodeData(1));
        graph.addNode(new NodeData(2));
        graph.addNode(new NodeData(3));
        graph.addNode(new NodeData(22));
        graph.addNode(new NodeData(33));

        graph.connect(0, 1, 0.1);
        graph.connect(0, 22, 3.0);
        graph.connect(0, 2, 5.2);

        graph.connect(1, 22, 33.2);
        graph.connect(1, 3, 0.2);
        graph.connect(1, 2, 3.2);
        graph.connect(1, 33, 0.2);

        graph.connect(2, 22, 3.2);
        graph.connect(2, 3, 0.2);
        graph.connect(2, 2, 3.2);
        graph.connect(2, 33, 0.2);

        graph.connect(3, 11, 4.0);
        graph.connect(3, 1, 4.0);
        graph.connect(3, 0, 4.0);
        graph.connect(3, 33, 4.0);

        graph.connect(22, 33, 3.0);
        graph.connect(22, 2, 3.2);
        graph.connect(22, 0, 0.1);

        graph.connect(33, 22, 23.2);
        return graph;
    }

    @Test
    void init() {
        graphAlg.init(graphDS);
    }

    @Test
    void getGraph() {
        directed_weighted_graph graph2 = graphAlg.getGraph();
        Assertions.assertEquals(graph2, graphDS);
    }

    @Test
    void copy() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.connect(0, 1, 0.1);
        g.connect(1, 2, 2.1);
        g.connect(2, 3, 2.3);
        g.connect(0, 2, 4.5);
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(g);
        directed_weighted_graph go = ga.copy();
        Assertions.assertEquals(false, g.getNode(3) == go.getNode(3));
        Assertions.assertEquals(g.getEdge(0, 1), go.getEdge(0, 1));
    }

    @Test
    void copyOne() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(g);
        directed_weighted_graph go = ga.copy();
        Assertions.assertEquals(g.getNode(0).getKey() == go.getNode(0).getKey(), true);
        Assertions.assertEquals(g.getEdge(0, 1), go.getEdge(0, 1));
    }


    @Test
    void isConnected() {
        Assertions.assertEquals(true, graphAlg.isConnected());
    }

    @Test
    void NotIsConnected() {
        directed_weighted_graph graph1 = new DWGraph_DS();
        graph1.addNode(new NodeData(0));
        graph1.addNode(new NodeData(1));
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(graph1);
        Assertions.assertNotEquals(true, ga.isConnected());
    }


    @Test
    void shortestPathDist() {
        directed_weighted_graph graph = new DWGraph_DS();
        for(int i=0;i<1000000;++i){
            graph.addNode(new NodeData(i));
        }
        Random r = new Random(1);
        for(int i=0;i<100000;i++){
            graph.connect(i, i+1,r.nextDouble()*1000);
        }
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.init(graph);
        Assertions.assertEquals(544.5548088936737, algo.shortestPathDist(27, 28));
    }

    @Test
    void shortestPathDistToHimself() {
        Assertions.assertEquals(0, graphAlg.shortestPathDist(1, 1));
    }

    @Test
    void shortestPathDistToNull() {
        Assertions.assertEquals(-1, graphAlg.shortestPathDist(1, 25));
    }


    @Test
    void shortestPath() {
        LinkedList<node_data> node = new LinkedList<>();
        node.addFirst(graphDS.getNode(1));
        node.add(graphDS.getNode(2));
        node.add(graphDS.getNode(22));
        Assertions.assertEquals(6.4, graphAlg.shortestPathDist(1,22));
        Assertions.assertEquals(node, graphAlg.shortestPath(1, 22));

    }

    @Test
    void shortestPathToNull() {
        Assertions.assertEquals(null, graphAlg.shortestPath(1, 10));
    }

    @Test
    void shortestPathToHimself() {
        LinkedList<node_data> node = new LinkedList<>();
        node.addFirst(graphDS.getNode(1));
        Assertions.assertEquals(node, graphAlg.shortestPath(1, 1));
    }

    @Test
    void save() {
        try {
            graphAlg.save("file");
            Assertions.assertTrue(graphAlg.save("file"));
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Test
    void load() {
        graphAlg.save("file");
        dw_graph_algorithms loadGraph = new DWGraph_Algo();
        loadGraph.load("file");
        directed_weighted_graph graph1 = loadGraph.getGraph();
        Assertions.assertEquals(graphDS, graph1, "no same");
    }

    @Test
    public void bigGraph(){
        directed_weighted_graph graph = new DWGraph_DS();
        for(int i=0;i<1000000;++i){
            graph.addNode(new NodeData(i));
        }
        Random r = new Random(1);
        for(int i=0;i<100000;i++){
            graph.connect(i, i+1,r.nextDouble()*1000);
        }
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.init(graph);
        algo.save("TestSave.json");
        dw_graph_algorithms algo2 = new DWGraph_Algo();
        algo2.load("TestSave.json");
        Assertions.assertEquals(algo2.getGraph().edgeSize(), graph.edgeSize());
        Assertions.assertEquals(algo2.getGraph().nodeSize(), graph.nodeSize());
    }
}