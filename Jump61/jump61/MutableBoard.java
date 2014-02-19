package jump61;


import static jump61.Color.*;
import java.util.ArrayList;
import java.util.List;

/** A Jump61 board state.
 *  @author Shin Yen Huang
 */
class MutableBoard extends Board {

    /** An N x N board in initial configuration. */
    MutableBoard(int N) {
        _N = N;
        _board = new String[N][N];
        _saveStates = new ArrayList<Board>();
        _moves = 1;
    }

    /** A board whose initial contents are copied from BOARD0. Clears the
     *  undo history. */
    MutableBoard(Board board0) {
        this.copy(board0);
        _saveStates = new ArrayList<Board>();
    }

    @Override
    void clear(int N) {
        _board = new String[N][N];
        _saveStates = new ArrayList<Board>();
        _N = N;
        _moves = 1;
    }

    @Override
    void copy(Board board) {
        for (int r = 1; r <= size(); r += 1) {
            for (int c = 1; c <= size(); c += 1) {
                _board[r - 1][c - 1] = String.format("%d%c", board.spots(r, c),
                    board.color(r, c).toString().charAt(0));
            }
        }
    }

    @Override
    int size() {
        return _N;
    }

    @Override
    int spots(int r, int c) {
        if (color(r, c) == WHITE) {
            return 0;
        } else {
            String charr = String.valueOf(_board[r - 1][c - 1].charAt(0));
            return Integer.parseInt(charr);
        }
    }

    @Override
    int spots(int n) {
        int r = row(n);
        int c = col(n);
        return spots(r, c);
    }

    @Override
    Color color(int r, int c) {
        if (_board[r - 1][c - 1] == null
            || _board[r - 1][c - 1].charAt(1) == 'w') {
            return WHITE;
        } else {
            char ch = _board[r - 1][c - 1].charAt(1);
            if (ch == 'r') {
                return RED;
            } else {
                return BLUE;
            }
        }
    }

    @Override
    Color color(int n) {
        int r = row(n);
        int c = col(n);
        return color(r, c);
    }

    @Override
    int numMoves() {
        return _moves;
    }

    @Override
    int numOfColor(Color color) {
        int num = 0;
        for (int r = 1; r <= size(); r += 1) {
            for (int c = 1; c <= size(); c += 1) {
                if (color(r, c) == color) {
                    num += 1;
                }
            }
        }
        return num;
    }

    @Override
    void addSpot(Color player, int r, int c) {
        if (color(r, c) == WHITE) {
            if (player == RED) {
                _board[r - 1][c - 1] = "1r";
            } else {
                _board[r - 1][c - 1] = "1b";
            }
        } else {
            if (spots(r, c) == neighbors(r, c)) {
                set(r, c, 1, player);
                if (getWinner() == null) {
                    jump(sqNum(r, c));
                }
            } else {
                set(r, c, spots(r, c) + 1, player);
            }
        }
    }

    /** method that takes in PLAYER R and C and save it pending on N.*/
    void saveAddSpot(Color player, int r, int c, boolean n) {
        if (n) {
            addSpot(player, r, c);
            _moves += 1;
            MutableBoard save = new MutableBoard(_N);
            save.copy(this);
            MutableBoard constantSave = new MutableBoard(save);
            _saveStates.add(0, constantSave);
        } else {
            addSpot(player, r, c);
        }
    }

    /** Returns a DEEP copy of nested array INP. */
    private MutableBoard deepCopyBoard() {
        MutableBoard s = new MutableBoard(_N);
        for (int r = 1; r <= s.size(); r += 1) {
            for (int c = 1; c <= s.size(); c += 1) {
                s._board[r - 1][c - 1] = _board[r - 1][c - 1];
            }
        }
        return s;
    }

    /** takes in PLAYER and S and saves it pending on N.*/
    void saveAddSpot(Color player, int s, boolean n) {
        saveAddSpot(player, row(s), col(s), n);
    }

    @Override
    void addSpot(Color player, int n) {
        addSpot(player, row(n), col(n));
    }

    @Override
    void set(int r, int c, int num, Color player) {
        String numm = Integer.toString(num);
        String spot = _board[r - 1][c - 1];
        if (color(r, c) == WHITE) {
            if (player == RED) {
                _board[r - 1][c - 1] = numm + "r";
            } else if (player == BLUE) {
                _board[r - 1][c - 1] = numm + "b";
            } else {
                _board[r - 1][c - 1] = null;
            }
        } else if (color(r, c) == RED) {
            if (player == BLUE) {
                _board[r - 1][c - 1] = numm + "b";
            } else if (player == RED) {
                _board[r - 1][c - 1] = numm + "r";
            } else {
                _board[r - 1][c - 1] = null;
            }
        } else {
            if (player == RED) {
                _board[r - 1][c - 1] = numm + "r";
            } else if (player == BLUE) {
                _board[r - 1][c - 1] = numm + "b";
            } else {
                _board[r - 1][c - 1] = null;
            }
        }
    }

    @Override
    void set(int n, int num, Color player) {
        int r = row(n);
        int c = col(n);
        set(r, c, num, player);
    }

    @Override
    void setMoves(int num) {
        assert num > 0;
        _moves = num;
    }

    @Override
    void undo() {
        if (_saveStates.size() < 2) {
            MutableBoard temp = new MutableBoard(_N);
            _saveStates.add(temp);
        }
        _saveStates.remove(0);
        this.copy(_saveStates.get(0));
        _moves -= 1;
    }

    /** Do all jumping on this board, assuming that initially, S is the only
     *  square that might be over-full. */
    private void jump(int S) {
        if (col(S) != size()) {
            int spotRight = S + 1;
            changeColor(spotRight);
            saveAddSpot(whoseMove(), spotRight, false);
        }
        if (row(S) != size()) {
            int spotBot = (S + size());
            changeColor(spotBot);
            saveAddSpot(whoseMove(), spotBot, false);
        }
        if (row(S) != 1) {
            int spotTop = S - size();
            changeColor(spotTop);
            saveAddSpot(whoseMove(), spotTop, false);
        }
        if (col(S) != 1) {
            int spotLeft = S - 1;
            changeColor(spotLeft);
            saveAddSpot(whoseMove(), spotLeft, false);
        }
    }

    /** changes the color of S to the color of the current player. */
    private void changeColor(int s) {
        if (color(s) != whoseMove()) {
            int temp1 = spots(s);
            set(s, temp1, whoseMove());
        }
    }

    /** Total combined number of moves by both sides. */
    protected int _moves;
    /** Convenience variable: size of board (squares along one edge). */
    private int _N;
    /** an array of board saved board states for undo function. */
    private List<Board> _saveStates;
    /** the board for the game. */
    private String[][] _board;

}
