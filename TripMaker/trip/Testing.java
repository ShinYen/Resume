package trip;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your trip package per se (that is, it must be
 * possible to remove them and still have your package work). */

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

/** Unit tests for the trip package. */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(trip.Testing.class));
    }

    @Test
    public void test1() {
        Location l = new Location("San Franscisco", 0.0f, 0.0f);
        assertEquals("Name", "San Franscisco", l.getName());
    }

    @Test
    public void distanceTest() {
        Distance d = new Distance("Japan", "SilkRoad", 3.4f, "N", "China");
        assertEquals("Start", "Japan", d.getStart());
        assertEquals("Road", "SilkRoad", d.getRoad());
        assertEquals("End", "China", d.getDest());
        assertEquals("Direction", "N", d.getDirection());
    }
}
