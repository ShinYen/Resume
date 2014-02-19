package jump61;

import java.io.Reader;
import java.io.Writer;
import java.io.PrintWriter;

import java.util.Scanner;
import java.util.Random;

import static jump61.Color.*;
import static jump61.GameException.error;
import java.util.InputMismatchException;
/** Main logic for playing (a) game(s) of Jump61.
 *  @author Shin-Yen Huang
 */
class Game {

    /** Name of resource containing help message. */
    private static final String HELP = "jump61/Help.txt";

    /** A new Game that takes command/move input from INPUT, prints
     *  normal output on OUTPUT, prints prompts for input on PROMPTS,
     *  and prints error messages on ERROROUTPUT. The Game now "owns"
     *  INPUT, PROMPTS, OUTPUT, and ERROROUTPUT, and is responsible for
     *  closing them when its play method returns. NEEDS FIX */
    Game(Reader input, Writer prompts, Writer output, Writer errorOutput) {
        _board = new MutableBoard(Defaults.BOARD_SIZE);
        _readonlyBoard = new ConstantBoard(_board);
        _prompter = new PrintWriter(prompts, true);
        _inp = new Scanner(input);
        _inp.useDelimiter("(?m)\\p{Blank}*$|^\\p{Blank}*|\\p{Blank}+");
        _out = new PrintWriter(output, true);
        _err = new PrintWriter(errorOutput, true);
        p1 = new HumanPlayer(this, RED);
        p2 = new AI(this, BLUE);
        _playing = false;
    }

    /** Returns a readonly view of the game board.  This board remains valid
     *  throughout the session. */
    Board getBoard() {
        return _board;
    }

    /** Play a session of Jump61.  This may include multiple games,
     *  and proceeds until the user exits.  Returns an exit code: 0 is
     *  normal; any positive quantity indicates an error. NEEDS FIX */
    int play() {
        _out.println("Welcome to " + Defaults.VERSION);
        _out.flush();
        while (true) {
            Color col = _board.whoseMove();
            if (!_playing && promptForNext()) {
                readExecuteCommand();
                _inp.reset();
            } else {
                if (p1.getColor() == col) {
                    p1.makeMove();
                    checkForWin();
                } else {
                    p2.makeMove();
                    checkForWin();
                }
            }
        }
    }

    /** Get a move from my input and place its row and column in
     *  MOVE.  Returns true if this is successful, false if game stops
     *  or ends first. */
    boolean getMove(int[] move) {
        while (_playing && _move[0] == 0 && promptForNext()) {
            readExecuteCommand();
        }
        if (_move[0] > 0) {
            move[0] = _move[0];
            move[1] = _move[1];
            _move[0] = 0;
            return true;
        } else {
            return false;
        }
    }

    /** Add a spot to R C, if legal to do so. */
    void makeMove(int r, int c) {
        Color player = _board.whoseMove();
        if (_board.isLegal(player, r, c)) {
            _board.saveAddSpot(player, r, c, true);
        } else {
            reportError("Illegal Move, try again");
        }
    }

    /** Add a spot to square #N, if legal to do so. */
    void makeMove(int n) {
        int r = _board.row(n);
        int c = _board.col(n);
        makeMove(r, c);
    }

    /** Return a random integer in the range [0 .. N), uniformly
     *  distributed.  Requires N > 0. */
    int randInt(int n) {
        return _random.nextInt(n);
    }

    /** Send a message to the user as determined by FORMAT and ARGS, which
     *  are interpreted as for String.format or PrintWriter.printf. */
    void message(String format, Object... args) {
        _out.printf(format, args);
    }

    /** Check whether we are playing and there is an unannounced winner.
     *  If so, announce and stop play. */
    private void checkForWin() {
        if (_playing && (_board.getWinner() != null)) {
            announceWinner();
            _playing = false;
        }
    }

    /** Send announcement of winner to my user output. */
    private void announceWinner() {
        message(_board.getWinner().toCapitalizedString()
            + " wins.%n");
    }

    /** Make PLAYER an AI for subsequent moves. */
    private void setAuto(Color player) {
        _playing = false;
        if (player == RED) {
            p1 = new AI(this, player);
        } else {
            p2 = new AI(this, player);
        }
    }

    /** Make PLAYER take manual input from the user for subsequent moves. */
    private void setManual(Color player) {
        _playing = false;
        if (player == RED) {
            p1 = new HumanPlayer(this, player);
        } else {
            p2 = new HumanPlayer(this, player);
        }
    }

    /** Stop any current game and clear the board to its initial
     *  state. */
    private void clear() {
        _board.clear(_board.size());
        _playing = false;
    }

    /** Print the current board using standard board-dump format. */
    private void dump() {
        _out.println(_board);
    }

    /** Print a help message. */
    private void help() {
        Main.printHelpResource(HELP, _out);
    }

    /** Stop any current game and set the move number to N. */
    private void setMoveNumber(int n) {
        if (n > 0) {
            _playing = false;
            _board.setMoves(n);
        } else {
            reportError("Bad move param");
        }
    }

