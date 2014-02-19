package tex61;

import java.io.PrintWriter;
import java.util.ArrayList;

import static tex61.FormatException.reportError;


/** Receives (partial) words and commands, performs commands, and
 *  accumulates and formats words into lines of text, which are sent to a
 *  designated PageAssembler.  At any given time, a Controller has a
 *  current word, which may be added to by addText, a current list of
 *  words that are being accumulated into a line of text, and a list of
 *  lines of endnotes.
 *  @author Shin-Yen Huang
 */
class Controller {

    /** A new Controller that sends formatted output to OUT. */
    Controller(PrintWriter out) {
        _output = out;
        _lines = new ArrayList<String>();
        _pagePrinter = new PagePrinter(_output);
        _pageCollector = new PageCollector(_lines);
        _lineAssembler = new LineAssembler(_pagePrinter);
        _endNote = new LineAssembler(_pageCollector);
        _fillMode = true;
        _endNote.setParSkip(Defaults.ENDNOTE_PARAGRAPH_SKIP);
        _endNote.setIndentation(Defaults.ENDNOTE_INDENTATION);
        _endNote.setParIndentation(Defaults.ENDNOTE_PARAGRAPH_INDENTATION);
        _endNote.setTextWidth(Defaults.ENDNOTE_TEXT_WIDTH);
        _endnoteMode = false;
    }

    /** Add TEXT to the end of the word of formatted text currently
     *  being accumulated. */
    void addText(String text) {
        if (_endnoteMode) {
            _endNote.addText(text);
        } else {
            _lineAssembler.addText(text);
        }
    }

    /** adds WORD to the line collector. */
    void addWord(String word) {
        if (_endnoteMode) {
            _endNote.addWord(word);
        } else {
            _lineAssembler.addWord(word);
        }
    }

    /** Finish any current word of text and, if present, add to the
     *  list of words for the next line.  Has no effect if no unfinished
     *  word is being accumulated. */
    void endWord() {
        if (_endnoteMode) {
            _endNote.finishWord();
        } else {
            _lineAssembler.finishWord();
        }
    }

    /** Finish any current word of formatted text and process an end-of-line
     *  according to the current formatting parameters. */
    void addNewline() {
        if (_fillMode) {
            endWord();
        } else {
            if (_endnoteMode) {
                _endNote.newLine();
            } else {
                _lineAssembler.newLine();
            }
        }
    }

    /** Finish any current word of formatted text, format and output any
     *  current line of text, and start a new paragraph. */
    void endParagraph() {
        endWord();
        if (_endnoteMode) {
            _endNote.endParagraph();
        } else {
            _lineAssembler.endParagraph();
        }
    }

    /** If valid, process TEXT into an endnote, first appending a reference
     *  to it to the line currently being accumulated. */
    void formatEndnote(String text) {
        if (text.contains("\\endnote")) {
            reportError("Nested endnotes: %s", text);
        }
        _refNum += 1;
        addText(String.format("[%d]", _refNum));
        setEndnoteMode();
        String endinp = String.format("[%d]\\ %s\n", _refNum, text);
        _endParser = new InputParser(endinp, this);
        _endParser.process();
        setNormalMode();
    }

    /** Set the current text height (number of lines per page) to VAL, if
     *  it is a valid setting.  Ignored when accumulating an endnote. */
    void setTextHeight(int val) {
        if (_endnoteMode) {
            _endNote.setTextHeight(val);
        } else {
            _lineAssembler.setTextHeight(val);
        }
    }

    /** Set the current text width (width of lines including indentation)
     *  to VAL, if it is a valid setting. */
    void setTextWidth(int val) {
        if (_endnoteMode) {
            _endNote.setTextWidth(val);
        } else {
            _lineAssembler.setTextWidth(val);
        }
    }

    /** Set the current text indentation (number of spaces inserted before
     *  each line of formatted text) to VAL, if it is a valid setting. */
    void setIndentation(int val) {
        if (_endnoteMode) {
            _endNote.setIndentation(val);
        } else {
            _lineAssembler.setIndentation(val);
        }
    }

    /** Set the current paragraph indentation (number of spaces inserted before
     *  first line of a paragraph in addition to indentation) to VAL, if it is
     *  a valid setting. */
    void setParIndentation(int val) {
        if (_endnoteMode) {
            _endNote.setParIndentation(val);
        } else {
            _lineAssembler.setParIndentation(val);
        }
    }

    /** Set the current paragraph skip (number of blank lines inserted before
     *  a new paragraph, if it is not the first on a page) to VAL, if it is
     *  a valid setting. */
    void setParSkip(int val) {
        if (_endnoteMode) {
            _endNote.setParSkip(val);
        } else {
            _lineAssembler.setParSkip(val);
        }
    }

    /** Iff ON, begin filling lines of formatted text. */
    void setFill(boolean on) {
        _fillMode = on;
        if (_endnoteMode) {
            _endNote.setFill(on);
        } else {
            _lineAssembler.setFill(on);
        }
    }

    /** Iff ON, begin justifying lines of formatted text whenever filling is
     *  also on. */
    void setJustify(boolean on) {
        if (_endnoteMode) {
            _endNote.setJustify(on);
        } else {
            _lineAssembler.setJustify(on);
        }
    }

    /** Finish the current formatted document or endnote (depending on mode).
     *  Formats and outputs all pending text. */
    void close() {
        if (!_endnoteMode) {
            writeEndnotes();
            _output.close();
        }
    }

    /** Start directing all formatted text to the endnote assembler. */
    private void setEndnoteMode() {
        _endnoteMode = true;
    }

    /** Return to directing all formatted text to _mainText. */
    private void setNormalMode() {
        _endnoteMode = false;
    }

    /** Write all accumulated endnotes to _mainText. */
    private void writeEndnotes() {
        for (String n : _lines) {
            _pagePrinter.addLine(n);
        }
    }

    /** Boolean that checks if endnote mode is on. */
    private boolean _endnoteMode;
    /** the instance of the Lineasembler.  */
    private LineAssembler _lineAssembler;
    /** the instance of the _endnote assembler.  */
    private LineAssembler _endNote;
    /**the instance of the page collector. */
    private PageCollector _pageCollector;
    /** the instance of the _pageprinter. */
    private PagePrinter _pagePrinter;
    /** the output that goes to the _pagePrinter. */
    private ArrayList<String> _lines;
    /** the output of the file. */
    private PrintWriter _output;
    /** character counter. */
    private int _refNum = 0;
    /** an input parser for endnotes. */
    private InputParser _endParser;
    /** checks if fillMode is on. */
    private boolean _fillMode;
    /** instance var of endnote text width. */
    private int _endnoteTextWidth;
    /** instance var endnote paragraph skip. */
    private int _endnotePS;
    /** instance var of endnote indentation. */
    private int _endnoteI;
    /** instance var of endnote paragraphindentation. */
    private int _endnotePI;
}

