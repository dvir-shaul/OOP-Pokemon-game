package api;

import java.util.Objects;

public class NodeData implements node_data, Comparable<node_data> {

    private int key;
    private geo_location location;
    private double weight;
    private String info;
    private int tag;

    /**
     * default constructor
     *
     * @param key the key of the node_data.
     */
    public NodeData(int key) {
        this.key = key;
        this.location = null;
        this.weight = 0.0;
        this.info = "";
        this.tag = 0;
    }

    /**
     * constructor for new node
     *
     * @param n node_data.
     */
    public NodeData(node_data n) {
        this.key = n.getKey();
        this.location = new GeoLocation(n.getLocation());
        this.weight = n.getWeight();
        this.info = n.getInfo();
        this.tag = n.getTag();
    }

    /**
     * @return key of node data
     */
    @Override
    public int getKey() {
        return this.key;
    }

    /**
     * @return location of node
     */
    @Override
    public geo_location getLocation() {
        return this.location;
    }

    /**
     * new location of point
     *
     * @param p new location (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.location = p;
    }

    /**
     * @return the weight associated with this node.
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * @param w the new weight
     *          change node weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    /**
     * @return info of node
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * change node info
     *
     * @param s change the info of the node_data.
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * @return tag of node
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * change node tag
     *
     * @param t the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public String toString() {
        return "" + key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return key == nodeData.key &&
                Double.compare(nodeData.weight, weight) == 0 &&
                tag == nodeData.tag &&
                location.equals(nodeData.location) &&
                info.equals(nodeData.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, location, weight, info, tag);
    }

    @Override
    public int compareTo(node_data o) {
        if (o.getWeight() > this.getWeight()) return -1;
        return 1;
    }
}