    /** Seed the random-number generator with SEED. */
    public void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** Place SPOTS spots on square R:C and color the square red or
     *  blue depending on whether COLOR is "r" or "b".  If SPOTS is
     *  0, clears the square, ignoring COLOR.  SPOTS must be less than
     *  the number of neighbors of square R, C. */
    private void setSpots(int r, int c, int spots, String color) {
        if (spots >= 0) {
            if (_board.exists(r, c)) {
                if (spots <= _board.neighbors(r, c)) {
                    if (color.equals("r")) {
                        _board.set(r, c, spots, RED);
                    } else if (color.equals("b")) {
                        _board.set(r, c, spots, BLUE);
                    } else if (spots == 0) {
                        _board.set(r, c, 0, WHITE);
                    }
                    _playing = false;
                } else {
                    reportError("Spots > neighbors set another move.");
                }
            } else {
                reportError("Spot does not exist make another move.");
            }
        } else {
            reportError("Spots < 0 bad param");
        }
    }

    /** Stop any current game and set the board to an empty N x N board
     *  with numMoves() == 0.  */
    private void setSize(int n) {
        if (n > 0) {
            _playing = false;
            _board.clear(n);
        } else {
            reportError("Bad Size");
        }
    }

    /** Begin accepting moves for game.  If the game is won,
     *  immediately print a win message and end the game. */
    private void restartGame() {
        _playing = true;
        checkForWin();
    }

    /** Save move R C in _move.  Error if R and C do not indicate an
     *  existing square on the current board. */
    private void saveMove(int r, int c) {
        if (!_board.exists(r, c)) {
            throw error("move %d %d out of bounds", r, c);
        }
        _move[0] = r;
        _move[1] = c;
    }

    /** Returns a color (player) name from _inp: either RED or BLUE.
     *  Throws an exception if not present. */
    private Color readColor() {
        return Color.parseColor(_inp.next("[rR][eE][dD]|[Bb][Ll][Uu][Ee]"));
    }

    /** Read and execute one command.  Leave the input at the start of
     *  a line, if there is more input. */
    private void readExecuteCommand() {
        try {
            String cmnd = _inp.next();
            if (_playing) {
                if  (isNumeric(cmnd)) {
                    int a = Integer.parseInt(cmnd);
                    int b = _inp.nextInt();
                    if (_board.exists(a, b)) {
                        saveMove(a, b);
                    } else {
                        reportError("Bad numbers");
                    }
                } else {
                    executeCommand(cmnd);
                }
            } else {
                executeCommand(cmnd);
            }
        } catch (InputMismatchException e) {
            _inp.nextLine();
        }
    }

    /** Gather arguments and execute command CMND.  Throws GameException
     *  on errors. FIX AFTER BREAK */
    private void executeCommand(String cmnd) {
        try {
            switch (cmnd) {
            case "\n": case "\r\n":
                return;
            case "#":
                break;
            case "dump":
                dump();
                break;
            case "set":
                setSpots(_inp.nextInt(), _inp.nextInt(), _inp.nextInt(),
                    _inp.next("[rRbB]"));
                break;
            case "auto":
                setAuto(readColor());
                break;
            case "manual":
                setManual(readColor());
                break;
            case "size":
                setSize(_inp.nextInt());
                break;
            case "move":
                setMoveNumber(_inp.nextInt());
                break;
            case "start":
                restartGame();
                break;
            case "clear":
                clear();
                break;
            case "quit":
                System.exit(0);
                break;
            case "seed":
                setSeed(_inp.nextLong());
                break;
            case "help":
                help();
                break;
            default:
                reportError("bad command: '%s'", cmnd);
                _inp.nextLine();
            }
        } catch (InputMismatchException e) {
            reportError("bad command: '%s'", cmnd);
            _inp.nextLine();
        }
    }

    /** Returns true iff S is a int of string. */
    public boolean isNumeric(String s) {
        return s.matches("[+-]?\\d*\\.?\\d+");
    }

    /** Print a prompt and wait for input. Returns true iff there is another
     *  token. NEEds FIX*/
    private boolean promptForNext() {
        if (_playing) {
            message("%s> ", _board.whoseMove());
        } else {
            message("> ");
        }
        return _inp.hasNext();
    }

    /** Send an error message to the user formed from arguments FORMAT
     *  and ARGS, whose meanings are as for printf. */
    void reportError(String format, Object... args) {
        _err.print("Error: ");
        _err.printf(format, args);
        _err.println();
    }

    /** Writer on which to print prompts for input. */
    private final PrintWriter _prompter;
    /** Scanner from current game input.  Initialized to return
     *  newlines as tokens. */
    private final Scanner _inp;
    /** Outlet for responses to the user. */
    private final PrintWriter _out;
    /** Outlet for error responses to the user. */
    private final PrintWriter _err;

    /** The board on which I record all moves. */
    private final Board _board;
    /** A readonly view of _board. */
    private final Board _readonlyBoard;

    /** A pseudo-random number generator used by players as needed. */
    private final Random _random = new Random();

    /** True iff a game is currently in progress. MORE INST VARS*/
    private boolean _playing;
    /** player 1 of game. */
    private Player p1;
    /** player 2 of game. */
    private Player p2;

   /** Used to return a move entered from the console.  Allocated
     *  here to avoid allocations. */
    private final int[] _move = new int[2];
}
