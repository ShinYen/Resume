package tex61;

import java.util.List;
import java.util.ArrayList;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests of InputParser.
 *  @author Shin-Yen Huang
 */
public class InputParserTest {

    private static final String NL = System.getProperty("line.separator");

    private void setupWriter() {
        output = new StringWriter();
        writer = new PrintWriter(output);
    }

    private void setupCollector() {
        outList = new ArrayList<>();
    }

    private void makeTestLines(int n) {
        testLines = new ArrayList<>();
        for (int i = 0; i < n; i += 1) {
            testLines.add("Line " + i);
        }
    }

    private void setupController() {
        _cont = new Controller(writer);
    }

    private String joinLines() {
        StringBuilder S = new StringBuilder();
        for (String L : testLines) {
            S.append(L);
            S.append(NL);
        }
        return S.toString();
    }

    @Test
    public void testProcess() {
        setupWriter();
        makeTestLines(5);
        Controller cont = new Controller(writer);
        InputParser input = new InputParser(joinLines(), cont);
        input.process();
        writer.close();
        assertEquals("Wrong output", "   Line 0 Line 1 Line 2 Line 3 Line 4"
                        + NL, output.toString());
    }

    @Test
    public void testParIndent() {
        setupWriter();
        makeTestLines(5);
        String S = "\\parindent{2}" + joinLines();
        Controller cont = new Controller(writer);
        InputParser input = new InputParser(S, cont);
        input.process();
        writer.close();
        assertEquals("Wrong output", "  Line 0 Line 1 Line 2 Line 3 Line 4"
                        + NL, output.toString());
    }


    @Test
    public void testTextHeight() {
        setupWriter();
        makeTestLines(5);
        String S = "\\textheight{5}" + joinLines();
        Controller cont = new Controller(writer);
        InputParser input = new InputParser(S, cont);
        input.process();
        writer.close();
        assertEquals("Wrong output", "   Line 0 Line 1 Line 2 Line 3 Line 4"
                        + NL, output.toString());
    }

    @Test
    public void testIsInt() {
        boolean a = InputParser.isInt("1");
        assertEquals("IsInt", true, a);
    }

    /** Controller test case thing. */
    private Controller _cont;
    /** Collects output to a PrintWriter. */
    private StringWriter output;
    /** Collects output from a PageAssembler. */
    private PrintWriter writer;
    /** Lines of test data. */
    private List<String> testLines;
    /** Lines from a PageCollector. */
    private List<String> outList;
    /** Target PageAssembler. */
    private PageAssembler pages;
}
