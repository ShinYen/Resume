package graph;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;
/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular set of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visting it, and then adding its untraversed
 *  successors to the fringe.  The client can dictate an ordering on
 *  the fringe, determining which item is next removed, by which kind
 *  of traversal is requested.
 *     + A depth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at one end.  It also revisits the node
 *       itself after traversing all successors by calling the
 *       postVisit method on it.
 *     + A breadth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at different ends.  It also revisits the node
 *       itself after traversing all successors as for depth-first
 *       traversals.
 *     + A general traversal treats the fringe as an ordered set, as
 *       determined by a Comparator argument.  There is no postVisit
 *       for this type of traversal.
 *  As vertices are added to the fringe, the traversal calls a
 *  preVisit method on the vertex.
 *
 *  Generally, the client will extend Traversal, overriding the visit,
 *  preVisit, and postVisit methods, as desired (by default, they do nothing).
 *  Any of these methods may throw StopException to halt the traversal
 *  (temporarily, if desired).  The preVisit method may throw a
 *  RejectException to prevent a vertex from being added to the
 *  fringe, and the visit method may throw a RejectException to
 *  prevent its successors from being added to the fringe.
 *  @author Shin Yen Huang
 */
public class Traversal<VLabel, ELabel> {

    /** Perform a traversal of G over all vertices reachable from V.
     *  ORDER determines the ordering in which the fringe of
     *  untraversed vertices is visited.  The effect of specifying an
     *  ORDER whose results change as a result of modifications made during the
     *  traversal is undefined. */
    public void traverse(Graph<VLabel, ELabel> G,
                         Graph<VLabel, ELabel>.Vertex v,
                         Comparator<VLabel> order) {
        if (!_inorder) {
            final Comparator<VLabel> vertOrder = order;
            Comparator<Graph<VLabel, ELabel>.Vertex> a =
                new Comparator<Graph<VLabel, ELabel>.Vertex>() {
                    @Override
                    public int compare(Graph<VLabel, ELabel>.Vertex v1,
                                Graph<VLabel, ELabel>.Vertex v2) {
                        return vertOrder.compare(v1.getLabel(), v2.getLabel());
                    }
                };
            _travList = new TreeSet<Graph<VLabel, ELabel>.Vertex>(a);
        }
        _inorder = true;
        traverseHelp(G, v, order, _visited);
        _inorder = false;
        _visited.clear();
    }

    /** Takes in G V ORDER X, and traverses the graph. */
    private void traverseHelp(Graph<VLabel, ELabel> G,
                        Graph<VLabel, ELabel>.Vertex v,
                        Comparator<VLabel> order,
                        ArrayList<Graph<VLabel, ELabel>.Vertex> x) {
        try {
            x.add(v);
            visit(v);
            Iteration<Graph<VLabel, ELabel>.Vertex> iSucc = G.successors(v);
            Iteration<Graph<VLabel, ELabel>.Edge> succOut = G.outEdges(v);
            ArrayList<Graph<VLabel, ELabel>.Vertex> allSucc =
                new ArrayList<Graph<VLabel, ELabel>.Vertex>();
            while (iSucc.hasNext()) {
                Graph<VLabel, ELabel>.Vertex success = iSucc.next();
                allSucc.add(success);
                Graph<VLabel, ELabel>.Edge succE;
                for (Graph<VLabel, ELabel>.Edge e : succOut) {
                    if (!x.contains(e.getV1())) {
                        succE = e;
                        try {
                            preVisit(succE, e.getV0());
                        } catch (StopException  a) {
                            _finalVertex = v;
                            _graph = G;
                            _order = order;
                            return;
                        } catch (RejectException a) {
                            break;
                        }
                        _travList.add(e.getV0());
                        break;
                    }
                }
            }
            TreeSet<Graph<VLabel, ELabel>.Vertex> copy =
                new TreeSet<Graph<VLabel, ELabel>.Vertex>(_travList);
            while (_travList.size() != 0) {
                traverseHelp(G, _travList.pollFirst(), order, x);
            }
        } catch (StopException  e) {
            _finalVertex = v;
            _graph = G;
            _order = order;
            return;
        } catch (RejectException e) {
            _finalVertex = v;
            _graph = G;
        }
        postVisitCatch(v, G);
        _order = order;
    }

