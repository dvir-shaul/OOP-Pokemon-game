package api;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EdgeData implements edge_data, Comparable<edge_data> {

    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    /**
     * Default constructor for new edge
     */
    public EdgeData() {
        this.src = 0;
        this.dest = 0;
        this.weight = 0.0;
        this.info = "";
        this.tag = 0;
    }

    public EdgeData(int s, int d,double w) {
        this.src = s;
        this.dest = d;
        this.weight = w;
        this.info = "";
        this.tag = 0;
    }

    /**
     * Constructor for new edge.
     * @param e
     */
    public EdgeData(edge_data e) {
        this.src = e.getSrc();
        this.dest = e.getDest();
        this.weight = e.getWeight();
        this.info = e.getInfo();
        this.tag = e.getTag();
    }

    /**
     * @return from which node the edge came from.
     */
    @Override
    public int getSrc() {
        return this.src;
    }

    /**
     * @return destination of the edge.
     */
    @Override
    public int getDest() {
        return this.dest;
    }

    /**
     * @return weight of the edge.
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * Change the weight of the edge.
     * @param e
     */
    public void setWeight(double e){
        this.weight = e;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     * @return the remark (meta data) associated with this edge.
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms.
     * @return the tag of the edge.
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public String toString(){
        return "" + this.weight;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeData edgeData = (EdgeData) o;
        return src == edgeData.src &&
                dest == edgeData.dest &&
                Double.compare(edgeData.weight, weight) == 0 &&
                tag == edgeData.tag &&
                Objects.equals(info, edgeData.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight, info, tag);
    }

    @Override
    public int compareTo(@NotNull edge_data o) {
        if(o.getWeight() > this.getWeight()) return -1;
        return 1;
    }
}
