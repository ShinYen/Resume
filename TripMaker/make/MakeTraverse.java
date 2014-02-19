package make;

import graph.Traversal;
import graph.Graph;
import java.util.Map;
import java.util.ArrayList;

/** extends Traversal, used to test Traverse.
*   @author Shin-Yen Huang */
public class MakeTraverse<VLabel, NoLabel>
    extends Traversal<VLabel, NoLabel> {

    /** constructor takes in G HIGH, CURRENTTIME,FILEMAP. */
    MakeTraverse(Graph<VLabel, NoLabel> g, int currentTime, int high,
        Map<String, Integer> fileMap) {
        _fileMap = fileMap;
        _highestTime = high;
        _currentTime = currentTime;
        _g = g;
    }

    @Override
    protected void preVisit(Graph<VLabel, NoLabel>.Edge e,
                            Graph<VLabel, NoLabel>.Vertex v0) {
    }

    @Override
    protected void visit(Graph<VLabel, NoLabel>.Vertex v) {
    }

    @Override
    protected void postVisit(Graph<VLabel, NoLabel>.Vertex v) {
        if (!_printed.contains(v)) {
            int ind = 0;
            Rules V = (Rules) v.getLabel();
            int successors = V.getP().size();
            if (!_fileMap.containsKey(V.getT())) {
                _highestTime += 1;
                _fileMap.put(V.getT(), _highestTime);
                System.out.print(v);
                _printed.add(v);
            } else {
                for (Graph<VLabel, NoLabel>.Vertex vert : _g.successors(v)) {
                    Rules S = (Rules) vert.getLabel();
                    Rules S2 = (Rules) v.getLabel();
                    int a = _fileMap.get(S.getT());
                    int b = _fileMap.get(S2.getT());
                    if (a < b) {
                        ind += 1;
                    } else {
                        _highestTime += 1;
                        ind -= 5;
                        _fileMap.put(S2.getT(), _highestTime);
                    }
                }
                if (ind != successors) {
                    System.out.print(v);
                    _printed.add(v);
                }
            }
        }
    }

    /** Arraylist of printed VLabel. */
    private ArrayList<Graph<VLabel, NoLabel>.Vertex> _printed =
        new ArrayList<Graph<VLabel, NoLabel>.Vertex>();

    /** list of visited vertices. */
    private ArrayList<Graph<VLabel, NoLabel>.Vertex> _visit =
        new ArrayList<Graph<VLabel, NoLabel>.Vertex>();

    /** graph in question. */
    private Graph<VLabel, NoLabel> _g;

    /** map of the file passed in from caller. */
    private Map<String, Integer> _fileMap;

    /** current time. */
    private int _currentTime;

    /** highest time. */
    private int _highestTime;
}
