package make;

import java.util.List;
import java.util.ArrayList;

/** Rule class that contains the target, commands, and prerequisites
 *  from the makeFile.
 *  @author Shin-Yen Huang
 */

public class Rules {

    /** Constructor the the rules that makes T the target. */
    public Rules(String t) {
        _t = t;
        _p = new ArrayList<String>();
        _commands = new ArrayList<String>();
    }

    /** Empty constructor. */
    public Rules() {
        _t = "";
        _p = new ArrayList<String>();
        _commands = new ArrayList<String>();
    }

    /** Adds the COMMAND to _P. */
    public void addP(String command) {
        _p.add(command);
    }

    /** adds the lines to the command LINE. */
    public void add(String line) {
        _commands.add(line);
    }

    /** returns the T or header fo the rule. */
    public String getT() {
        return _t;
    }

    /** returns  the prereqs. */
    public List<String> getP() {
        return _p;
    }

    /** returns commands of the rule. */
    public List<String> getCommands() {
        return _commands;
    }

    /** the to string method that returns the string interpretation of rule. */
    public String toString() {
        String temp = "";
        for (String s : _commands) {
            temp += s + "\n";
        }
        return temp;
    }

    /** The t of the rule. */
    private String _t;

    /** A list of p's for the rule. */
    private List<String> _p;

    /**  commands of the rule. */
    private List<String> _commands;

}
