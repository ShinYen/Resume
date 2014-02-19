package tex61;

import java.util.ArrayList;

import static tex61.FormatException.reportError;

/** An object that receives a sequence of words of text and formats
 *  the words into filled and justified text lines that are sent to a receiver.
 *  @author Shin-Yen Huang
 */
class LineAssembler {

    /** A new, empty line assembler with default settings of all
     *  parameters, sending finished lines to PAGES. */
    LineAssembler(PageAssembler pages) {
        _pages = pages;
        _pages.setTextHeight(Defaults.TEXT_HEIGHT);
        _textHeight = Defaults.TEXT_HEIGHT;
        _textWidth = Defaults.TEXT_WIDTH;
        _indentation = Defaults.INDENTATION;
        _paragraphIndentation = Defaults.PARAGRAPH_INDENTATION;
        _paragraphSkip = Defaults.PARAGRAPH_SKIP;
        _fillMode = true;
        _words = new ArrayList<String>();
        _justifyMode = true;
        _firstLine = true;
        _lastLine = false;
        _veryFirstLine = true;
    }

    /** Add TEXT to the word currently being built. */
    void addText(String text) {
        _buildingWord += text;
    }

    /** Finish the current word, if any, and add to words being accumulated. */
    void finishWord() {
        if (!_buildingWord.equals("")) {
            addWord(_buildingWord);
            _buildingWord = "";
        }
    }

    /** Add WORD to the formatted text. */
    void addWord(String word) {
        if (_fillMode) {
            int result = _chars + word.length() + _words.size() + _indentation;
            result -= 1;
            if (_firstLine) {
                result += _paragraphIndentation;
            }
            if (result == _textWidth) {
                addLine(stringer(_words));
                _words.add(word);
                _chars = word.length();
            } else if (result < _textWidth) {
                _chars += word.length();
                _words.add(word);
            } else {
                addLine(stringer(_words));
                if (word.length() > _textWidth) {
                    _words.add(word);
                    addLine(word);
                    _chars = 0;
                } else {
                    _chars = word.length();
                    _words.add(word);
                }
            }
        } else {
            _words.add(word);
        }
    }

     /** Process the end of the current input line.  No effect if
     *  current line accumulator is empty or in fill mode.  Otherwise,
     *  adds a new complete line to the finished line queue and clears
     *  the line accumulator. */
    void newLine() {
        if (!_words.isEmpty()) {
            finishWord();
            addLine(stringer(_words));
        }
    }

    /** Add LINE to our output, with no preceding paragraph skip.  There must
     *  not be an unfinished line pending. */
    void addLine(String line) {
        if (_firstLine && !(_words.isEmpty())) {
            for (int i = 0; i < _paragraphIndentation + _indentation; i++) {
                String temp = _words.get(0);
                _words.set(0, " " + temp);
            }
            if (!_veryFirstLine) {
                for (int j = 0; j < _paragraphSkip; j++) {
                    _pages.addLine("");
                }
            }
            _chars += _paragraphIndentation;
            _veryFirstLine = false;
        }
        if (_fillMode && _justifyMode) {
            outputLine(_lastLine);
        } else if (_fillMode) {
            if (_lastLine) {
                emitLine(_indentation, 1);
                _lastLine = false;
                _firstLine = true;
            } else {
                emitLine(_indentation, 1);
            }
        } else {
            emitLine(0, 1);
        }
    }

    /** Set the current indentation to VAL. VAL >= 0. */
    void setIndentation(int val) {
        if (val < 0) {
            reportError("Indent less than 0: %d", val);
        }
        _indentation = val;
    }

    /** Set the current paragraph indentation to VAL. VAL >= 0. */
    void setParIndentation(int val) {
        if (val + _indentation < 0) {
            reportError("ParInd - Ind < 0: %d", val + _indentation);
        }
        _paragraphIndentation = val;
    }

    /** Set the text width to VAL, where VAL >= 0. */
    void setTextWidth(int val) {
        if (val < 0) {
            reportError("TextWidth set less than 0: %d", val);
        }
        _textWidth = val;
    }

    /** Iff ON, set fill mode. */
    void setFill(boolean on) {
        _fillMode = on;
    }

