package graph;
/** extends Traversal, used to test Traverse.
*   @author Shin-Yen Huang */
public class VerboseTraversal<VLabel, ELabel>
    extends Traversal<VLabel, ELabel> {

    @Override
    protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                            Graph<VLabel, ELabel>.Vertex v0) {
        System.out.println("preVisit: " + e.getV(v0));
    }

    @Override
    protected void visit(Graph<VLabel, ELabel>.Vertex v) {
        System.out.println("visit: " + v.getLabel());
    }

    @Override
    protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {
        System.out.println("postVisit: " + v.getLabel());
    }

}
