package tex61;

import java.util.List;
import java.util.ArrayList;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests of Controller.
 *  @author Shin-Yen Huang
 */
public class ControllerTest {

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

    private void writeTestLines() {
        for (String L : testLines) {
            pages.addLine(L);
        }
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
    public void testOutput() {
        setupWriter();
        Controller cont = new Controller(writer);
        makeTestLines(17);
        for (String S: testLines) {
            cont.addText(S);
            cont.endWord();
        }
        cont.addNewline();
        cont.endParagraph();
        writer.close();
        assertEquals("This is wrong", "   Line 0 Line 1 Line 2 Line 3 Line 4"
                        + " Line 5 Line 6 Line 7 Line 8 Line 9" + NL
                        + "Line 10 Line 11 Line 12 Line 13 Line"
                        + " 14 Line 15 Line 16" + NL,
                        output.toString());
    }

    @Test
    public void testTextWidth() {
        setupWriter();
        Controller cont = new Controller(writer);
        String S = "asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf"
                    + "asdfasdfasdfasdfasd";
        cont.addText(S);
        cont.endWord();
        writer.close();
        assertEquals("This is wrong", "",
                        output.toString());
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
