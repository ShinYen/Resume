package graph;

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;
import java.util.ArrayList;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your graph package per se (that is, it must be
 * possible to remove them and still have your package work). */

/** Unit tests for the graph package.
 *  @author
 */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(graph.Testing.class));
        System.exit(textui.runClasses(graph.Testing.class));
    }

    @Test
    public void emptyGraph() {
        DirectedGraph<String, String> g1 = new DirectedGraph<String, String>();
        UndirectedGraph<String, String> g2 =
            new UndirectedGraph<String, String>();
        assertEquals("Initial graph has vertices", 0, g1.vertexSize());
        assertEquals("Initial graph has edges", 0, g1.edgeSize());
        assertEquals("Initial graph has vertices", 0, g2.vertexSize());
        assertEquals("Initial graph has edges", 0, g2.edgeSize());
    }
    @Test
    public void directedGraphTest() {
        DirectedGraph<String, String> g1 = new DirectedGraph<String, String>();
        DirectedGraph<String, String>.Vertex v0 = g1.add("v0");
        DirectedGraph<String, String>.Vertex v1 = g1.add("v1");
        DirectedGraph<String, String>.Vertex v2 = g1.add("v2");
        DirectedGraph<String, String>.Vertex v3 = g1.add("v3");
        DirectedGraph<String, String>.Edge e0 = g1.add(v0, v1, "e0");
        DirectedGraph<String, String>.Edge e1 = g1.add(v1, v2, "e1");
        DirectedGraph<String, String>.Edge e2 = g1.add(v2, v3, "e2");
        DirectedGraph<String, String>.Edge e3 = g1.add(v1, v3, "e3");
        DirectedGraph<String, String>.Edge e4 = g1.add(v2, v0, "e4");
        assertEquals("Graph has vertices", 4, g1.vertexSize());
        assertEquals("Graph has edges", 5, g1.edgeSize());
        assertEquals("Vertex v0 has outdegrees", 1, g1.outDegree(v0));
        assertEquals("Vertex v1 has outdegrees", 2, g1.outDegree(v1));
        assertEquals("Vertex v2 has outdegrees", 2, g1.outDegree(v2));
        assertEquals("Vertex v3 has outdegrees", 0, g1.outDegree(v3));
        assertEquals("Vertex v0 has indegrees", 1, g1.inDegree(v0));
        assertEquals("Vertex v1 has indegrees", 1, g1.inDegree(v1));
        assertEquals("Vertex v2 has indegrees", 1, g1.inDegree(v2));
        assertEquals("Vertex v3 has indegrees", 2, g1.inDegree(v3));
        assertEquals("Vertex contains", true, g1.contains(v0, v1));
        assertEquals("Vertex does not contain", false, g1.contains(v1, v0));
        assertEquals("Vertex contains", true, g1.contains(v1, v3, "e3"));
    }
    @Test
    public void directedGraphTest2() {
        DirectedGraph<String, String> g1 = new DirectedGraph<String, String>();
        DirectedGraph<String, String>.Vertex v0 = g1.add("v0");
        DirectedGraph<String, String>.Vertex v1 = g1.add("v1");
        DirectedGraph<String, String>.Vertex v2 = g1.add("v2");
        DirectedGraph<String, String>.Vertex v3 = g1.add("v3");
        DirectedGraph<String, String>.Edge e0 = g1.add(v0, v1, "e0");
        DirectedGraph<String, String>.Edge e1 = g1.add(v1, v2, "e1");
        DirectedGraph<String, String>.Edge e2 = g1.add(v2, v3, "e2");
        DirectedGraph<String, String>.Edge e3 = g1.add(v1, v3, "e3");
        DirectedGraph<String, String>.Edge e4 = g1.add(v2, v0, "e4");
        g1.remove(v1);
        assertEquals("Graph has vertices", 3, g1.vertexSize());
        assertEquals("Graph has edges", 2, g1.edgeSize());
        assertEquals("Vertex v0 has outdegrees", 0, g1.outDegree(v0));
        assertEquals("Vertex v2 has outdegrees", 2, g1.outDegree(v2));
        assertEquals("Vertex v3 has outdegrees", 0, g1.outDegree(v3));
        assertEquals("Vertex v0 has indegrees", 1, g1.inDegree(v0));
        assertEquals("Vertex v2 has indegrees", 0, g1.inDegree(v2));
        assertEquals("Vertex v3 has indegrees", 1, g1.inDegree(v3));
        g1.remove(v2, v3);
        assertEquals("Graph has vertices", 3, g1.vertexSize());
        assertEquals("Graph has edges", 1, g1.edgeSize());
        assertEquals("Vertex v0 has outdegrees", 0, g1.outDegree(v0));
        assertEquals("Vertex v0 has indegrees", 1, g1.inDegree(v0));
        assertEquals("Vertex v2 has outdegrees", 1, g1.outDegree(v2));
        assertEquals("Vertex v2 has indegrees", 0, g1.inDegree(v2));
        assertEquals("Vertex v3 has outdegrees", 0, g1.outDegree(v3));
        assertEquals("Vertex v3 has indegrees", 0, g1.inDegree(v3));
    }
    @Test
    public void undirectedGraphTest() {
        UndirectedGraph<String, String> g2 =
            new UndirectedGraph<String, String>();
        UndirectedGraph<String, String>.Vertex v0 = g2.add("v0");
        UndirectedGraph<String, String>.Vertex v1 = g2.add("v1");
        UndirectedGraph<String, String>.Vertex v2 = g2.add("v2");
        UndirectedGraph<String, String>.Vertex v3 = g2.add("v3");
        UndirectedGraph<String, String>.Edge e0 = g2.add(v0, v1, "e0");
        UndirectedGraph<String, String>.Edge e1 = g2.add(v1, v2, "e1");
        UndirectedGraph<String, String>.Edge e2 = g2.add(v2, v3, "e2");
        UndirectedGraph<String, String>.Edge e3 = g2.add(v1, v3, "e3");
        UndirectedGraph<String, String>.Edge e4 = g2.add(v2, v0, "e4");
        assertEquals("Graph has vertices", 4, g2.vertexSize());
        assertEquals("Graph has edges", 5, g2.edgeSize());
        assertEquals("Vertex v0 has edges", 2, g2.degree(v0));
        assertEquals("Vertex v1 has edges", 3, g2.degree(v1));
        assertEquals("Vertex v2 has edges", 3, g2.degree(v2));
        assertEquals("Vertex v3 has edges", 2, g2.degree(v3));
        assertEquals("Vertex contains", true, g2.contains(v0, v1));
        assertEquals("Vertex contains", true, g2.contains(v1, v0));
        assertEquals("Vertex contains", true, g2.contains(v1, v3, "e3"));
        assertEquals("Vertex does not contain", false, g2.contains(v0, v3));
        assertEquals("Vertex contains", true, g2.contains(v3, v2));
    }
    @Test
    public void undirectedGraphTest2() {
        UndirectedGraph<String, String> g2 =
            new UndirectedGraph<String, String>();
        UndirectedGraph<String, String>.Vertex v0 = g2.add("v0");
        UndirectedGraph<String, String>.Vertex v1 = g2.add("v1");
        UndirectedGraph<String, String>.Vertex v2 = g2.add("v2");
        UndirectedGraph<String, String>.Vertex v3 = g2.add("v3");
        UndirectedGraph<String, String>.Edge e0 = g2.add(v0, v1, "e0");
        UndirectedGraph<String, String>.Edge e1 = g2.add(v1, v2, "e1");
        UndirectedGraph<String, String>.Edge e2 = g2.add(v2, v3, "e2");
        UndirectedGraph<String, String>.Edge e3 = g2.add(v1, v3, "e3");
        UndirectedGraph<String, String>.Edge e4 = g2.add(v2, v0, "e4");
        UndirectedGraph<String, String>.Edge e5 = g2.add(v0, v0, "e5");
        assertEquals("Vertex contains", true, g2.contains(v0, v0));
        g2.remove(e5);
        assertEquals("Vertex contains", false, g2.contains(v0, v0));
        g2.remove(v2);
        assertEquals("Graph has vertices", 3, g2.vertexSize());
        assertEquals("Graph has edges", 2, g2.edgeSize());
        assertEquals("Vertex v0 has edges", 1, g2.degree(v0));
        assertEquals("Vertex v1 has edges", 2, g2.degree(v1));
        assertEquals("Vertex v3 has edges", 1, g2.degree(v3));
        g2.remove(v0, v1);
        assertEquals("Graph has vertices", 3, g2.vertexSize());
        assertEquals("Graph has edges", 1, g2.edgeSize());
        assertEquals("Vertex v0 has edges", 0, g2.degree(v0));
        assertEquals("Vertex v1 has edges", 1, g2.degree(v1));
        assertEquals("Vertex v3 has edges", 1, g2.degree(v3));
        g2.remove(e3);
        assertEquals("Graph has vertices", 3, g2.vertexSize());
        assertEquals("Graph has edges", 0, g2.edgeSize());
    }

    @Test
    public void traversalTest() {
        UndirectedGraph<String, String> g2 =
            new UndirectedGraph<String, String>();
        VerboseTraversal<String, String> vt =
            new VerboseTraversal<String, String>();
        UndirectedGraph<String, String>.Vertex v0 = g2.add("A");
        UndirectedGraph<String, String>.Vertex v1 = g2.add("B");
        UndirectedGraph<String, String>.Vertex v2 = g2.add("C");
        UndirectedGraph<String, String>.Vertex v3 = g2.add("D");
        UndirectedGraph<String, String>.Vertex v4 = g2.add("E");
        UndirectedGraph<String, String>.Vertex v5 = g2.add("F");
        UndirectedGraph<String, String>.Vertex v6 = g2.add("G");
        UndirectedGraph<String, String>.Edge e0 = g2.add(v0, v1, "e0");
        UndirectedGraph<String, String>.Edge e1 = g2.add(v0, v2, "e1");
        UndirectedGraph<String, String>.Edge e2 = g2.add(v1, v2, "e2");
        UndirectedGraph<String, String>.Edge e3 = g2.add(v2, v3, "e3");
        UndirectedGraph<String, String>.Edge e4 = g2.add(v6, v5, "e4");
        UndirectedGraph<String, String>.Edge e5 = g2.add(v2, v6, "e5");
        UndirectedGraph<String, String>.Edge e6 = g2.add(v4, v3, "e6");
        UndirectedGraph<String, String>.Edge e7 = g2.add(v3, v1, "e7");
    }

    @Test
    public void undSuccessorTest() {
        UndirectedGraph<String, String> g2 =
            new UndirectedGraph<String, String>();
        UndirectedGraph<String, String>.Vertex v0 = g2.add("A");
        UndirectedGraph<String, String>.Vertex v1 = g2.add("B");
        UndirectedGraph<String, String>.Vertex v2 = g2.add("C");
        UndirectedGraph<String, String>.Vertex v3 = g2.add("D");
        UndirectedGraph<String, String>.Vertex v4 = g2.add("E");
        UndirectedGraph<String, String>.Vertex v5 = g2.add("F");
        UndirectedGraph<String, String>.Vertex v6 = g2.add("G");
        UndirectedGraph<String, String>.Edge e0 = g2.add(v0, v1, "e0");
        UndirectedGraph<String, String>.Edge e1 = g2.add(v0, v2, "e1");
        UndirectedGraph<String, String>.Edge e2 = g2.add(v1, v2, "e2");
        UndirectedGraph<String, String>.Edge e3 = g2.add(v2, v3, "e3");
        UndirectedGraph<String, String>.Edge e4 = g2.add(v6, v5, "e4");
        UndirectedGraph<String, String>.Edge e5 = g2.add(v2, v6, "e5");
        UndirectedGraph<String, String>.Edge e6 = g2.add(v4, v3, "e6");
        UndirectedGraph<String, String>.Edge e7 = g2.add(v3, v1, "e7");
        Iteration<Graph<String, String>.Vertex> bSucc = g2.successors(v1);
        ArrayList<Graph<String, String>.Vertex> containment =
            new ArrayList<Graph<String, String>.Vertex>();
        for (Graph<String, String>.Vertex vert : bSucc) {
            containment.add(vert);
        }
        assertEquals("Graph succ v0", true, containment.contains(v0));
        assertEquals("Graph succ v2", true, containment.contains(v2));
        assertEquals("Graph succ v3", true, containment.contains(v3));
        Iteration<Graph<String, String>.Vertex> dSucc = g2.successors(v3);
        containment.clear();
        for (Graph<String, String>.Vertex vert : dSucc) {
            containment.add(vert);
        }
        assertEquals("Graph succ v4", true, containment.contains(v4));
        assertEquals("Graph succ v2", true, containment.contains(v2));
        assertEquals("Graph succ v1", true, containment.contains(v1));
    }

    @Test
    public void undOutEdgeTest() {
        UndirectedGraph<String, String> g2 =
            new UndirectedGraph<String, String>();
        UndirectedGraph<String, String>.Vertex v0 = g2.add("A");
        UndirectedGraph<String, String>.Vertex v1 = g2.add("B");
        UndirectedGraph<String, String>.Vertex v2 = g2.add("C");
        UndirectedGraph<String, String>.Vertex v3 = g2.add("D");
        UndirectedGraph<String, String>.Vertex v4 = g2.add("E");
        UndirectedGraph<String, String>.Vertex v5 = g2.add("F");
        UndirectedGraph<String, String>.Vertex v6 = g2.add("G");
        UndirectedGraph<String, String>.Edge e0 = g2.add(v0, v1, "e0");
        UndirectedGraph<String, String>.Edge e1 = g2.add(v0, v2, "e1");
        UndirectedGraph<String, String>.Edge e2 = g2.add(v1, v2, "e2");
        UndirectedGraph<String, String>.Edge e3 = g2.add(v2, v3, "e3");
        UndirectedGraph<String, String>.Edge e4 = g2.add(v6, v5, "e4");
        UndirectedGraph<String, String>.Edge e5 = g2.add(v2, v6, "e5");
        UndirectedGraph<String, String>.Edge e6 = g2.add(v4, v3, "e6");
        UndirectedGraph<String, String>.Edge e7 = g2.add(v3, v1, "e7");
        Iteration<Graph<String, String>.Edge> eSucc = g2.outEdges(v1);
        ArrayList<Graph<String, String>.Edge> bOut =
            new ArrayList<Graph<String, String>.Edge>();
        for (Graph<String, String>.Edge e : eSucc) {
            bOut.add(e);
        }
        assertEquals("Graph esucc e0", true, bOut.contains(e0));
        assertEquals("Graph esucc e2", true, bOut.contains(e2));
        assertEquals("Graph esucc e7", true, bOut.contains(e7));
        bOut.clear();
        Iteration<Graph<String, String>.Edge> v3Succ = g2.outEdges(v3);
        for (Graph<String, String>.Edge e : v3Succ) {
            bOut.add(e);
        }
        assertEquals("Graph esucc e3", true, bOut.contains(e3));
        assertEquals("Graph esucc e6", true, bOut.contains(e6));
        assertEquals("Graph esucc e7", true, bOut.contains(e7));
    }

    @Test
    public void dSuccessorTest() {
        DirectedGraph<String, String> g2 =
            new DirectedGraph<String, String>();
        DirectedGraph<String, String>.Vertex v0 = g2.add("A");
        DirectedGraph<String, String>.Vertex v1 = g2.add("B");
        DirectedGraph<String, String>.Vertex v2 = g2.add("C");
        DirectedGraph<String, String>.Vertex v3 = g2.add("D");
        DirectedGraph<String, String>.Vertex v4 = g2.add("E");
        DirectedGraph<String, String>.Vertex v5 = g2.add("F");
        DirectedGraph<String, String>.Vertex v6 = g2.add("G");
        DirectedGraph<String, String>.Edge e0 = g2.add(v0, v1, "e0");
        DirectedGraph<String, String>.Edge e1 = g2.add(v0, v2, "e1");
        DirectedGraph<String, String>.Edge e2 = g2.add(v1, v2, "e2");
        DirectedGraph<String, String>.Edge e3 = g2.add(v2, v3, "e3");
        DirectedGraph<String, String>.Edge e4 = g2.add(v6, v5, "e4");
        DirectedGraph<String, String>.Edge e5 = g2.add(v2, v6, "e5");
        DirectedGraph<String, String>.Edge e6 = g2.add(v4, v3, "e6");
        DirectedGraph<String, String>.Edge e7 = g2.add(v3, v1, "e7");
        Iteration<Graph<String, String>.Vertex> bSucc = g2.successors(v1);
        ArrayList<Graph<String, String>.Vertex> containment =
            new ArrayList<Graph<String, String>.Vertex>();
        for (Graph<String, String>.Vertex vert : bSucc) {
            containment.add(vert);
        }
        assertEquals("Graph succ v0", false, containment.contains(v0));
        assertEquals("Graph succ v2", true, containment.contains(v2));
        assertEquals("Graph succ v3", false, containment.contains(v3));
        Iteration<Graph<String, String>.Vertex> dSucc = g2.successors(v3);
        containment.clear();
        for (Graph<String, String>.Vertex vert : dSucc) {
            containment.add(vert);
        }
        assertEquals("Graph succ v4", false, containment.contains(v4));
        assertEquals("Graph succ v2", false, containment.contains(v2));
        assertEquals("Graph succ v1", true, containment.contains(v1));
    }

    @Test
    public void dOutEdge() {
        DirectedGraph<String, String> g2 =
            new DirectedGraph<String, String>();
        DirectedGraph<String, String>.Vertex v0 = g2.add("A");
        DirectedGraph<String, String>.Vertex v1 = g2.add("B");
        DirectedGraph<String, String>.Vertex v2 = g2.add("C");
        DirectedGraph<String, String>.Vertex v3 = g2.add("D");
        DirectedGraph<String, String>.Vertex v4 = g2.add("E");
        DirectedGraph<String, String>.Vertex v5 = g2.add("F");
        DirectedGraph<String, String>.Vertex v6 = g2.add("G");
        DirectedGraph<String, String>.Edge e0 = g2.add(v0, v1, "e0");
        DirectedGraph<String, String>.Edge e1 = g2.add(v0, v2, "e1");
        DirectedGraph<String, String>.Edge e2 = g2.add(v1, v2, "e2");
        DirectedGraph<String, String>.Edge e3 = g2.add(v2, v3, "e3");
        DirectedGraph<String, String>.Edge e4 = g2.add(v6, v5, "e4");
        DirectedGraph<String, String>.Edge e5 = g2.add(v2, v6, "e5");
        DirectedGraph<String, String>.Edge e6 = g2.add(v4, v3, "e6");
        DirectedGraph<String, String>.Edge e7 = g2.add(v3, v1, "e7");
        Iteration<Graph<String, String>.Edge> eSucc = g2.outEdges(v1);
        ArrayList<Graph<String, String>.Edge> bOut =
            new ArrayList<Graph<String, String>.Edge>();
        for (Graph<String, String>.Edge e : eSucc) {
            bOut.add(e);
        }
        assertEquals("Graph esucc e0", false, bOut.contains(e0));
        assertEquals("Graph esucc e2", true, bOut.contains(e2));
        assertEquals("Graph esucc e7", false, bOut.contains(e7));
        bOut.clear();
        Iteration<Graph<String, String>.Edge> v3Succ = g2.outEdges(v3);
        for (Graph<String, String>.Edge e : v3Succ) {
            bOut.add(e);
        }
        assertEquals("Graph esucc e3", false, bOut.contains(e3));
        assertEquals("Graph esucc e6", false, bOut.contains(e6));
        assertEquals("Graph esucc e7", true, bOut.contains(e7));
    }
}

