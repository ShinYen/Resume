package jump61;

import java.util.ArrayList;

/** An automated Player.
 *  @author Shin-Yen Huang
 */
class AI extends Player {

    /** A new player of GAME initially playing COLOR that chooses
     *  moves automatically.
     */
    AI(Game game, Color color) {
        super(game, color);
        _moves = new ArrayList<Integer>();
        _m = new int[2];
    }

    @Override
    int[] getArr() {
        return _m;
    }

    @Override
    void makeMove() {
        Game game = getGame();
        Board b = getBoard();
        minmax(getColor(), b, 3, Defaults.NEG_BOUND, _moves);
        int move = _moves.get(game.randInt(_moves.size()));
        _m[0] = b.row(move);
        _m[1] = b.col(move);
        int r = b.row(move);
        int c = b.col(move);
        game.makeMove(r, c);
        _moves.clear();
        System.out.printf("%s moves %d %d.%n",
            getColor().toCapitalizedString(), r, c);
    }

    /** Return the minimum of CUTOFF and the minmax value of board B
     *  (which must be mutable) for player P to a search depth of D
     *  (where D == 0 denotes evaluating just the next move).
     *  If MOVES is not null and CUTOFF is not exceeded, set MOVES to
     *  a list of all highest-scoring moves for P; clear it if
     *  non-null and CUTOFF is exceeded. the contents of B are
     *  invariant over this call.
     *  System.out.println(d + ": " +b);*/
    private int minmax(Color p, Board b, int d, int cutoff,
                       ArrayList<Integer> moves) {
        if (b.getWinner() != null || d == -1) {
            return staticEval(getColor(), b);
        }
        ArrayList<Integer> legMoves =  new ArrayList<Integer>();
        for (int i = 0; i < b.size() * b.size(); i += 1) {
            if (b.isLegal(p, i)) {
                legMoves.add(i);
            }
        }
        if (p == getColor()) {
            int best = cutoff;
            for (int s : legMoves) {
                b.saveAddSpot(p, s, true);
                int alpha = minmax(p.opposite(), b, d - 1, -best, moves);
                b.undo();
                if (d == 3) {
                    if (alpha > best) {
                        moves.clear();
                        moves.add(s);
                    } else if (alpha == best) {
                        moves.add(s);
                    }
                }
                best = Math.max(best, alpha);
                if (alpha >= -cutoff) {
                    return alpha;
                }
                cutoff = Math.max(cutoff, best);
            }
            return best;
        } else if (p == getColor().opposite()) {
            int best = cutoff;
            for (int s : legMoves) {
                b.saveAddSpot(p, s, true);
                int beta = minmax(p.opposite(), b, d - 1, -best, moves);
                b.undo();
                best = Math.min(best, beta);
                if (beta <= -cutoff) {
                    return beta;
                }
                cutoff = Math.min(cutoff, best) * -1;
            }
            return best;
        }
        return cutoff;
    }

    /** Returns heuristic value of board B for player P.
     *  Higher is better for P. */
    private int staticEval(Color p, Board b) {
        if (b.getWinner() == getColor()) {
            return Defaults.POS_BOUND;
        }
        if (b.getWinner() == getColor().opposite()) {
            return Defaults.NEG_BOUND;
        }
        int total = b.numOfColor(getColor()) * Defaults.SPOT_VAL;
        total -= b.numOfColor(getColor().opposite()) * Defaults.SPOT_VAL;
        for (int r = 1; r < b.size(); r += 1) {
            for (int c = 1; r < b.size(); r += 1) {
                if (b.color(r, c) == getColor()) {
                    if (b.spots(r, c) == b.neighbors(r, c)) {
                        total += b.neighbors(r, c) * Defaults.NEIGH_VAL;
                    }
                    total += Defaults.COL_VAL;
                }
                if (b.color(r, c) == getColor().opposite()) {
                    if (b.spots(r, c) == b.neighbors(r, c)) {
                        total -= b.neighbors(r, c) * Defaults.NEIGH_VAL;
                    }
                    total -= Defaults.COL_VAL;
                }
            }
        }
        return total;
    }

    /** arraylist that stores the moves.*/
    private ArrayList<Integer> _moves;
    /** boolean if in forced win. */
    private boolean _forcedWin;
}

