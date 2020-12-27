package api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class represents a Directed (positive) Weighted Graph Theory Algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // JSON file
 * 6. Load(file); // JSON file
 */
public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph graph;
    HashMap<Integer, HashMap<Integer, node_data>> aba = new HashMap<>();

    /**
     * Initiate the graph.
     * @param g the directed_weighted_graph.
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.graph = g;
    }

    /**
     * Get a directed weighted graph.
     * @return directed_weighted_graph.
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * A deep copy that initiate the dw_graph_algorithms.
     * @return a deep copy of graph.
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph a = new DWGraph_DS(graph);
        return a;
    }

    /**
     * Check if the graph is connected or not, using BFS.
     */
    @Override
    public boolean isConnected() {
        if (this.graph.nodeSize() == 0 || this.graph.nodeSize() == 1) return true;
        Iterator<node_data> n1 = graph.getV().iterator();
        node_data n = n1.next();
        if (!bfs(n)) return false;
        while (n1.hasNext()) {
            node_data n2 = n1.next();
            if (!bfs(n2)) return false;
        }
        return true;
    }

    /**
     * Zeros all the vertices at the graph.
     *
     * @param c the Collection of all the node_data.
     */
    private static void setTagsToZero(Collection<node_data> c) {
        for (node_data n : c) {
            n.setTag(0);
        }
    }

    /**
     * The BFS goes all the node's graph and tag all of them zero
     * and start from a random vertex which we will start going over the graph,
     * tag the initial vertex one, that means that we passed of it but not finish with it, we put it in a queue.
     * While the queue isn't empty we will take out the first vertex that is there
     * and go over it's neighbors, we will put all it's neighbors in the queue and tag them one,
     * after we have finished going through all the neighbors of the initial vertex, we will tag
     * the same vertex in two because we finished with it and put all the neighbors in the queue,
     * So we will also go over the next vertex, we will put in all it's neighbors in the queue if
     * it's neighbors are zero and so on until we go on all the vertices.
     * @return true if all the vertices two.
     * else, return false.
     */
    private boolean bfs(node_data n) {
        Queue<node_data> q = new LinkedList<>();
        int size = graph.nodeSize();
        setTagsToZero(graph.getV());
        int counter = 0;
        q.add(n);
        n.setTag(1);
        while (!q.isEmpty()) {
            counter++;
            node_data m = q.poll();
            for (edge_data a : graph.getE(m.getKey())) {
                if (graph.getNode(a.getDest()).getTag() == 0) {
                    q.add(graph.getNode(a.getDest()));
                    graph.getNode(a.getDest()).setTag(1);
                }
            }
            m.setTag(2);
        }
        return (counter == size);
    }

    /**
     * Calculate the shortest path from node src to node dest, using in dijkstra algorithm,
     * the dijkstra algorithm set infinite all the nodes and the src it's set to zero and add to the Priority Queue.
     * Now, we do a for loop that over all the neighbors of this node, it's checks if the sum fo the tag of the node that
     * we over all it's neighbors plus the weight of the edge between it's neighbor to it small from the tag of the
     * neighbor, if it is, so we set the tag of the neighbor to the smallest tag' and so on until we find the
     * shortest path to the dest.
     * @param src start node
     * @param dest end (target) node
     * @return the shortest path from node src to node dest.
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (graph.nodeSize() == 0 || graph.getNode(src) == null || graph.getNode(dest) == null) return -1;
        if (src == dest) return 0;
        PriorityQueue<node_data> pq = new PriorityQueue<>(new Comparator<node_data>() {
            @Override
            public int compare(node_data o1, node_data o2) {
                if(o1.getKey() > o2.getKey()) return -1;
                return 1;
            }
        });
        HashMap<node_data, Boolean> visited = new HashMap<>();
        setWeightsToInfinite(graph.getV());
        node_data s = graph.getNode(src);
        s.setWeight(0);
        visited.put(s, true);
        pq.add(s);
        while (!pq.isEmpty()) {
            node_data min = pq.poll();
            for (edge_data e : graph.getE(min.getKey())) {
                node_data e1 = graph.getNode(e.getDest());
                aba.get(e1.getKey()).put(min.getKey(), min);
                if (!visited.containsKey(e1)) {
                    e1.setWeight(min.getWeight() + graph.getEdge(min.getKey(), e1.getKey()).getWeight());
                    pq.add(e1);
                    visited.put(e1, true);
                } else if (e1.getWeight() > min.getWeight() + graph.getEdge(min.getKey(), e1.getKey()).getWeight()) {
                    e1.setWeight(min.getWeight() + graph.getEdge(min.getKey(), e1.getKey()).getWeight());
                }
            }
        }
        return graph.getNode(dest).getWeight();
    }

    /**
     * Infinites all the vertices at the graph.
     *
     * @param c the Collection of the node_data.
     */
    private void setWeightsToInfinite(Collection<node_data> c) {
        for (node_data n : c) {
            n.setTag(Integer.MAX_VALUE);
            aba.put(n.getKey(), new HashMap<>());
        }
    }

    /**
     * Calculate the shortest path from node src to node dest, using in dijkstra algorithm,
     * the dijkstra algorithm set infinite all the nodes and the src it's set to zero and add to the Priority Queue.
     * Now, we do a for loop that over all the neighbors of this node, it's checks if the sum fo the tag of the node that
     * we over all it's neighbors plus the weight of the edge between it's neighbor to it small from the tag of the
     * neighbor, if it is, so we set the tag of the neighbor to the smallest tag and so on until we find the
     * shortest path to the dest and add the nodes (that we passed of them to get the shortest path) to the list.
     * @param src start node
     * @param dest end (target) node
     * @return the list of nodes that give us the shortest path.
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (graph.nodeSize() == 0 || graph.getNode(src) == null || graph.getNode(dest) == null) return null;
        shortestPathDist(src, dest);
        LinkedList<node_data> list = new LinkedList<>();
        if(src == dest) {
            list.add(graph.getNode(src));
            return list;
        }
        PriorityQueue<node_data> pq = new PriorityQueue<>(new Comparator<node_data>() {
            @Override
            public int compare(node_data o1, node_data o2) {
                if(o1.getKey() > o2.getKey()) return -1;
                return 1;            }
        });
        HashMap<Integer, node_data> parent = new HashMap<>();
        node_data d = graph.getNode(dest);
        pq.add(d);
        list.addFirst(d);
        while (!pq.isEmpty()) {
            node_data m = pq.poll();
            double min = m.getWeight();
            node_data s = null;
            for (int n2 : aba.get(m.getKey()).keySet()) {
                node_data n = graph.getNode(n2);
                if ((n.getWeight() + graph.getEdge(n.getKey(), m.getKey()).getWeight()) == m.getWeight()) {
                    min = n.getWeight();
                    s = n;
                }
            }
            if (list.contains(graph.getNode(src))) return list;
            if (s != null) {
                pq.add(s);
                list.addFirst(s);
            }
        }
        return list;
    }

    /**
     * Saves this weighted graph to the given file name, used Gson.
     * @param file the file name (may include a relative path).
     * @return true if the file is save.
     */
    @Override
    public boolean save(String file) {
        if (this.graph != null) {
            if (!file.contains(file + ".txt")) {
                Gson gson = new Gson();
                JsonObject json = new JsonObject();
                JsonArray edges = new JsonArray();
                for (node_data n : this.graph.getV()) {
                    for (edge_data e : this.graph.getE(n.getKey())) {
                        JsonObject currentEdge = new JsonObject();
                        currentEdge.addProperty("src", e.getSrc());
                        currentEdge.addProperty("w", e.getWeight());
                        currentEdge.addProperty("dest", e.getDest());
                        edges.add(currentEdge);
                    }
                }
                json.add("Edges", edges);

                JsonArray nodes = new JsonArray();
                for (node_data n : this.graph.getV()) {
                    JsonObject currentNode = new JsonObject();
                    if (n.getLocation() != null) {
                        String location = n.getLocation().x() + ", " + n.getLocation().y() + ", " + n.getLocation().z();
                        currentNode.addProperty("pos", location);
                    } else {
                        currentNode.addProperty("pos", "null");
                    }
                    currentNode.addProperty("id", n.getKey());
                    nodes.add(currentNode);
                }
                json.add("Nodes", nodes);
                try {
                    PrintWriter writer = new PrintWriter(new File(file));
                    writer.write(gson.toJson(json));
                    writer.flush();
                    writer.close();
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * This method load a graph to this graph algorithm, used Gson.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file file name
     * @return true if the file is load.
     */
    @Override
    public boolean load(String file) {
        if (!file.contains(file + ".txt")) {
            try {
                Gson gson = new Gson();
                JsonObject json = new JsonObject();
                this.graph = new DWGraph_DS();
                Type type = new TypeToken<JsonObject>() {
                }.getType();
                JsonReader reader = new JsonReader(new FileReader(file));
                JsonObject g = gson.fromJson(reader, type);
                JsonArray nodes = g.get("Nodes").getAsJsonArray();
                JsonArray edges = g.get("Edges").getAsJsonArray();
                for (JsonElement n : nodes) {
                    String[] s = n.getAsJsonObject().get("pos").getAsString().split(",");
                    node_data m = new NodeData(n.getAsJsonObject().get("id").getAsInt());
                    m.setLocation(new GeoLocation(Double.parseDouble(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2])));
                    this.graph.addNode(m);
                }

                for (JsonElement n : edges) {
                    this.graph.connect(n.getAsJsonObject().get("src").getAsInt(), n.getAsJsonObject().get("dest").getAsInt(), n.getAsJsonObject().get("w").getAsDouble());
                }
                init(this.graph);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
