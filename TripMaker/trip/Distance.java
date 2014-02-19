package trip;

/** Represents the paths of map.
 *  @author Shin-Yen Huang
 */
public class Distance {

    /** Constructor for Distance that takes in S R DIST DIR DEST. */
    public Distance(String s, String r, Float dist,
        String dir, String dest) {
        _s = s;
        _r = r;
        _dist = dist;
        _dir = dir;
        _dest = dest;
    }

    /** Returns name of path. */
    public String getRoad() {
        return _r;
    }

    /** Returns start location. */
    public String getStart() {
        return _s;
    }

    /** Returns length of path.. */
    public Float getDist() {
        return _dist;
    }

    /** Sets DISTANCE of path. */
    public void setDist(Float distance) {
        _dist = distance;
    }

    /** Returns direction of path. */
    public String getDirection() {
        return _dir;
    }

    /** Returns path destination. */
    public String getDest() {
        return _dest;
    }

    @Override
    public String toString() {
        return _s + " to " + _dest;
    }

    /**  start. */
    private String _s;
    /**  name. */
    private String _r;
    /**  length. */
    private Float _dist;
    /**  direction. */
    private String _dir;
    /**  destination. */
    private String _dest;
}
