package jump61;

import java.util.Formatter;

import static jump61.Color.*;

/** Represents the state of a Jump61 game.  Squares are indexed either by
 *  row and column (between 1 and size()), or by square number, numbering
 *  squares by rows, with squares in row 1 numbered 0 - size()-1, in
 *  row 2 numbered size() - 2*size() - 1, etc.
 *  @author Shin-Yen Huang
 */
abstract class Board {

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo history and sets the number of moves to 0. */
    void clear(int N) {
        unsupported("clear");
    }

    /** Copy the contents of BOARD into me. */
    void copy(Board board) {
        unsupported("copy");
    }

    /** Return the number of rows and of columns of THIS. */
    abstract int size();

    /** Returns the number of spots in the square at row R, column C,
     *  1 <= R, C <= size (). */
    abstract int spots(int r, int c);

    /** Returns the number of spots in square #N. */
    abstract int spots(int n);

    /** Returns the color of square #N, numbering squares by rows, with
     *  squares in row 1 number 0 - size()-1, in row 2 numbered
     *  size() - 2*size() - 1, etc. */
    abstract Color color(int n);

    /** Returns the color of the square at row R, column C,
     *  1 <= R, C <= size(). */
    abstract Color color(int r, int c);

    /** Returns the total number of moves made (red makes the odd moves,
     *  blue the even ones). */
    abstract int numMoves();

    /** Returns the Color of the player who would be next to move.  If the
     *  game is won, this will return the loser (assuming legal position). */
    Color whoseMove() {
        if (numMoves() % 2 == 1) {
            return RED;
        } else {
            return BLUE;
        }
    }

    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /** Return true iff S is a valid square number. */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }

    /** Return the row number for square #N. */
    final int row(int n) {
        return (n / size()) + 1;
    }

    /** Return the column number for square #N. */
    final int col(int n) {
        return (n % size()) + 1;
    }

    /** Return the square number of row R, column C. */
    final int sqNum(int r, int c) {
        return ((r - 1) * size()) + (c - 1);
    }


    /** Returns true iff it would currently be legal for PLAYER to add a spot
        to square at row R, column C. */
    boolean isLegal(Color player, int r, int c) {
        return isLegal(player, sqNum(r, c));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
     *  to square #N. */
    boolean isLegal(Color player, int n) {
        return player.playableSquare(color(n));
    }

    /** Returns true iff PLAYER is allowed to move at this point. */
    boolean isLegal(Color player) {
        return player.playableSquare(player);
    }

    /** Returns the winner of the current position, if the game is over,
     *  and otherwise null. NEEDS FIXING */
    final Color getWinner() {
        int spots = size() * size();
        if (numOfColor(RED) == spots) {
            return RED;
        } else if (numOfColor(BLUE) == spots) {
            return BLUE;
        } else {
            return null;
        }
    }

    /** Return the number of squares of given COLOR. */
    abstract int numOfColor(Color color);

    /** Add a spot from PLAYER at row R, column C.  Assumes
     *  isLegal(PLAYER, R, C). */
    void addSpot(Color player, int r, int c) {
        unsupported("addSpot");
    }


    /** ADD SPOT with PLAYER R and C and save if B is true. */
    void saveAddSpot(Color player, int r, int c, boolean b) {
        unsupported("saveAddSpot");
    }

    /** Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N). */
    void addSpot(Color player, int n) {
        unsupported("addSpot");
    }

    /** addspot to PLAYER and N and save if B is true. */
    void saveAddSpot(Color player, int n, boolean b) {
        unsupported("saveAddSpot");
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white).  Clear the undo
     *  history. */
    void set(int r, int c, int num, Color player) {
        unsupported("set");
    }

    /** Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER
     *  if NUM > 0 (otherwise, white).  Clear the undo history. */
    void set(int n, int num, Color player) {
        unsupported("set");
    }

    /** Set the current number of moves to N.  Clear the undo history. */
    void setMoves(int n) {
        unsupported("setMoves");
    }

    /** Undo the effects one move (that is, one addSpot command).  One
     *  can only undo back to the last point at which the undo history
     *  was cleared, or the construction of this Board. */
    void undo() {
        unsupported("undo");
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = 1; r <= size(); r += 1) {
            out.format("   ");
            for (int c = 1; c <= size(); c += 1) {
                if (color(r, c) == WHITE) {
                    out.format(" --");
                } else {
                    out.format(" %d%c", spots(r, c),
                        color(r, c).toString().charAt(0));
                }
            }
            out.format("%n");
        }
        out.format("===");
        return out.toString();
    }

    /** Returns an external rendition of me, suitable for
     *  human-readable textual display.  This is distinct from the dumped
     *  representation (returned by toString). */
    public String toDisplayString() {
        StringBuilder out = new StringBuilder(toString());
        return out.toString();
    }

    /** Returns the number of neighbors of the square at row R, column C. */
    int neighbors(int r, int c) {
        if (c == 1 || c == size()) {
            if (r == 1 || r == size()) {
                return 2;
            } else {
                return 3;
            }
        } else if (r == 1 || r == size()) {
            return 3;
        } else {
            return 4;
        }
    }

    /** Returns the number of neighbors of square #N. */
    int neighbors(int n) {
        if (col(n) == 0 || col(n) == size()) {
            if (row(n) == 0 || row(n) == size()) {
                return 2;
            } else {
                return 3;
            }
        } else if (row(n) == 1 || row(n) == size()) {
            return 3;
        } else {
            return 4;
        }
    }

    /** Indicate fatal error: OP is unsupported operation. */
    private void unsupported(String op) {
        String msg = String.format("'%s' operation not supported", op);
        throw new UnsupportedOperationException(msg);
    }

    /** The length of an end of line on this system. */
    private static final int NL_LENGTH =
        System.getProperty("line.separator").length();

    /** string rep of board. */
    protected String[][] _board;

}
