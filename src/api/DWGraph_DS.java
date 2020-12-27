package api;


import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class DWGraph_DS implements directed_weighted_graph {

    private HashMap<Integer, node_data> vertices;
    private HashMap<Integer, HashMap<Integer, edge_data>> edges;
    private HashMap<Integer, HashMap<Integer, edge_data>> neighbours;
    private int nodeSize;
    private int edgeSize;
    private int mC;

    /**
     * Constructor.
     */
    public DWGraph_DS() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        this.neighbours = new HashMap<>();
        this.nodeSize = 0;
        this.edgeSize = 0;
        this.mC = 0;
    }

    /**
     * Deep copy to directed_weighted_graph.
     *
     * @param g the directed_weighted_graph.
     */
    public DWGraph_DS(directed_weighted_graph g) {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
        this.neighbours = new HashMap<>();
        for (node_data n : g.getV()) {
            node_data t = new NodeData(n.getKey());
            this.vertices.put(t.getKey(), t);
            this.edges.put(t.getKey(), new HashMap<>());
            this.neighbours.put(t.getKey(), new HashMap<>());
        }
        for (node_data n : g.getV()) {
            for (edge_data m : g.getE(n.getKey())) {
                this.connect(n.getKey(), m.getDest(), g.getEdge(n.getKey(), m.getDest()).getWeight());
            }
        }
        this.nodeSize = g.nodeSize();
        this.edgeSize = g.edgeSize();
        this.mC = g.getMC();
    }

    /**
     * returns the node_data by the node_id.
     *
     * @param key the node_id.
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return this.vertices.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src the src of the edge.
     * @param dest the dest if the edge.
     * @return the data of the edge.
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        try {
            return this.edges.get(src).get(dest);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n the node_data.
     */
    @Override
    public void addNode(node_data n) {
        if (!this.vertices.containsKey(n.getKey())) {
            this.vertices.put(n.getKey(), new NodeData(n));
            this.edges.put(n.getKey(), new HashMap<>());
            this.neighbours.put(n.getKey(), new HashMap<>());
            this.nodeSize++;
            this.mC++;
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * Note: this method should run in O(1) time.
     *
     * @param src the source of the edge.
     * @param dest the destination of the edge.
     * @param w positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (this.vertices.containsKey(src) && this.vertices.containsKey(dest)) {
            if(this.getEdge(src, dest) != null){
                this.getEdge(src, dest);
                ++mC;
            }
            if (this.getEdge(src, dest) == null) {
                edge_data e = new EdgeData(src, dest, w);
                this.edges.get(src).put(dest, new EdgeData(e));
                this.neighbours.get(dest).put(src, new EdgeData(e));
                edgeSize++;
                ++mC;
            }
        }
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<node_data>.
     */
    @Override
    public Collection<node_data> getV() {
        return this.vertices.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @param node_id the node_id in the edges hashmap.
     * @return Collection<edge_data>.
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (this.edges.containsKey(node_id))
            return this.edges.get(node_id).values();
        return null;
    }


    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key the node_data.
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if (this.vertices.containsKey(key)) {
            node_data n = this.getNode(key);
            int[] arr = new int[getE(key).size()];
            int i = 0;
            for (edge_data k : getE(key)) { //array of every edge has from node
                arr[i] = k.getDest();
                i++;
            }
            for (int j = 0; j < arr.length; j++) {
                this.removeEdge(key, arr[j]);
            }
            for ( Integer k : this.neighbours.get((key)).keySet()) {
                if (  k != null ) {
                    removeEdge(k, key);
                }
            }

            /**  int[] arr2 = new int[getV().size()];
             int count = 0;
             for (node_data node : getV()) {
             if (node != null) {
             if (this.edges.get(node.getKey()).keySet().contains(key) == true) {
             arr2[count] = node.getKey(); //array of every edge that come to node
             count++;
             }
             }
             }
             for (int j = 0; j < count; j++) {
             removeEdge(arr2[j], key);
             }*/
            this.edges.remove(key);
            this.vertices.remove(key);
            ++this.mC;
            nodeSize--;
            return n;
        }
        return null;
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src the src of the edge.
     * @param dest the dest f the edge.
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        edge_data e = this.getEdge(src, dest);
        if (e != null) {
            this.edges.get(src).remove(dest);
            this.edgeSize--;
            ++this.mC;
        }
        return null;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return the number of nodes.
     */
    @Override
    public int nodeSize() {
        return this.nodeSize;
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return the number of edges.
     */
    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return the Mode Count - for testing changes in the graph.
     */
    @Override
    public int getMC() {
        return this.mC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        return (nodeSize == that.nodeSize &&
                edgeSize == that.edgeSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeSize, edgeSize);
    }

    public String toString() {
        String v = vertices.toString();
        String e = edges.toString();
        return v + e;
    }
}