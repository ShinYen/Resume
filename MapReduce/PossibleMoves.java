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
import java.util.Arrays;

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

public class PossibleMoves {
    public static class Map extends Mapper<IntWritable, MovesWritable, IntWritable, IntWritable> {
        int boardWidth;
        int boardHeight;
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
            OTurn = context.getConfiguration().getBoolean("OTurn", true);
        }

        /**
         * The map function for the first mapreduce that you should be filling out.
         */
        @Override
        public void map(IntWritable key, MovesWritable val, Context context) throws IOException, InterruptedException {
            String sKey = Proj2Util.gameUnhasher(key.get(), boardWidth, boardHeight);
            char[] sArray = sKey.toCharArray();
            char whoseTurn;
            if (OTurn) {
                whoseTurn = 'O';
            } else {
                whoseTurn = 'X';
            }
            /*
            int[] temp = val.getMoves();
            int[] parentMoves = new int[temp.length + 1];
            System.arraycopy(temp, 0, parentMoves, 0, temp.length);
            parentMoves[temp.length] = key.get(); */
            for(int i = 0; i < sArray.length; i++) { 
                char curr = sArray[i];
                if (curr == ' ' && (val.getStatus() == 0)) {
                    char[] tempS = new char[sArray.length];
                    System.arraycopy(sArray, 0, tempS, 0, sArray.length);
                    tempS[i] = whoseTurn;
                    i += boardHeight - (i % boardHeight) - 1;
                    String newKey = new String();
                    for (char chars: tempS) {
                        newKey += chars;
                    }
                    int newHash = Proj2Util.gameHasher(newKey, boardWidth, boardHeight);
                    IntWritable val2 = new IntWritable(newHash);
                    context.write(val2, key);
                }
            }
        }
    }

    public static class Reduce extends Reducer<IntWritable, IntWritable, IntWritable, MovesWritable> {

        int boardWidth;
        int boardHeight;
        int connectWin;
        boolean OTurn;
        boolean lastRound;
        /**
         * Configuration and setup that occurs before reduce gets called for the first time.
         *
         **/
        @Override
        public void setup(Context context) {
            // load up the config vars specified in Proj2.java#main()
            boardWidth = context.getConfiguration().getInt("boardWidth", 0);
            boardHeight = context.getConfiguration().getInt("boardHeight", 0);
            connectWin = context.getConfiguration().getInt("connectWin", 0);
            OTurn = context.getConfiguration().getBoolean("OTurn", true);
            lastRound = context.getConfiguration().getBoolean("lastRound", true);
        }

        /**
         * The reduce function for the first mapreduce that you should be filling out.
         */
        @Override
        public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            Iterator<IntWritable> iterVal = values.iterator();
            List<Integer> parents = new ArrayList<Integer>();
			String sKey = Proj2Util.gameUnhasher(key.get(), boardWidth, boardHeight);
            while (iterVal.hasNext()) {
            	int test = iterVal.next().get();
            	String tester= Proj2Util.gameUnhasher(test, boardWidth, boardHeight);
                parents.add(test);
            }
            int status;
            String currBoard= Proj2Util.gameUnhasher(key.get(), boardWidth, boardHeight);
            boolean winner = Proj2Util.gameFinished(currBoard, boardWidth, boardHeight, connectWin);
            if (lastRound || winner) {
                if (winner) {
                    if (OTurn) {
                        status = 1;
                    } else {
                        status = 2;
                    }
                } else {
                    status = 3;
                }
            } else {
                status = 0;
            }
            int[] parent = new int[parents.size()];
            int c= 0;
            for (Integer x: parents) {
                parent[c] = x.intValue();
                c += 1;
            }
            MovesWritable currMove = new MovesWritable(status, 0, parent);
            context.write(key, currMove);
        }
        
    }
}
