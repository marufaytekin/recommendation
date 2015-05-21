package com.zaana.ml;

import java.io.IOException;
import java.util.*;

import com.zaana.ml.tests.CFPredictionTests;
import com.zaana.ml.tests.LSHPredictionTests;
import com.zaana.ml.tests.ModelBuildTimeTest;
import com.zaana.ml.tests.PrecisionTests;
import org.apache.log4j.Logger;



public final class Main
{
    static Logger LOG = Logger.getLogger(Main.class);
    private Main() {
    }

    private static HashMap<String, HashMap<String, Integer>> userRateMap = null;
    private static HashMap<String, HashMap<String, Integer>> itemRateMap = null;
    private static HashMap<String, HashMap<String, Integer>> testDataMap = null;

    //static String dataFilePath = "data/ymusic/ymusic.data";
    //static String dataFileBase = "data/ymusic/ymusic";
    static String dataFilePath = "data/100k/ml.data";
    static String dataFileBase = "data/100k/ml";
    //static String dataFilePath = "data/1m/ml-1m.data";
    //static String dataFileBase = "data/1m/ml1m";
    static final String seperator = "\\t";
    // “in most real-world situations, a neighborhood of 20 to 50 neighbors
    // seems reasonable” ( Herlocker et al. 2002 ).
    static int topN = 20;
    static int kNN = 20;
    static int y = 10; // significance value. Must not be 0!
    static int numOfRun = 10;
    static final int smoothRun = 3;
    // l: number of bands
    // k: number of hash functions
    static int l = 4;
    static int k = 4;

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
            System.out.println("50 - User-based - Prediction vs. k");
            System.out.println("51 - User-based - Prediction 2D (y & k) test");
            System.out.println("54 - UBLSH - Predicton - 2D (Hash Functions & k) test");
            System.out.println("55 - UBLSH - Predicton - 2D (Hash Functions & y) test");
            System.out.println("56 - UBLSH - Prediction - HashTables change ( inc. by 1 )");
            System.out.println("57 - UBLSH - Prediction - HashFunctions change ( inc. by 1 )");
            System.out.println("58 - UBLSH - Prediction & k");
            System.out.println("59 - UBLSH - Predicton - 2D test");
            System.out.println("60 - UBLSH - Predicton - 2D (Hash Tables & k) test");
            System.out.println("61 - UBLSH - Predicton - 2D (Hash Tables & y) test");
            System.out.println("62 - UBLSH - Prediction - 2D (y & k) test");
            System.out.println("63 - LSH - Prediction - HashTables change ( inc. by 1 )");
            System.out.println("64 - LSH - Prediction - HashFunctions change ( inc. by 1 )");
            System.out.println("65 - LSH - Predicton - 2D test");
            System.out.println("");
            System.out.println("70 - User-based - Precision vs. k");
            System.out.println("71 - UBLSH - Precision vs. k");
            System.out.println("72 - UBLSH - Precision - 2D - (HashFunctions & k) test");
            System.out.println("73 - UBLSH - Precision - 2D - (HashTables & k) test");
            System.out.println("74 - UBLSH - Precision vs. y");
            System.out.println("75 - UBLSH - Precision - 2D (k & y) test");
            System.out.println("76 - User-based - Precision - 2D (k & y) test");
            System.out.println("");
            System.out.println("Experimental Tests");
            System.out.println("10 - Model Build Time - All");

            System.out.println("99 - Exit");
            System.out.println("===================================");
            System.out.println("Enter a command number to execute:");

