package trip;

/** Represents each location of a map.
 *  @author Shin-Yen Huang
 */
public class Location {

    /** Location C at (X, Y). */
    public Location(String c, Float x, Float y) {
        _place = c;
        _x = x;
        _y = y;
    }

    /** Returns location name. */
    public String getName() {
        return _place;
    }

    /** Returns location X coordinate. */
    public Float getX() {
        return _x;
    }

    /** Returns location Y coordinate. */
    public Float getY() {
        return _y;
    }

    /** Sets location WEIGHT. */
    public void setWeight(double weight) {
        _weight = weight;
    }

    /** Returns location weight. */
    public double getWeight() {
        return _weight;
    }

    @Override
    public String toString() {
        return _place;
    }

    /** Location name. */
    private String _place;

    /** Location X coordinate. */
    private Float _x;

    /** Location Y coordinate. */
    private Float _y;

    /** Location weight. */
    private double _weight;

}
