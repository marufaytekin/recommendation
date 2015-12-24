package com.zaana.ml;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;



public final class MainRun
{
    static Logger LOG = Logger.getLogger(MainRun.class);
    private MainRun() {
    }

    static HashMap<String, HashMap<String, Integer>> userRateMap = null;
    private static HashMap<String, HashMap<String, Integer>> itemRateMap = null;
    private static HashMap<String, HashMap<String, Integer>> testDataMap = null;

    static String dataFilePath = "data/ymusic/ymusic.data";
    static String dataFileBase = "data/ymusic/ymusic";
    //static String dataFilePath = "data/100k/ml.data";
    //static String dataFileBase = "data/100k/ml";
    //static String dataFilePath = "data/1m/ml-1m.data";
    //static String dataFileBase = "data/1m/ml1m";
    static final String seperator = "\\t";
    static int topN = 20;
    static int kNN = 20;
    static int y = 5; // significance value. Must not be 0!
    static int numOfRun = 20;
    static final int smoothRun = 3;
    // l: number of bands
    // k: number of hash functions
    static int l = 5;
    static int k = 6;

    public static void main(final String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        String selection = null;
        do {
            System.out.println("");
            System.out.println("Options");
            System.out.println("===================================");
            System.out.println("0 - preprocess train/test data sets");
            System.out.println("1 - Set k-NN ( " + kNN + " )");
            System.out.println("");
            System.out.println("Parameter CV Tests");
            System.out.println("51 - User-based - Prediction 2D (y & k) test");
            System.out.println("53 - Item-based - Prediction 2D (y & k) test");
            System.out.println("62 - UBLSH - Prediction - 2D (y & k) test");
            System.out.println("80 - IBLSH - Prediction - 2D (y & k) test");
            System.out.println("");
            System.out.println("54 - UBLSH - Prediction - 2D (Hash Functions & k) test");
            System.out.println("60 - UBLSH - Prediction - 2D (Hash Tables & k) test");
            System.out.println("");
            System.out.println("81 - IBLSH - Prediction - 2D (Hash Functions & k) test");
            System.out.println("82 - IBLSH - Prediction - 2D (Hash Tables & k) test");
            System.out.println("");
            System.out.println("63 - LSH - Prediction - HashTables change ( inc. by 1 )");
            System.out.println("64 - LSH - Prediction - HashFunctions change ( inc. by 1 )");
            System.out.println("65 - LSH - Prediction - 2D test");
            System.out.println("");
            System.out.println("70 - User-based - Precision vs. k");
            System.out.println("71 - Item-based - Precision vs. k");
            System.out.println("72 - UBLSH - Precision - 2D - (HashFunctions & k) test");
            System.out.println("73 - UBLSH - Precision - 2D - (HashTables & k) test");
            System.out.println("");
            System.out.println("92 - IBLSH - Precision - 2D - (HashFunctions & k) test");
            System.out.println("93 - IBLSH - Precision - 2D - (HashTables & k) test");
            System.out.println("");
            System.out.println("Experimental Tests");
            System.out.println("10 - Model Build Time - All");
            System.out.println("");
            System.out.println("100 - User-based - Prediction - test");
            System.out.println("101 - Item-based - Prediction - test");
            System.out.println("59 - UBLSH - Prediction - 2D test");
            System.out.println("83 - IBLSH - Prediction - 2D test");
            System.out.println("");
            System.out.println("102 - User-based - Precision - test");
            System.out.println("103 - Item-based - Precision - test");
            System.out.println("104 - UBLSH - Precision - 2D - test");
            System.out.println("105 - IBLSH - Precision - 2D - test");
            System.out.println("");
            System.out.println("99 - Exit");
            System.out.println("===================================");
            System.out.println("Enter a command number to execute:");

            selection = scanner.nextLine();
            LOG.info("Selected menu: " + selection);
            Run.runSelection(selection, dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

        } while (!selection.equals("99"));

        scanner.close();

    }

}
