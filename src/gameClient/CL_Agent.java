package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Agent {
    public static final double EPS = 0.0001;
    private static int _count = 0;
    private static int _seed = 3331;
    private int _id;
    private Point3D _pos;
    private geo_location _pose;
    private double _speed;
    private edge_data _curr_edge;
    private node_data _curr_node;
    private directed_weighted_graph _gg;
    private CL_Pokemon _curr_fruit;
    private long _sg_dt;
    private double _value;
    private boolean avilable = true;

    /**
     * basic contsructor
     * @param g - graph
     * @param start_node - the start location of the agent
     */
    public CL_Agent(directed_weighted_graph g, int start_node) {
        _gg = g;
        setMoney(0);
        this._curr_node = _gg.getNode(start_node);
        _pose = _curr_node.getLocation();
        _id = -1;
        setSpeed(0);
    }

    /**
     *
     * @param json - string to read from json,
     *             make the agent
     */
    public void update(String json) {
        JSONObject line;
        try {
            //"GameServer":{"graph":"A0","pokemons":3,"agents":1}}
            line = new JSONObject(json);
            JSONObject ttt = line.getJSONObject("Agent");
            int id = ttt.getInt("id");
            if (id == this.getID() || this.getID() == -1) {
                if (this.getID() == -1) {
                    _id = id;
                }
                double speed = ttt.getDouble("speed");
                String p = ttt.getString("pos");
                Point3D pp = new Point3D(p);
                int src = ttt.getInt("src");
                int dest = ttt.getInt("dest");
                double value = ttt.getDouble("value");
                this._pos = pp;
                this.setCurrNode(src);
                this.setSpeed(speed);
                this.setNextNode(dest);
                this.setMoney(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return location of the edge the agent left from
     */
    public int getSrcNode() {
        return this._curr_node.getKey();
    }

    public String toJSON() {
        int d = this.getNextNode();
        String ans = "{\"Agent\":{"
                + "\"id\":" + this._id + ","
                + "\"value\":" + this._value + ","
                + "\"src\":" + this._curr_node.getKey() + ","
                + "\"dest\":" + d + ","
                + "\"speed\":" + this.getSpeed() + ","
                + "\"pos\":\"" + _pos.toString() + "\""
                + "}"
                + "}";
        return ans;
    }

    /**
     *
     * @param v - money of agent
     */
    private void setMoney(double v) {
        _value = v;
    }

    /**
     *
     * @param dest - node of the agent headed for
     * @return destination of the agnet
     */
    public boolean setNextNode(int dest) {
        boolean ans = false;
        int src = this._curr_node.getKey();
        this._curr_edge = _gg.getEdge(src, dest);
        if (_curr_edge != null) {
            ans = true;
        } else {
            _curr_edge = null;
        }
        return ans;
    }

    /**
     *
     * @param src - set current agent node
     */
    public void setCurrNode(int src) {
        this._curr_node = _gg.getNode(src);
    }

    /**
     *
     * @return if agent is moving over the graph
     */
    public boolean isMoving() {
        return this._curr_edge != null;
    }

    /**
     *
     * @return - to string in json
     */
    public String toString() {
        return toJSON();
    }


    public String toString1() {
        String ans = "" + this.getID() + "," + _pos + ", " + isMoving() + "," + this.getValue();
        return ans;
    }

    /**
     *
     * @return id number of the agent
     */
    public int getID() {
        // TODO Auto-generated method stub
        return this._id;
    }

    /**
     *
     * @return location of the agent
     */
    public geo_location getLocation() {
        // TODO Auto-generated method stub
        return _pos;
    }

    /**
     *
     * @return value after eating all pokemon
     */
    public double getValue() {
        // TODO Auto-generated method stub
        return this._value;
    }

    /**
     *
     * @return next node of the agent is headed for
     */
    public int getNextNode() {
        int ans = -2;
        if (this._curr_edge == null) {
            ans = -1;
        }
        else {
            ans = this._curr_edge.getDest();
        }
        return ans;
    }

    /**
     *
     * @return  speed of the agent
     */
    public double getSpeed() {
        return this._speed;
    }

    public void setSpeed(double v) {
        this._speed = v;
    }

    /**
     *
     * @return to which pokemon agent go
     */
    public CL_Pokemon get_curr_fruit() {
        return _curr_fruit;
    }

    /**
     *
     * @param curr_fruit - what is the agent current pokemon he headed for
     */
    public void set_curr_fruit(CL_Pokemon curr_fruit) {
        this._curr_fruit = curr_fruit;
    }

    public void set_SDT(long ddtt) {
        long ddt = ddtt;
        if (this._curr_edge != null) {
            double w = get_curr_edge().getWeight();
            geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
            geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();
            double de = src.distance(dest);
            double dist = _pos.distance(dest);
            if (this.get_curr_fruit().get_edge() == this.get_curr_edge()) {
                dist = _curr_fruit.getLocation().distance(this._pos);
            }
            double norm = dist / de;
            double dt = w * norm / this.getSpeed();
            ddt = (long) (1000.0 * dt);
        }
        this.set_sg_dt(ddt);
    }

    /**
     *
     * @return edge of the agent
     */
    public edge_data get_curr_edge() {
        return this._curr_edge;
    }

    public long get_sg_dt() {
        return _sg_dt;
    }

    public void set_sg_dt(long _sg_dt) {
        this._sg_dt = _sg_dt;
    }

    /**
     *
     * @return location of pokemon
     */
    public Point3D getLocation1() {
        return _pos;
    }

}