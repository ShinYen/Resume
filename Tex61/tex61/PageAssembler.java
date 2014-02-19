package tex61;

import static tex61.FormatException.reportError;

/** A PageAssembler accepts complete lines of text (minus any
 *  terminating newlines) and turns them into pages, adding form
 *  feeds as needed.  It prepends a form feed (Control-L  or ASCII 12)
 *  to the first line of each page after the first.  By overriding the
 *  'write' method, subtypes can determine what is done with
 *  the finished lines.
 *  @author Shin-Yen Huang
 */
abstract class PageAssembler {

    /** Create a new PageAssembler that sends its output to OUT.
     *  Initially, its text height is unlimited. It prepends a form
     *  feed character to the first line of each page except the first. */
    PageAssembler() {
        _textHeight = Integer.MAX_VALUE;
        _currentHeight = 0;
    }

    /** Add LINE to the current page, starting a new page with it if
     *  the previous page is full. A null LINE indicates a skipped line,
     *  and has no effect at the top of a page. */
    void addLine(String line) {
        if (_currentHeight == _textHeight) {
            if (!line.equals("")) {
                line = "\f" + line;
                write(line);
                _currentHeight = 1;
            }
        } else if (_currentHeight != 1 && line.equals("")) {
            write("");
            _currentHeight += 1;
        } else {
            _currentHeight += 1;
            write(line);
        }
    }

    /** Set text height to VAL, where VAL > 0. */
    void setTextHeight(int val) {
        if (val <= 0) {
            reportError("Height less than 0: %d", val);
        }
        _textHeight = val;
    }

    /** Perform final disposition of LINE, as determined by the
     *  concrete subtype. */
    abstract void write(String line);
    /** an index of current height. */
    protected int _currentHeight;
    /** the max height of the page. */
    protected int _textHeight;
    /** the string of the page. */
    protected String _pages;
}
