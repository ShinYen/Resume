package jump61;

import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static jump61.Color.*;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests of Games.
 *  @author Shin-Yen Huang
 */

public class GameTest {

    private Writer output = new OutputStreamWriter(System.out);
    private String inp = "";

    private Game makeGame() {
        Game temp = new Game(new StringReader(inp),
                            output, output,
                            new OutputStreamWriter(System.err));
        return temp;
    }

    @Test
    public void testMoves() {
        Game t = makeGame();
        assertEquals("bad init", 1, t.getBoard().numMoves());
        t.makeMove(1, 1);
        assertEquals("bad num", 2, t.getBoard().numMoves());
    }

    @Test
    public void testMakeMove() {
        Game c = makeGame();
        assertEquals("wrong color", "white", c.getBoard().color(0).toString());
        assertEquals("wrong spots", 0, c.getBoard().spots(0));
        c.makeMove(1, 1);
        assertEquals("wrong color", "red", c.getBoard().color(0).toString());
        assertEquals("wrong color", 1, c.getBoard().spots(0));
    }
}
