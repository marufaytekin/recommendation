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
        String separator = "\\t";

        int topN = 20;
        int y = 5;
        int kNN = 20;
        int numOfRun = 10;
        int smoothRun = 5;
        int l = 5; // number of bands
        int k = 6; // number of hash functions

        if (args.length != 2) {
            printHelpAndExit();
        }
        dataFilePath = args[0];
        dataFileBase = args[0];
        String test = args[1];

        TestDriver.runSelection(test, dataFilePath, dataFileBase, scanner, separator, numOfRun, smoothRun, kNN, k, l, y, topN);

    }

    private static void printHelpAndExit(){
        System.out.println("Usage: java -jar lsh-jar-with-dependencies.jar <dataFileBase> <test_type:100|101>");
        System.out.println("===========================================");
        System.out.println("dataFileBase is full path to the file base");
        System.out.println("test_type 100 for all prediction tests");
        System.out.println("test_type 101 for all top-n recommendation tests");
        System.exit(0);

    }

}