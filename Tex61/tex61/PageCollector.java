package tex61;

import java.util.List;

/** A PageAssembler that collects its lines into a designated List.
 *  @author Shin-Yen Huang
 */
class PageCollector extends PageAssembler {

    /** A new PageCollector that stores lines in OUT. */
    PageCollector(List<String> out) {
        _out = out;
    }

    /** Add LINE to my List. */
    @Override
    void write(String line) {
        _out.add(line);
    }
    /** collects the lines. */
    private List<String> _out;
}
