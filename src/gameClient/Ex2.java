package gameClient;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.*;
import api.game_service;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class Ex2 implements Runnable {
    private static MyFrame _win;
    private static Arena _ar;
    private static long dt = 80;
    private static int ID;
    private static int level;

    public static void main(String[] a) {
        if (a.length > 0) {
            try {
                ID = Integer.parseInt(a[0]);
            } catch (NumberFormatException e) {
                System.out.println("Try Again!");
                System.exit(0);
            }
            try {
                level = Integer.parseInt(a[1]);
            } catch (NumberFormatException e) {
                System.out.println("Try Again!");
                System.exit(0);
            }
        } else {
            ImageIcon open = new ImageIcon("./images/PuckmanPockimonTitle.png");
            try {
                ID = Integer.parseInt((String) JOptionPane.showInputDialog(null,
                        "Enter your ID number: ",
                        "Enter ID",
                        JOptionPane.QUESTION_MESSAGE,
                        open,
                        null,
                        ID));
            }
            catch (NumberFormatException e) {
                System.out.println("Try Again!");
                System.exit(0);
            }
            try {
                level = Integer.parseInt((String) JOptionPane.showInputDialog(
                        null,
                        "Select level: ",
                        "Select Level",
                        JOptionPane.QUESTION_MESSAGE,
                        open,
                        null,
                        level
                ));
            }
            catch (NumberFormatException e) {
                System.out.println("Try Again!");
                System.exit(0);
            }
        }
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(level); // you have [0,23] games
        game.login(ID);
        String g = game.getGraph();
        String pks = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        try {
            File f = new File("Graph.json");
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(g);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.load("Graph.json");
        directed_weighted_graph gg1 = ga.getGraph();
        init(game);
        game.startGame();
        _win.setTitle("Ex2 - OOP: Pokemon Game ");
        _win.setLevel(level);
        _win.setID(ID);
        int ind = 0;
        while (game.isRunning()) {
            if (game.timeToEnd() >= 0) {
                _win.setTimeToEnd(game.timeToEnd() / 10);
            }
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                    moveAgants(game, gg);
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen by calculate the path
     * and to which pokemon he most want to go to by using functions.
     * WhichPokeToGo2 - what pokemon should the agent go
     * goToPokemon - how the agent should go
     * nextNode - random choose edge
     *
     * @param game - the game we play in
     * @param gg   - directed weighted graph
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        _ar.getPokemons();
        int counter = 1;
        for (CL_Agent ag : log) {
            CL_Pokemon poke2 = WhichPokeToGo2(ffs, gg, ag.getSrcNode());
            poke2.setIsOnMe();
            if (ag.getNextNode() == -1) {
                int got = goToPokemon(poke2, gg, counter, ag);
                game.chooseNextEdge(ag.getID(), got);
                System.out.println("Agent: " + ag.getID() + "  turned to node: " + got);
            }
            if (ag.isMoving() == false) {
                int got = nextNode(gg, ag.getSrcNode());
                game.chooseNextEdge(ag.getID(), got);
                System.out.println("Agent: " + ag.getID() + "   turned to node: " + got);
            }
        }
    }

    /**
     * WhichPokeToGo2 is a method that run dijkstra algorithm to shortest path,
     * and by knowing the location of the agents and the value of the pokemon,
     * using to calculate the ratio by value of pokemon / shortest path.
     * to analyze which pokemon the agent will go.
     * the pokemon which is preferred to go will guarantied us more points at the end of the game
     *
     * @param ffs - list of pokemons
     * @param gg  - the graph
     * @param src - location of the agents
     * @return - the pokemon the agent go to
     */
    private static CL_Pokemon WhichPokeToGo2(List<CL_Pokemon> ffs, directed_weighted_graph gg, int src) {
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(gg);
        CL_Pokemon poke = ffs.get(0);
        for (CL_Pokemon poke1 : ffs) {
            if (poke1.isOnMe(poke1) == false) {
                poke = poke1;
            }
        }
        double way;
        double way1;
        if (poke.getType() > 0) {
            way = poke.getValue() * 100 / ga.shortestPathDist(src, poke.get_edge().getDest());
        } else {
            way = poke.getValue() * 100 / ga.shortestPathDist(src, poke.get_edge().getSrc());
        }
        for (CL_Pokemon poke1 : ffs) {
            if (poke1.isOnMe(poke1) == false) {
                if (poke.getType() > 0) {
                    way1 = poke.getValue() * 100 / ga.shortestPathDist(src, poke1.get_edge().getDest());
                } else {
                    way1 = poke.getValue() * 100 / ga.shortestPathDist(src, poke1.get_edge().getSrc());
                }
                if (way < way1) {
                    way = way1;
                    poke = poke1;
                }
            }
        }
        return poke;
    }

    /**
     * this method calculate the shortest route to the pokemon that a agent go to.
     * using dijkstra algorithm on a graph with several edges.
     * on each entry there is new calculate and return only the next node the agent will go.
     *
     * @param poke    the pokemon that the agent travels to
     * @param gg      the graph
     * @param counter the node on the list in the path to pokemon
     * @param ag      the agent
     * @return the dest for a current agent to go to
     */
    private static int goToPokemon(CL_Pokemon poke, directed_weighted_graph gg, int counter, CL_Agent ag) {
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(gg);
        int a = -1;
        int way = poke.get_edge().getDest();
        List<node_data> longWay = ga.shortestPath(ag.getSrcNode(), way);
        if(ag.getSpeed()< 2.2 ){dt=130;}
        else if(longWay.size() <=2){dt=85;}
        else{dt=102;}
        if (longWay.size() > counter) {
            a = longWay.get(counter).getKey();
            ag.set_curr_fruit(poke);
            ag.setNextNode(a);
            if (gg.getE(ag.getSrcNode()).contains(a)) {
                longWay = ga.shortestPath(ag.getSrcNode(), way);
                a = longWay.get(0).getKey();
            }
            return a;
        } else {
            ag.setNextNode(poke.get_edge().getSrc());
            return poke.get_edge().getSrc();
        }
    }

    /**
     * nextNode is a method that use to run the agents on the edges.
     * by doing a random choose to the node edges and chose 1 edge to go.
     *
     * @param g   - directed weighted graph
     * @param src - the agent start location
     * @return random edge destination
     */
    private static int nextNode(directed_weighted_graph g, int src) {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int) (Math.random() * s);
        int i = 0;
        while (i < r) {
            itr.next();
            i++;
        }
        ans = itr.next().getDest();
        return ans;
    }

    /**
     * in this init we basicly locate the all the pokemons and the agent in the graph
     *
     * @param game - initialize the game
     */
    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.setResizable(true);
        _win.update(_ar);

        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), gg);
            }
            for (int a = 0; a < rs; a++) {
                double b = 0;
                CL_Pokemon k = cl_fs.get(0);
                for (CL_Pokemon poke : cl_fs) {
                    if (poke.getValue() > b) {
                        b = poke.getValue();
                        k = poke;
                    }
                }
                int nn = k.get_edge().getDest();
                if (k.getType() < 0) {
                    nn = k.get_edge().getSrc();
                }
                game.addAgent(nn);
                cl_fs.remove(k);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}