    /** postVisits V and G with catch blocks implemented. */
    private void postVisitCatch(Graph<VLabel, ELabel>.Vertex v,
                    Graph<VLabel, ELabel> G) {
        try {
            if (!_post.contains(v)) {
                postVisit(v);
                _post.add(v);
            }
        } catch (StopException d) {
            _finalVertex = v;
            _graph = G;
            return;
        } catch (RejectException d) {
            _finalVertex = v;
            _graph = G;
        }
    }

    /** Performs a depth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it or removed from it at one end in
     *  an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v) {
        _graph = G;
        _depthMode = true;
        depthHelp(G, v, _visited);
        _depthMode = false;
        _visited.clear();
    }

    /** takes in G V X is a helper for depth. */
    private void depthHelp(Graph<VLabel, ELabel> G,
                        Graph<VLabel, ELabel>.Vertex v,
                        ArrayList<Graph<VLabel, ELabel>.Vertex> x) {
        try {
            if (!x.contains(v)) {
                x.add(v);
                visit(v);
                Iteration<Graph<VLabel, ELabel>.Vertex> iSucc = G.successors(v);
                Iteration<Graph<VLabel, ELabel>.Edge> succOut = G.outEdges(v);
                ArrayList<Graph<VLabel, ELabel>.Vertex> allSucc =
                    new ArrayList<Graph<VLabel, ELabel>.Vertex>();
                while (iSucc.hasNext()) {
                    Graph<VLabel, ELabel>.Vertex success = iSucc.next();
                    allSucc.add(success);
                    Graph<VLabel, ELabel>.Edge succE;
                    for (Graph<VLabel, ELabel>.Edge e : succOut) {
                        if ((!x.contains(e.getV1()) && x.contains(e.getV0()))
                            || (!x.contains(e.getV0())
                            && x.contains(e.getV1()))) {
                            try {
                                preVisit(e, v);
                            } catch (StopException  a) {
                                _finalVertex = v;
                                _graph = G;
                                return;
                            } catch (RejectException a) {
                                break;
                            }
                            break;
                        }
                    }
                }
                for (Graph<VLabel, ELabel>.Vertex a : allSucc) {
                    if (!x.contains(a)) {
                        depthHelp(G, a, x);
                    }
                }
            }
        } catch (StopException  e) {
            _finalVertex = v;
            _graph = G;
            return;
        } catch (RejectException e) {
            _finalVertex = v;
            _graph = G;
        }
        postVisitCatch(v, G);
    }

