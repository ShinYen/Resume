package jump61;

import static jump61.Color.*;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests of Boards.
 *  @author Shin-Yen Huang
 */
public class BoardTest {

    private static final String NL = System.getProperty("line.separator");

    @Test
    public void testClear() {
        Board B = new MutableBoard(6);
        B.saveAddSpot(RED, 1, 1, true);
        B.saveAddSpot(BLUE, 2, 1, true);
        B.saveAddSpot(RED, 6, 6, true);
        B.clear(B.size());
        B.saveAddSpot(RED, 2, 1, true);
        checkBoard("Wrong", B, 2, 1, 1, RED);
    }

    @Test
    public void testCopy() {
        Board B = new MutableBoard(6);
        B.saveAddSpot(RED, 1, 1, true);
        B.saveAddSpot(BLUE, 1, 2, true);
        B.saveAddSpot(RED, 6, 5, true);
        B.saveAddSpot(BLUE, 4, 4, true);
        B.saveAddSpot(RED, 5, 4, true);
        Board C = new MutableBoard(6);
        C.copy(B);
        checkBoard("Diff", C, 1, 1, 1, RED, 1, 2, 1, BLUE,
            6, 5, 1, RED, 4, 4, 1, BLUE, 5, 4, 1, RED);
    }
    @Test
    public void testNumOfColor() {
        Board B = new MutableBoard(6);
        assertEquals("Not all white", B.numOfColor(WHITE), 36);
        B.saveAddSpot(RED, 2, 1, true);
        B.saveAddSpot(BLUE, 1, 6, true);
        B.saveAddSpot(RED, 3, 3, true);
        B.saveAddSpot(BLUE, 3, 4, true);
        B.saveAddSpot(RED, 5, 1, true);
        assertEquals("Wrong", B.numOfColor(RED), 3);
        assertEquals("Wrong", B.numOfColor(BLUE), 2);
        assertEquals("Wrong", B.numOfColor(WHITE), 31);
    }
    @Test
    public void testSqnum() {
        Board B = new MutableBoard(6);
        assertEquals("Diff", B.sqNum(2, 2), 7);
        assertEquals("Diff", B.row(34), 6);
        assertEquals("Diff", B.col(12), 1);
    }

    @Test
    public void testWinner() {
        Board B = new MutableBoard(2);
        B.set(2, 2, 1, RED);
        B.set(1, 1, 1, RED);
        B.set(1, 2, 1, RED);
        B.set(2, 1, 1, RED);
        assertEquals("RED wins", B.getWinner(), RED);
        B.clear(B.size());
        B.set(1, 1, 2, BLUE);
        B.set(1, 2, 2, BLUE);
        B.set(2, 1, 2, BLUE);
        B.set(2, 2, 2, BLUE);
        assertEquals("BLUE wins", B.getWinner(), BLUE);
        B.clear(B.size());
        assertEquals("No wins.", B.getWinner(), null);
    }

    @Test
    public void testExists() {
        Board B = new MutableBoard(5);
        assertEquals("Not in board", B.exists(0, 1), false);
        assertEquals("Not in board", B.exists(6, 5), false);
        assertEquals("In board", B.exists(4, 4), true);
    }

    @Test
    public void testLegal() {
        Board B = new MutableBoard(6);
        B.saveAddSpot(RED, 1, 1, true);
        assertEquals("Illegal move", B.isLegal(BLUE, 1, 1), false);
        assertEquals("Legal move", B.isLegal(RED, 1, 1), true);
    }

    @Test
    public void testSize() {
        Board B = new MutableBoard(5);
        assertEquals("bad length", 5, B.size());
        ConstantBoard C = new ConstantBoard(B);
        assertEquals("bad length", 5, C.size());
        Board D = new MutableBoard(C);
        assertEquals("bad length", 5, C.size());
    }

    @Test
    public void testSet() {
        Board B = new MutableBoard(5);
        B.set(2, 2, 1, RED);
        B.setMoves(1);
        assertEquals("wrong number of spots", 1, B.spots(2, 2));
        assertEquals("wrong color", RED, B.color(2, 2));
        assertEquals("wrong count", 1, B.numOfColor(RED));
        assertEquals("wrong count", 0, B.numOfColor(BLUE));
        assertEquals("wrong count", 24, B.numOfColor(WHITE));
    }

    @Test
    public void testMove() {
        Board B = new MutableBoard(6);
        B.saveAddSpot(RED, 1, 1, true);
        checkBoard("#1", B, 1, 1, 1, RED);
        B.saveAddSpot(BLUE, 2, 1, true);
        checkBoard("#2", B, 1, 1, 1, RED, 2, 1, 1, BLUE);
        B.saveAddSpot(RED, 1, 1, true);
        checkBoard("#3", B, 1, 1, 2, RED, 2, 1, 1, BLUE);
        B.saveAddSpot(BLUE, 2, 1, true);
        checkBoard("#4", B, 1, 1, 2, RED, 2, 1, 2, BLUE);
        B.saveAddSpot(RED, 1, 1, true);
        checkBoard("#5", B, 1, 1, 1, RED, 2, 1, 3, RED, 1, 2, 1, RED);
        B.undo();
        checkBoard("#4U", B, 1, 1, 2, RED, 2, 1, 2, BLUE);
        B.undo();
        checkBoard("#3U", B, 1, 1, 2, RED, 2, 1, 1, BLUE);
        B.undo();
        checkBoard("#2U", B, 1, 1, 1, RED, 2, 1, 1, BLUE);
        B.undo();
        checkBoard("#1U", B, 1, 1, 1, RED);
    }

    private void checkBoard(String msg, Board B, Object... contents) {
        for (int k = 0; k < contents.length; k += 4) {
            String M = String.format("%s at %d %d", msg, contents[k],
                                     contents[k + 1]);
            assertEquals(M, (int) contents[k + 2],
                         B.spots((int) contents[k], (int) contents[k + 1]));
            assertEquals(M, contents[k + 3],
                         B.color((int) contents[k], (int) contents[k + 1]));
        }
        int c;
        c = 0;
        for (int i = B.size() * B.size() - 1; i >= 0; i -= 1) {
            assertTrue("bad white square #" + i,
                       (B.color(i) == WHITE) == (B.spots(i) == 0));
            if (B.color(i) != WHITE) {
                c += 1;
            }
        }
        assertEquals("extra squares filled", contents.length / 4, c);
    }

}