            selection = scanner.nextLine();
            LOG.info("Selected menu: " + selection);
            runSelection(selection, scanner);

        } while (!selection.equals("99"));

        scanner.close();

    }

    private static void runSelection(final String selection, Scanner scanner)
    {
        switch (selection) {

            case "0":
                dataFilePath = "data/100k/ml.data";
                dataFileBase = "data/100k/ml";
                y = 10;
                runnCompTests(scanner);
                dataFilePath = "data/ymusic/ymusic.data";
                dataFileBase = "data/ymusic/ymusic";
                y = 4;
                runnCompTests(scanner);
                break;
            case "00":
                DataParser.processDataFile(dataFilePath, seperator, 0, 100);
                userRateMap = DataParser.getUserRateMap();
                DataParser.calculateDataSetHistogram(userRateMap);
                break;

            case "1":
                System.out.println("Enter k-NN: ");
                kNN = Integer.parseInt(scanner.nextLine());
                break;
            case "2":
                System.out.println("Enter number of bands ( l ) :");
                l = Integer.parseInt(scanner.nextLine());
                System.out.println("Enter of hash functions ( k ) :");
                k = Integer.parseInt(scanner.nextLine());
                break;
            case "3":
                System.out.println("Enter data file path: ");
                dataFilePath = scanner.nextLine();
                break;

            case "10": //old 8
                ModelBuildTimeTest.runModelBuildTimeTest("UB", dataFilePath, seperator, k, l);
                ModelBuildTimeTest.runModelBuildTimeTest("UBLSH", dataFilePath, seperator, k, l);
                break;

            case "50":
                CFPredictionTests.runCFPredictionAndKTest(dataFilePath, dataFileBase, "UB", smoothRun, seperator, y);
                break;
            case "51":
                CFPredictionTests.runCFPredictionKAndY2DTest("UB", dataFileBase, numOfRun, smoothRun, seperator);
                break;
            case "52":
                CFPredictionTests.runCFPredictionAndKTest(dataFilePath, dataFileBase, "IB", smoothRun, seperator, y);
                break;

            case "54":
                LSHPredictionTests.runLSHHashFunctionsAndKTest("UBLSH",dataFileBase,"val",numOfRun,smoothRun,seperator,kNN,y);
                break;
            case "55":
                LSHPredictionTests.runLSHHashFunctionsAndYTest("UBLSH",dataFileBase,"val",numOfRun,smoothRun,seperator,kNN,y);
                break;
            case "56":
                LSHPredictionTests.runLSHHashTablesAndPrediction("UBLSH", dataFileBase, "val", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "57":
                LSHPredictionTests.runLSHHashFunctionsAndPrediction("UBLSH", dataFileBase, "val", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "58":
                LOG.info("Running User-based LSH Prediction and k test...");
                LSHPredictionTests.runLSHAndKTest("UBLSH", dataFileBase, l, k, smoothRun, seperator, y);
                break;
            case "59":
                LOG.info("Running 2D HashFunctions-HashTables test...");
                LSHPredictionTests.runLSH2DHashFunctionsTablesTest("UBLSH", numOfRun, smoothRun, dataFileBase, seperator, kNN, y);
                break;

            case "60":
                LSHPredictionTests.runLSHHashTablesAndKTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "61":
                LSHPredictionTests.runLSHHashTablesAndYTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "62":
                LSHPredictionTests.runLSHYAndKTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, l, k);
                break;
            case "63":
                LSHPredictionTests.runLSHHashTablesAndPrediction("LSH", dataFileBase, "val", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "64":
                LSHPredictionTests.runLSHHashFunctionsAndPrediction("LSH", dataFileBase, "val", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "65":
                LOG.info("Running 2D HashFunctions-HashTables test...");
                LSHPredictionTests.runLSH2DHashFunctionsTablesTest("LSH", numOfRun, smoothRun, dataFileBase, seperator, kNN, y);
                break;

            case "70":
                PrecisionTests.runUBPrecisionTests(dataFileBase, seperator, l, k, smoothRun, topN, y);
                break;
            case "71":
                PrecisionTests.runUBLSHPrecisionTest(dataFileBase, seperator, l, k, smoothRun, topN, y);
                break;
            case "72":
                PrecisionTests.run2DPrecisionHashFunctionsAndKTests("UBLSH",dataFileBase,numOfRun,smoothRun,seperator,topN, y);
                break;
            case "73":
                PrecisionTests.run2DPrecisionHashTablesAndKTests("UBLSH", dataFileBase, numOfRun,smoothRun,seperator,topN, y);
                break;
            case "74":
                PrecisionTests.runUBLSHPrecisionAndYTest(dataFileBase, "UBLSH", seperator, l, k, smoothRun, topN, kNN);
                break;
            case "75":
                PrecisionTests.runUBLSHPrecisionKAndYTest(dataFileBase, seperator, l, k, smoothRun, topN, numOfRun );
                break;
            case "76":
                PrecisionTests.runUBPrecisionKAndYTest(dataFileBase, seperator, l, k, smoothRun, topN, numOfRun );
                break;


            case "99":
                LOG.info("bye...\n");
                break;

            default:
                LOG.info("Command not recognized...\n");
                break;
        }

    }

    private static void runnCompTests(Scanner scanner) {
        runSelection("54",scanner);
        runSelection("55",scanner);

    }

    private static void runConfiguredTests(Scanner scanner) {
        runSelection("56",scanner);
        runSelection("57",scanner);
        runSelection("58",scanner);
        runSelection("59",scanner);

        runSelection("60",scanner);
        runSelection("61",scanner);
        runSelection("62",scanner);

        runSelection("70",scanner);
        runSelection("71",scanner);
        runSelection("72",scanner);
        runSelection("73",scanner);
        runSelection("74",scanner);
        runSelection("75",scanner);
        runSelection("76",scanner);
    }

}