    /** Performs a breadth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it at one end and removed from it at the
     *  other in an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void breadthFirstTraverse(Graph<VLabel, ELabel> G,
                                     Graph<VLabel, ELabel>.Vertex v) {
        _graph = G;
        _breadthMode = true;
        _queue.add(null);
        breadthHelp(G, v, _visited);
        _breadthMode = false;
        _visited.clear();
    }

    /** takes in G V X is a helper for breadth. */
    private void breadthHelp(Graph<VLabel, ELabel> G,
                            Graph<VLabel, ELabel>.Vertex v,
                            ArrayList<Graph<VLabel, ELabel>.Vertex> x) {
        if (x.contains(_queue.getFirst())) {
            postVisitCatch(_queue.remove(), G);
        } else {
            try {
                x.add(v);
                visit(v);
                Iteration<Graph<VLabel, ELabel>.Vertex> iSucc = G.successors(v);
                Iteration<Graph<VLabel, ELabel>.Edge> succOut = G.outEdges(v);
                ArrayList<Graph<VLabel, ELabel>.Vertex> allSucc =
                    new ArrayList<Graph<VLabel, ELabel>.Vertex>();
                while (iSucc.hasNext()) {
                    Graph<VLabel, ELabel>.Vertex success = iSucc.next();
                    allSucc.add(success);
                    Graph<VLabel, ELabel>.Edge succE;
                    for (Graph<VLabel, ELabel>.Edge e : succOut) {
                        if (!x.contains(e.getV1())) {
                            succE = e;
                            try {
                                preVisit(succE, e.getV0());
                            } catch (StopException  a) {
                                _finalVertex = v;
                                _graph = G;
                                return;
                            } catch (RejectException a) {
                                continue;
                            }
                            _queue.add(e.getV1());
                            allSucc.add(success);
                            break;
                        }
                    }
                }
                _queue.remove();
                _queue.add(v);
                LinkedList<Graph<VLabel, ELabel>.Vertex> copy =
                    new LinkedList<Graph<VLabel, ELabel>.Vertex>(_queue);
                while (_queue.size() != 0) {
                    breadthHelp(G, _queue.getFirst(), x);
                }
            } catch (StopException  e) {
                _finalVertex = v;
                _graph = G;
                return;
            } catch (RejectException e) {
                _finalVertex = v;
                _graph = G;
            }
        }
    }
    /** Continue the previous traversal starting from V.
     *  Continuing a traversal means that we do not traverse
     *  vertices that have been traversed previously. */
    public void continueTraversing(Graph<VLabel, ELabel>.Vertex v) {
        if (_breadthMode) {
            breadthFirstTraverse(_graph, _finalVertex);
        } else if (_depthMode) {
            depthFirstTraverse(_graph, _finalVertex);
        } else if (_inorder) {
            traverse(_graph, _finalVertex, _order);
        }
    }

    /** If the traversal ends prematurely, returns the Vertex argument to
     *  preVisit, visit, or postVisit that caused a Visit routine to
     *  return false.  Otherwise, returns null. */
    public Graph<VLabel, ELabel>.Vertex finalVertex() {
        return _finalVertex;
    }

    /** If the traversal ends prematurely, returns the Edge argument to
     *  preVisit that caused a Visit routine to return false. If it was not
     *  an edge that caused termination, returns null. */
    public Graph<VLabel, ELabel>.Edge finalEdge() {
        return _finalEdge;
    }

    /** Returns the last graph argument to a traverse routine, or null if none
     *  of these methods have been called. */
    protected Graph<VLabel, ELabel> theGraph() {
        return _graph;
    }

    /** Method to be called when adding the node at the other end of E from V0
     *  to the fringe. If this routine throws a StopException,
     *  the traversal ends.  If it throws a RejectException, the edge
     *  E is not traversed. The default does nothing.
     */
    protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                            Graph<VLabel, ELabel>.Vertex v0) {
    }

    /** Method to be called when visiting vertex V.  If this routine throws
     *  a StopException, the traversal ends.  If it throws a RejectException,
     *  successors of V do not get visited from V. The default does nothing. */
    protected void visit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** Method to be called immediately after finishing the traversal
     *  of successors of vertex V in pre- and post-order traversals.
     *  If this routine throws a StopException, the traversal ends.
     *  Throwing a RejectException has no effect. The default does nothing.
     */
    protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** The Vertex (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Vertex _finalVertex;
    /** The Edge (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Edge _finalEdge;
    /** The last graph traversed. */
    protected Graph<VLabel, ELabel> _graph;
    /** postvisited list of vertices. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _post =
        new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** visited verts. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _visited =
        new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** queue for breadth. */
    private LinkedList<Graph<VLabel, ELabel>.Vertex> _queue =
        new LinkedList<Graph<VLabel, ELabel>.Vertex>();
    /** treeSet for the order traversal. */
    private TreeSet<Graph<VLabel, ELabel>.Vertex> _travList
        = new TreeSet<Graph<VLabel, ELabel>.Vertex>();
    /** comparator for order traversal. */
    private Comparator<VLabel> _order;
    /** breadthmode boolean. */
    private boolean _breadthMode;
    /** depthMode boolean. */
    private boolean _depthMode;
    /** inorder boolean. */
    private boolean _inorder;
}
