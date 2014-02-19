package trip;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import graph.Graphs;
import graph.Graph;
import graph.Weighter;
import graph.Weighting;
import graph.Distancer;
import graph.DirectedGraph;
import graph.Iteration;

/** Reads and prints the path.
 *  @author Shin-Yen Huang
 */
public class Travels {

    /** Reads and prints MAPFILE. */
    public Travels(String mapFile) {
        _mapFile = mapFile;

    }

    /** Parses the input file. */
    public void parse() {
        try {
            Reader tripInput;
            tripInput = new FileReader(new File(_mapFile));
            Scanner input = new Scanner(tripInput);
            while (input.hasNext()) {
                String in = input.next();
                if (in.matches("R")) {
                    String s = input.next();
                    String r = input.next();
                    Float w = Float.parseFloat(input.next());
                    String d1 = direction(input.next());
                    String d2 = opposite(d1);
                    String end = input.next();
                    Distance dist =
                        new Distance(s, r, w, d1, end);
                    Distance dist2 =
                        new Distance(end, r, w, d2, s);
                    _dist.add(dist);
                    _dist.add(dist2);
                } else if (in.matches("L")) {
                    String name = input.next();
                    Float a = Float.parseFloat(input.next());
                    Float b = Float.parseFloat(input.next());
                    Location loca = new Location(name, a, b);
                    _loca.add(loca);
                }
            }
            build();
        } catch (IOException e) {
            System.err.print("Bad Input.");
            System.exit(1);
            return;
        } catch (NumberFormatException err) {
            System.err.print("Not a Float");
            System.exit(1);
            return;
        }
    }

    /** Returns the opposite of IN. */
    private String opposite(String in) {
        switch (in) {
        case "north":
            return "south";
        case "south":
            return "north";
        case "east":
            return "west";
        case "west":
            return "east";
        default:
            System.err.printf("not direction");
            System.exit(1);
            return "";
        }
    }

    /** Returns the direction of the IN path. */
    private String direction(String in) {
        switch (in) {
        case "SN":
            return "north";
        case "NS":
            return "south";
        case "EW":
            return "west";
        case "WE":
            return "east";
        default:
            System.err.printf("not a directio");
            System.exit(1);
            return "";
        }
    }

    /** Creates all the maps's paths and locations. */
    public void create() {
        Scanner inp = new Scanner(System.in);
        String delimiter = "[\\s,\\n\\r]+";
        inp.useDelimiter(delimiter);
        _vert = new ArrayList<Graph<Location, Distance>.Vertex>();
        while (inp.hasNext()) {
            Iteration<Graph<Location, Distance>.Vertex> vertex =
                _graph.vertices();
            String loc = inp.next();
            for (Graph<Location, Distance>.Vertex v : vertex) {
                if (v.getLabel().getName().equals(loc)) {
                    _vert.add(v);
                }
            }
        }
        if (_vert.size() >= 2) {
            System.out.printf("From %s:%n", _vert.get(0).getLabel().getName());
            System.out.println();
        } else {
            System.err.printf("There is no path.");
            System.exit(1);
        }
        while (_vert.size() >= 2) {
            List<Graph<Location, Distance>.Edge> roads =
                new ArrayList<Graph<Location, Distance>.Edge>();
            roads = Graphs.shortestPath(_graph, _vert.get(0),
                _vert.get(1), new Distancer<Location>() {
                    @Override
                    public double dist(Location x, Location y) {
                        double a = (x.getX() - y.getX())
                            * (x.getX() - y.getX());
                        double b = (x.getY() - y.getY())
                            * (x.getY() - y.getY());
                        return Math.sqrt(a + b);
                    }
                }, new Weighter<Location>() {
                    @Override
                    public void setWeight(Location l, double w) {
                        l.setWeight(w);
                    }
                    @Override
                    public double weight(Location l) {
                        return l.getWeight();
                    }
                }, new Weighting<Distance>() {
                    @Override
                    public double weight(Distance d) {
                        return d.getDist();
                    }
                });
            print(roads);
        }
    }

    /** Prints the all the ROADS. */
    public void print(List<Graph<Location, Distance>.Edge> roads)  {
        if (roads != null) {
            Float prevDist = 0.0f;
            for (int i = 1; i <= roads.size(); i += 1) {
                Distance prev = roads.get(i - 1).getLabel();
                if (roads.get(i - 1).getV1().equals(_vert.get(1))) {
                    counter += 1;
                    System.out.printf("%d. Take %s %s for %.1f miles",
                        counter, prev.getRoad(),
                        prev.getDirection(), prev.getDist());
                    System.out.printf(" to %s.",
                        _vert.get(1).getLabel().getName());
                    _vert.remove(0);
                    System.out.println();
                    break;
                }
                Distance curr = roads.get(i).getLabel();
                if (curr.getDirection().equals(prev.getDirection())
                    && curr.getRoad().equals(prev.getRoad())) {
                    prevDist = prev.getDist();
                    curr.setDist(curr.getDist() + prevDist);
                    continue;
                } else {
                    counter += 1;
                    System.out.printf("%d. Take %s %s for %.1f miles.%n",
                        counter, prev.getRoad(),
                        prev.getDirection(), prev.getDist());
                }
            }
        } else {
            System.err.printf("no path");
            System.exit(1);
        }
    }

    /** Creates the map with all locations and paths. */
    public void build() {
        _graph = new DirectedGraph<Location, Distance>();
        for (Location l : _loca) {
            _graph.add(l);
        }
        Iteration<Graph<Location, Distance>.Vertex> vSucc = _graph.vertices();
        List<Graph<Location, Distance>.Vertex> tempVert =
            new ArrayList<Graph<Location, Distance>.Vertex>();
        List<String> verticies = new ArrayList<String>();
        while (vSucc.hasNext()) {
            Graph<Location, Distance>.Vertex temp = vSucc.next();
            verticies.add(temp.getLabel().getName());
            tempVert.add(temp);
        }
        for (Distance d : _dist) {
            if (verticies.contains(d.getStart())
                && (verticies.contains(d.getDest()))) {
                Graph<Location, Distance>.Vertex x = null;
                Graph<Location, Distance>.Vertex y = null;
                for (Graph<Location, Distance>.Vertex v : tempVert) {
                    if (v.getLabel().getName().equals(d.getStart())) {
                        x = v;
                    } else if (v.getLabel().getName().equals(d.getDest())) {
                        y = v;
                    }
                }
                _graph.add(x, y, d);
            } else {
                System.err.printf("not a road");
                System.exit(1);
            }
        }
        create();
    }

    /** Name of the map file to be parsed. */
    private String _mapFile;

    /** Holds all locations of a _graph. */
    private List<Location> _loca = new ArrayList<Location>();

    /** Holds all paths of a _graph. */
    private List<Distance> _dist = new ArrayList<Distance>();

    /** Map with locations and paths. */
    private Graph<Location, Distance> _graph;

    /** List of locations on the _graph. */
    private List<Graph<Location, Distance>.Vertex> _vert;

    /** Number of solution paths. */
    private int counter;
}
