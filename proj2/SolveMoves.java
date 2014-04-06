/*
 * CS61C Spring 2014 Project2
 * Reminders:
 *
 * DO NOT SHARE CODE IN ANY WAY SHAPE OR FORM, NEITHER IN PUBLIC REPOS OR FOR DEBUGGING.
 *
 * This is one of the two files that you should be modifying and submitting for this project.
 */
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;
import java.util.ArrayList;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class SolveMoves {
    public static class Map extends Mapper<IntWritable, MovesWritable, IntWritable, ByteWritable> {
        /**
         * Configuration and setup that occurs before map gets called for the first time.
         *
         **/
        @Override
        public void setup(Context context) {
        }

        /**
         * The map function for the second mapreduce that you should be filling out.
         */
        @Override
        public void map(IntWritable key, MovesWritable val, Context context) throws IOException, InterruptedException {
            byte output = (byte) val.getValue();
            ByteWritable writeOutput = new ByteWritable(output);
            for (int move: val.getMoves()) {
                int end = val.getMovesToEnd();
                IntWritable writeMove = new IntWritable(move);
            	String sKey = Proj2Util.gameUnhasher(writeMove.get(), 2, 2);
                context.write(writeMove, writeOutput);
            }
        }
    }

    public static class Reduce extends Reducer<IntWritable, ByteWritable, IntWritable, MovesWritable> {

        int boardWidth;
        int boardHeight;
        int connectWin;
        boolean OTurn;
        /**
         * Configuration and setup that occurs before map gets called for the first time.
         *
         **/
        @Override
        public void setup(Context context) {
            // load up the config vars specified in Proj2.java#main()
            boardWidth = context.getConfiguration().getInt("boardWidth", 0);
            boardHeight = context.getConfiguration().getInt("boardHeight", 0);
            connectWin = context.getConfiguration().getInt("connectWin", 0);
            OTurn = context.getConfiguration().getBoolean("OTurn", true);
        }
        /** Makes all possible parents, including invalid ones, and returns them in an
        * ArrayList of Strings
        **/
        public ArrayList<String> makeParents(String board) {
        	char wantRemove;
        	if (OTurn) {
        		wantRemove = 'X';
        	} else {
        		wantRemove = 'O';
        	}
        	ArrayList<String> parents = new ArrayList<String>();
        	char[] boardArray = board.toCharArray();
        	for (int i = 1; i <= boardWidth; i++) {
        		for (int j = i * boardHeight - 1; j >= 0; j--) {
        			if (boardArray[j] == ' ') {
        				continue;
        			} else if (boardArray[j] == wantRemove) {
        				boardArray[j] = ' ';
        				String parentBoard = "";
        				for (char chars: boardArray) {
        					parentBoard += chars;
        				}
        				boardArray[j] = wantRemove;
        				parents.add(parentBoard);
        			}
        			break;		
        		}
        	}
        	return parents;
        }
        
        /** Converts our ArrayList<String> into an int[] of length boardWidth, that is
        * passed to our movesWritable.
        **/
        public int[] convertParents(ArrayList<String> parent) {
        	int counter = 0;
        	int[] finalParents = new int[parent.size()];
        	for (String stringParent: parent) {
        		finalParents[counter] = Proj2Util.gameHasher(stringParent, boardWidth, boardHeight);
        		counter += 1;
        	}
        	return finalParents;
        }
        

        /**
         * The reduce function for the first mapreduce that you should be filling out.
         */
        @Override
        public void reduce(IntWritable key, Iterable<ByteWritable> values, Context context) throws IOException, InterruptedException {
			byte bestMove;
            int whoseTurn;
            if (OTurn) {
                whoseTurn = 1;
                bestMove = 2;
            } else {
                whoseTurn = 2;
                bestMove = 1;
            }
            boolean valid = false;
            boolean win = false;
            boolean tie = false;
            for (ByteWritable curValB : values) {
            	if ((curValB.get() >>> 2) == 0) {
            		valid = true;
            	}
            	byte curVal = curValB.get();
            	int status = curVal & 3;
            	int bestStatus = bestMove & 3;
            	if (status == 0) {
            		continue;
            	} else if (status == whoseTurn && win) { //if its a win and we had a win before
            		if (curVal < bestMove) {
            			bestMove = curVal;
            		}
				} else if (status == whoseTurn && !win) { //if its a win and we didn't have a win
					bestMove = curVal;
					win = true;
				} else if (status == 3 && win) { //If its a tie but we had a win before
					continue;
				} else if (status == 3 && !win && tie) { //If its a tie and we had a tie before
					if (curVal > bestMove) {
						bestMove = curVal;
					}
            	} else if (status == 3 && !win && !tie) { //if its a tie and we were losing before
            		bestMove = curVal;
            		tie = true;
            	} else if (status != whoseTurn && !win && !tie) { //if we were losing and we were losing before
            		if (curVal > bestMove) {
            			bestMove = curVal;
            		} 
            	}
            }	
            if (!valid) {
            	return;
            }
            byte finalBestMove = bestMove;
            byte four = (byte) 4;
            finalBestMove += four;
            String sKey = Proj2Util.gameUnhasher(key.get(), boardWidth, boardHeight);
            ArrayList<String> parents = makeParents(sKey);
            int[] finalParents = convertParents(parents);
            MovesWritable move = new MovesWritable(finalBestMove, finalParents);
            context.write(key, move);
        }
    }
}
