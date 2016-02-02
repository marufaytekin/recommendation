package com.zaana.ml;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by maytekin on 26.05.2015.
 */
public class Main {

    static Logger LOG = Logger.getLogger(Main.class);
    private Main() {
    }

    static HashMap<String, HashMap<String, Integer>> userRateMap = null;
    private static HashMap<String, HashMap<String, Integer>> itemRateMap = null;
    private static HashMap<String, HashMap<String, Integer>> testDataMap = null;

    //static String dataFilePath = "data/ymusic/ymusic.data";
    //static String dataFileBase = "data/ymusic/ymusic";
    //static String dataFilePath = "data/music/music.dat";
    //static String dataFileBase = "data/music/music";
    //static String dataFilePath = "data/100k/ml.data";
    //static String dataFileBase = "data/100k/ml";
    //static String dataFilePath = "data/movies_tv/movies_tv.data";
    //static String dataFileBase = "data/movies_tv/movies_tv";
    //static String dataFilePath = "data/300k/xaa.data";
    //static String dataFileBase = "data/300k/xaa";
    static String dataFilePath = "data/1m/ml-1m.data";
    static String dataFileBase = "data/1m/ml-1m";
    static final String seperator = "\\t";
    static int topN = 20;
    static int kNN = 20;
    static int y = 5; // significance value. Must not be 0!
    static int numOfRun = 10;
    static final int smoothRun = 3;
    // l: number of bands
    // k: number of hash functions
    static int l = 5;
    static int k = 6;

    public static void main(final String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        String selection;
        do {
            System.out.println("");
            System.out.println("Options");
            System.out.println("===================================");
            System.out.println("0 - preprocess train/test data sets");
            System.out.println("");
            System.out.println("Parameter CV Tests");
            System.out.println("50 - User-based - Prediction vs. k - test");
            System.out.println("51 - User-based - Prediction 2D (y & k) test");
            System.out.println("");
            System.out.println("100 - User-based - Prediction - test");
            System.out.println("");
            System.out.println("63 - LSH - Prediction - HashTables change ( inc. by 1 )");
            System.out.println("64 - LSH - Prediction - HashFunctions change ( inc. by 1 )");
            System.out.println("65 - LSH - Prediction - 2D test");
            System.out.println("106 - LSH - top-N Precision - 2D test");
            System.out.println("107 - LSH - top-N Evaluation - HashFunctions test");
            System.out.println("108 - LSH - top-N Evaluation - HashTables test");
            System.out.println("");
            System.out.println("Experimental Tests");
            System.out.println("10 - Model Build Time - All");
            System.out.println("11 - Create cluster");
            System.out.println("12 - Run Cluster Tests");
            System.out.println("");
            System.out.println("99 - Exit");
            System.out.println("===================================");
            System.out.println("Enter a command number to execute:");

            selection = scanner.nextLine();
            LOG.info("Selected menu: " + selection);
            TestDriver.runSelection(selection, dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

        } while (!selection.equals("99"));

        scanner.close();

    }

}