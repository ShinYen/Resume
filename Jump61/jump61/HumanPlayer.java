package jump61;
/** import static jump61.GameException.error; */

/** A Player that gets its moves from manual input.
 *  @author Shin-Yen Huang
 */
class HumanPlayer extends Player {

    /** A new player initially playing COLOR taking manual input of
     *  moves from GAME's input source. needs FIX and makemove too */
    HumanPlayer(Game game, Color color) {
        super(game, color);
        _m = new int[2];
    }

    @Override
    int[] getArr() {
        return _m;
    }

    @Override
    void makeMove() {
        Game game = getGame();
        Board board = getBoard();
        if (game.getMove(_m)) {
            game.makeMove(_m[0], _m[1]);
        }
    }

}