    /** Iff ON, set justify mode (which is active only when filling is
     *  also on). */
    void setJustify(boolean on) {
        _justifyMode = on;
    }

    /** Set paragraph skip to VAL.  VAL >= 0. */
    void setParSkip(int val) {
        _paragraphSkip = val;
    }

    /** Set page height to VAL > 0. */
    void setTextHeight(int val) {
        if (val <= 0) {
            reportError("Height not greater than 0: %d", val);
        }
        _textHeight = val;
    }

    /** If there is a current unfinished paragraph pending, close it
     *  out and start a new one. */
    void endParagraph() {
        _lastLine = true;
        addLine(stringer(_words));
        _firstLine = true;
        _lastLine = false;
    }

    /** Transfer contents of _words to _pages, adding INDENT characters of
     *  indentation, and a total of SPACES spaces between words, evenly
     *  distributed.  Assumes _words is not empty.  Clears _words and _chars. */
    private void emitLine(int indent, int spaces) {
        if (!_firstLine) {
            for (int i = 0; i < indent; i++) {
                String temp = _words.get(0);
                _words.set(0, " " + temp);
            }
        }
        for (int j = 0; j < _words.size() - 1; j++) {
            for (int k = 0; k < spaces; k++) {
                String temp1 = _words.get(j);
                _words.set(j, temp1 + " ");
            }
        }
        _firstLine = false;
        _pages.addLine(stringer(_words));
        _words.clear();
        _chars = 0;
    }

    /** returns the String version of the LIST. */
    private static String stringer(ArrayList<String> list) {
        String line = "";
        for (String item : list) {
            line += item;
        }
        return line;
    }

    /** If the line accumulator is non-empty, justify its current
     *  contents, if needed, add a new complete line to _pages,
     *  and clear the line accumulator. LASTLINE indicates the last line
     *  of a paragraph. */
    private void outputLine(boolean lastLine) {
        int previousSpace = 0;
        if (_lastLine) {
            emitLine(_indentation, 1);
            _lastLine = false;
            _firstLine = true;
        } else {
            int spaces;
            if (!_firstLine) {
                for (int i = 0; i < _indentation; i++) {
                    String temp = _words.get(0);
                    _words.set(0, " " + temp);
                }
            }
            for (int k = 1; k < _words.size(); k++) {
                spaces = justifyFormula(k) - previousSpace;
                previousSpace += spaces;
                for (int i = 0; i < spaces; i++) {
                    String temp = _words.get(k);
                    _words.set(k, " " + temp);
                }
            }
            _firstLine = false;
            _pages.addLine(stringer(_words));
            _words.clear();
            _chars = 0;
        }
    }

    /** input K and returns the result of the Bk/N-1. */
    private int justifyFormula(int k) {
        double numOfSpaces = _words.size() - 1;
        double B = _textWidth - _chars - _indentation;
        if (B >= (3 * numOfSpaces)) {
            return 3;
        } else {
            double ans = .5 + (B * (k / numOfSpaces));
            return (int) ans;
        }
    }

    /** checks if its a lastline of the page. */
    private boolean _theLastLine;
    /** Destination given in constructor for formatted lines. */
    private final PageAssembler _pages;
    /** True if in justify mode. */
    private boolean _justifyMode;
    /** True if in fill mode. */
    private boolean _fillMode;
    /** Number of next endnote. */
    private int _refNum;
    /** instance var of textHeight. */
    private int _textHeight;
    /** instance var of textWidth. */
    private int _textWidth;
    /** instance var indentation. */
    private int _indentation;
    /** instance var of paragraph. */
    private int _paragraphIndentation;
    /** instance var of paragraph skip. */
    private int _paragraphSkip;
    /** checks to see if its the first line. */
    private boolean _veryFirstLine;
    /** building word for addword. */
    private String _buildingWord = "";
    /** counts the number of characters. */
    private int _chars;
    /** the line accumulator. */
    private ArrayList<String> _words;
    /** Checks if its the lastline of the paragraph. */
    private boolean _lastLine;
    /** checks if its the firstline of the paragraph. */
    private boolean _firstLine;

}

