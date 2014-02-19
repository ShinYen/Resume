package make;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.HashMap;
import graph.Graph;
import graph.NoLabel;
import graph.Iteration;
import graph.DirectedGraph;


/** Initial class for the 'make' program.
 *  @author Shin - Yen Huang
 */
public final class Main {

    /** Entry point for the CS61B make program.  ARGS may contain options
     *  and targets:
     *      [ -f MAKEFILE ] [ -D FILEINFO ] TARGET1 TARGET2 ...
     */
    public static void main(String... args) {
        String makefileName;
        String fileInfoName;

        if (args.length == 0) {
            usage();
        }

        makefileName = "Makefile";
        fileInfoName = "fileinfo";

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-f")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    makefileName = args[a];
                }
            } else if (args[a].equals("-D")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    fileInfoName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        ArrayList<String> targets = new ArrayList<String>();

        for (; a < args.length; a += 1) {
            targets.add(args[a]);
        }

        make(makefileName, fileInfoName, targets);
    }

    /** Carry out the make procedure using MAKEFILENAME as the makefile,
     *  taking information on the current file-system state from FILEINFONAME,
     *  and building TARGETS, or the first target in the makefile if TARGETS
     *  is empty.
     */
    private static void make(String makefileName, String fileInfoName,
                             List<String> targets) {
        try {
            Reader fileIn;
            fileIn = new FileReader(new File(fileInfoName));
            file = new HashMap<String, Integer>();
            Scanner fileInp = new Scanner(fileIn);
            _stringRules = new ArrayList<String>();
            Reader makeIn;
            makeIn = new FileReader(new File(makefileName));
            _rules = new ArrayList<Rules>();
            Scanner makeInp = new Scanner(makeIn);
            fileInputParsing(fileInp);
            inputParsing(makeInp);
            buildGraph(targets);
        } catch (IOException e) {
            System.exit(1);
            return;
        } catch (InputMismatchException err) {
            System.exit(1);
            return;
        } catch (NumberFormatException error) {
            System.exit(1);
            return;
        }
    }

    /** takes in FILEINP and parses it to class variables. */
    private static void fileInputParsing(Scanner fileInp) {
        currentTime = Integer.parseInt(fileInp.nextLine());
        _highestTime = 0;
        while (fileInp.hasNextLine()) {
            String name = fileInp.next();
            int date = fileInp.nextInt();
            file.put(name, date);
            if (date > _highestTime) {
                _highestTime = date;
            }
            fileInp.nextLine();
        }
    }

    /** takes in MAKEINP and parses it to class variables. */
    private static void inputParsing(Scanner makeInp) {
        Rules rule = new Rules();
        while (makeInp.hasNextLine()) {
            String line = makeInp.nextLine();
            if (line.matches("[^=:#\\ ]+:.*")) {
                String[] tempLine = line.split(" ");
                String targetstr = tempLine[0].
                    substring(0, tempLine[0].length() - 1);
                if (!_stringRules.contains(targetstr)) {
                    _stringRules.add(targetstr);
                    rule = new Rules(targetstr);
                    _rules.add(rule);
                } else {
                    for (Rules r : _rules) {
                        if (r.getT().equals(targetstr)) {
                            rule = r;
                        }
                    }
                }
                for (int i = 1; i < tempLine.length; i += 1) {
                    rule.addP(tempLine[i]);
                }
            } else if (line.matches("\\p{Blank}+[^=:#\\ ]+.*")) {
                if (rule.getCommands().isEmpty()) {
                    rule.add(line);
                } else {
                    System.err.println("Bad Makefile");
                    System.exit(1);
                }
            } else if (line.equals("")) {
                continue;
            }
        }
        if (!_rules.contains(rule)) {
            _rules.add(rule);
        }
    }

    /** checks if the rules dont cycle in VERTEX. */
    private static void checkCyclic(Graph<Rules, NoLabel>.Vertex vertex) {
        for (Rules r : _rules.subList(1, _rules.size())) {
            if (r.getP().contains(vertex.getLabel().getT())) {
                if (vertex.getLabel().getP().contains(r.getT())) {
                    System.err.println("Cyclic");
                    System.exit(1);
                }
            }
        }
    }

    /** checks prereqs were made in STR. */
    private static void checkPrereq(String str) {
        for (Rules r : _rules.subList(1, _rules.size())) {
            for (String s : r.getP()) {
                if (!str.contains(s)
                    && !file.containsKey(s)) {
                    System.err.println("Prereq doesnt exist.");
                    System.exit(1);
                }
            }
        }
    }

    /** Builds a graph off of rules and starts at TARGETS. */
    private static void buildGraph(List<String> targets) {
        Graph<Rules, NoLabel> graph = new DirectedGraph<Rules, NoLabel>();
        for (Rules r : _rules) {
            graph.add(r);
        }
        List<Graph<Rules, NoLabel>.Vertex> verts =
            new ArrayList<Graph<Rules, NoLabel>.Vertex>();
        List<Graph<Rules, NoLabel>.Vertex> vert =
            new ArrayList<Graph<Rules, NoLabel>.Vertex>();
        boolean containing = false;
        String str = "";
        for (Graph<Rules, NoLabel>.Vertex vertex : graph.vertices()) {
            checkCyclic(vertex);
            verts.add(vertex);
            vert.add(vertex);
            str += vertex.getLabel().getT();
        }
        checkPrereq(str);
        for (Graph<Rules, NoLabel>.Vertex vertex : verts) {
            for (String rule : vertex.getLabel().getP()) {
                for (Graph<Rules, NoLabel>.Vertex ver : vert) {
                    if (rule.equals(ver.getLabel().getT())) {
                        graph.add(vertex, ver, new NoLabel());
                    }
                }
            }
        }
        List<Graph<Rules, NoLabel>.Vertex> starts =
            new ArrayList<Graph<Rules, NoLabel>.Vertex>();
        if (targets.isEmpty()) {
            for (Graph<Rules, NoLabel>.Vertex v : graph.vertices()) {
                if (v.getLabel().equals(_rules.get(1))) {
                    starts.add(v);
                }
            }
        } else {
            for (String s : targets) {
                Iteration<Graph<Rules, NoLabel>.Vertex> vertices =
                    graph.vertices();
                for (Graph<Rules, NoLabel>.Vertex v : vertices) {
                    if (v.getLabel().getT().equals(s)) {
                        starts.add(v);
                    }
                }
            }
        }
        MakeTraverse<Rules, NoLabel> traverse =
            new MakeTraverse<Rules, NoLabel>(graph,
                currentTime, _highestTime, file);
        for (Graph<Rules, NoLabel>.Vertex s : starts) {
            traverse.depthFirstTraverse(graph, s);
        }
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.exit(1);
    }

    /** the int of the current time from FileInfo. */
    private static int currentTime;
    /** highest time of fileinfo. */
    private static int _highestTime;
    /** the hashmap of the information from fileInfo. */
    private static HashMap<String, Integer> file;
    /** list of string of names of _rules. */
    private static List<String> _stringRules;
    /** a list of rules used as graphs. */
    private static List<Rules> _rules;

}
