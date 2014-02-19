Tex61
=====

Text Editor we created in my 61b class (with Hilfinger)
Although this was a class project, almost all the code was written by me.

See tests for examples of how the text editor works

This commmand will output onto the standard output:

java tex61.Main INPUTFILE.tx

This command will output into thee OUTPUTFILE:

java tex61.Main INPUTFILE.tx OUTPUTFILE

Commands
=====

The commands include:

\indent{N} - Set the current indentation to N spaces, effective no later than the first line of the next paragraph that                is output ￼￼￼￼after processing this command.

\parindent{N} - Set the current paragraph indentation to N spaces, effective effective no later than the first line of the                 next paragraph that is output after processing this command. Subsequent paragraphs will be subject to this                 much indentation in addition to that set by \indent.

\textwidth{N} - Set the current text width to N. A line is considered full (for purposes of filling) when the number of                   characters on it (including indentation) is equal to N. This command takes effect no later than the next                  line of output that is started after this command is processed.

\textheight{N} -Set the current text height to N. This is the maximum number of lines that may appear on a page before
                the next page starts (if the current page already has too many lines,a new page thus starts immediately).                 This takes  effect no later than the next page of output that starts after the command is processed.

\parskip{N} - Insert N blank lines in front of each subsequent paragraph. Blank lines that would go at the top of a page 
              as a result are skipped. This command takes effect when (not before) the first line of the next paragraph is
              output after this command is processed. Thus, putting a \parskip{3} outputs three lines before the first 
              line of the next paragraph whether the \parskip occurs before or after the blank line marking the end of the               preceding paragraph. 
              
\\\ - A backslash character.

\\{ - A left curly brace character.

\\} - A right curly brace character.

\nofill - Stop filling and justifying lines. Upon encountering a line (or paragraph) termination, output all accumulated 
          text since the last output line, with words separated by single blanks, subject to the usual indentation. The 
          text width is ignored. This takes effect no later than the start of the next paragraph.
          
\fill - Fill lines as usual (and justify, if specified). This takes effect no later than the start of the next paragraph.

\justify - Commence justifying lines whenever in fill mode.

\nojustify - Stop justifying text (even when filling).

\endnote{TEXT} - Suspend current accumulation of ordinary text and add a new endnote containing TEXT (can next other 
                commmands within curly brackets.
