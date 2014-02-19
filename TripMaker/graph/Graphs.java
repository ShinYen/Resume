package graph;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

/** Assorted graph algorithms.
 *  @author Shin-Yen Huang
 */
public final class Graphs {

    /* A* Search Algorithms */

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the edge weighter EWEIGHTER.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. D.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. D.dist(v, w) <= D.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, uses VWEIGHTER to set the weight of vertex v
     *  to the weight of a minimal path from V0 to v, for each v in
     *  the returned path and for each v such that
     *       minimum path length from V0 to v + D.dist(v, V1)
     *              < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.  If V1 is
     *  unreachable from V0, returns null and sets the minimum path weights of
     *  all reachable nodes.  The distance to a node unreachable from V0 is
     *  Double.POSITIVE_INFINITY. */
    public static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G, Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1, Distancer<? super VLabel> d,
                 Weighter<? super VLabel> vweighter,
                 Weighting<? super ELabel> eweighter) {
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Vertex>
            from = new HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Vertex>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Edge>
            edges = new HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> g =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        final Map<Graph<VLabel, ELabel>.Vertex, Double> f =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        ArrayList<Graph<VLabel, ELabel>.Vertex> closed =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> open =
            new PriorityQueue<Graph<VLabel, ELabel>.Vertex>(G.vertexSize(),
                new Comparator<Graph<VLabel, ELabel>.Vertex>() {
                    @Override
                    public int compare(Graph<VLabel, ELabel>.Vertex v1,
                        Graph<VLabel, ELabel>.Vertex v2) {
                        return Double.compare(f.get(v1), f.get(v2));
                    }
                });
        open.add(V0);
        g.put(V0, 0.0);
        f.put(V0, g.get(V0) + d.dist(V0.getLabel(), V1.getLabel()));
        while (!open.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex current = open.peek();
            if (current.equals(V1)) {
                return rebuild(V1, from, edges);
            }
            open.poll();
            closed.add(current);
            for (Graph<VLabel, ELabel>.Edge neighbor : G.outEdges(current)) {
                double tentativeG = g.get(current)
                    + eweighter.weight(neighbor.getLabel());
                double tentativeF = tentativeG
                    + d.dist(neighbor.getV(current).getLabel(), V1.getLabel());
                if (closed.contains(neighbor.getV1()) && tentativeF
                    >= f.get(neighbor.getV(current))) {
                    continue;
                } else if (!open.contains(neighbor.getV1())
                    || tentativeF < f.get(neighbor.getV(current))) {
                    from.put(neighbor.getV(current), current);
                    edges.put(neighbor.getV(current), neighbor);
                    g.put(neighbor.getV(current), tentativeG);
                    f.put(neighbor.getV(current), tentativeF);
                    vweighter.setWeight(neighbor.getV(current).getLabel(),
                        tentativeG);
                    if (!open.contains(neighbor.getV1())) {
                        open.add(neighbor.getV(current));
                    }
                }
            }
        }
        return null;
    }

    /** Returns the path from VERTS starting at V and getting
     *  edges from EDGES with types VLABEL and ELABEL. */
    private static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    rebuild(Graph<VLabel, ELabel>.Vertex v,
        Map<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Vertex> verts,
        Map<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Edge> edges) {
        List<Graph<VLabel, ELabel>.Edge> pFinal =
            new ArrayList<Graph<VLabel, ELabel>.Edge>();
        if (verts.containsKey(v)) {
            pFinal = rebuild(verts.get(v), verts, edges);
            pFinal.add(edges.get(v));
            return pFinal;
        } else {
            return pFinal;
        }
    }

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the weights of its edge labels. VLABEL and ELABEL are the types of
     *  vertex and edge labels. Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. D.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. D.dist(v, w) <= D.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, sets the weight of vertex v to the weight of
     *  a minimal path from V0 to v, for each v in the returned path
     *  and for each v such that
     *       minimum path length from V0 to v + D.dist(v, V1)
     *           < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.
     *
     *  This function has the same effect as the 6-argument version of
     *  shortestPath, but uses the .weight and .setWeight methods of
     *  the edges and vertices themselves to determine and set
     *  weights. If V1 is unreachable from V0, returns null and sets
     *  the minimum path weights of all reachable nodes.  The distance
     *  to a node unreachable from V0 is Double.POSITIVE_INFINITY. */
    public static <VLabel extends Weightable, ELabel extends Weighted>
    List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G, Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> d) {
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Vertex>
            from = new HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Vertex>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Edge>
            edges = new HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> g =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        final Map<Graph<VLabel, ELabel>.Vertex, Double> f =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        ArrayList<Graph<VLabel, ELabel>.Vertex> closed =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> open =
            new PriorityQueue<Graph<VLabel, ELabel>.Vertex>(G.vertexSize(),
                new Comparator<Graph<VLabel, ELabel>.Vertex>() {
                    @Override
                    public int compare(Graph<VLabel, ELabel>.Vertex v1,
                        Graph<VLabel, ELabel>.Vertex v2) {
                        return Double.compare(f.get(v1), f.get(v2));
                    }
                });
        open.add(V0);
        g.put(V0, 0.0);
        f.put(V0, g.get(V0) + d.dist(V0.getLabel(), V1.getLabel()));
        while (!open.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex current = open.peek();
            if (current.equals(V1)) {
                return rebuild(V1, from, edges);
            }
            open.poll();
            closed.add(current);
            for (Graph<VLabel, ELabel>.Edge neighbor : G.outEdges(current)) {
                Graph<VLabel, ELabel>.Vertex vert = neighbor.getV(current);
                double tentativeG = g.get(current)
                    + neighbor.getLabel().weight();
                double tentativeF = tentativeG
                    + d.dist(neighbor.getV(current).getLabel(), V1.getLabel());
                if (closed.contains(neighbor.getV1()) && tentativeF
                    >= f.get(neighbor.getV(current))) {
                    continue;
                } else if (!open.contains(neighbor.getV1())
                    || tentativeF < f.get(neighbor.getV(current))) {
                    from.put(neighbor.getV(current), current);
                    edges.put(neighbor.getV(current), neighbor);
                    g.put(neighbor.getV(current), tentativeG);
                    f.put(neighbor.getV(current), tentativeF);
                    vert.getLabel().setWeight(tentativeG);
                    if (!open.contains(neighbor.getV1())) {
                        open.add(neighbor.getV(current));
                    }
                }
            }
        }
        return null;
    }

    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };
}
