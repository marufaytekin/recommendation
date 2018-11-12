package com.zaana.ml;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by maytekin on 26.05.2015.
 */
public class Main {

    static Logger LOG = Logger.getLogger(Main.class);
    private Main( ) {
    }

    static String dataFilePath;
    static String dataFileBase;

    public static void main(final String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        String seperator = "\\t";

        int topN = 20;
        int y = 5;
        int kNN = 20;
        int numOfRun = 10;
        int smoothRun = 10;
        int l = 5; // number of bands
        int k = 6; // number of hash functions
        List<String> dataList = Arrays.asList("yahoo-music", "amazon-movies-tv", "ml-1m");
        List<String> testType = Arrays.asList("100", "101");
        String test = null;

        //TestDriver.runSelection(null, dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        //System.out.println(args.length);
        //System.out.println(args[0]);
        //System.out.println(args[1]);

        if (args.length == 2) {
            if (!dataList.contains(args[0])) {
                System.out.println("first case");
                printHelpAndExit();
            }
            if (!testType.contains(args[1])){
                System.out.println("second case");
                printHelpAndExit();
            }
            dataFilePath = "data/" + args[0] + "/" + args[0];
            dataFileBase = "data/" + args[0] + "/" + args[0];
            test = args[1];

        } else {
            printHelpAndExit();
        }

        TestDriver.runSelection(test, dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);

    }

    private static void printHelpAndExit(){
        System.out.println("Usage: 'java lsh <dataFileBase> <test_type:100|101'");
        System.out.println("dataFileBase is one of following: \n" +
                        "yahoo-music\n" +
                        "amazon-movies-tv\n" +
                        "ml-1m\n" +
                "android-apps\n" +
                "ml-1m-new"
        );
        System.out.println("est_type 100 for all prediction tests");
        System.out.println("est_type 101 for all top-n recommendation tests");
        System.exit(0);

    }

}