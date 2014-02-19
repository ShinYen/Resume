package make;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your make package per se (that is, it must be
 * possible to remove them and still have your package work). */

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

/** Unit tests for the make package. */
public class Testing {

    /** Run all JUnit tests in the make package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(make.Testing.class));
    }

    @Test
    public void ruleTest() {
        Rules g = new Rules("hi");
        g.addP("cool");
        g.addP("beans");
        g.add("I love CS!");
        assertEquals("Header of Rules", "hi", g.getT());
        assertEquals("prereq check", "cool", g.getP().get(0));
        assertEquals("prereq check", "beans", g.getP().get(1));
        assertEquals("Header of Rules", "I love CS!", g.getCommands().get(0));
    }

    @Test
    public void ruleTest1() {
        Rules g = new Rules("bye");
        g.addP("beast");
        g.addP("company");
        g.add("I like CS!");
        assertEquals("Header of Rules", "bye", g.getT());
        assertEquals("prereq check", "beast", g.getP().get(0));
        assertEquals("prereq check", "company", g.getP().get(1));
        assertEquals("Header of Rules", "I like CS!", g.getCommands().get(0));
    }
}
