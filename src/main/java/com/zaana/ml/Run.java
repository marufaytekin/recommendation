package com.zaana.ml;

import com.zaana.ml.tests.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by maytekin on 26.05.2015.
 */
public class Run {
    static Logger LOG = Logger.getLogger(Run.class);
    static void runSelection(final String selection, String dataFilePath, String dataFileBase, Scanner scanner, String seperator, HashMap<String, HashMap<String, Integer>> userRateMap, int numOfRun, int smoothRun, int kNN, int k, int l, int y, int topN)
    {
        switch (selection) {

            case "02":
                dataFilePath = "data/ymusic/ymusic.data";
                dataFileBase = "data/ymusic/ymusic";
                y = 5;
                runValTests(dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                break;

            case "5000":
                runLSHValTests(dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                break;

            case "1":
                System.out.println("Enter k-NN: ");
                MainRun.kNN = Integer.parseInt(scanner.nextLine());
                break;
            case "2":
                System.out.println("Enter number of bands ( l ) :");
                MainRun.l = Integer.parseInt(scanner.nextLine());
                System.out.println("Enter of hash functions ( k ) :");
                MainRun.k = Integer.parseInt(scanner.nextLine());
                break;
            case "3":
                System.out.println("Enter data file path: ");
                MainRun.dataFilePath = scanner.nextLine();
                break;

            case "10": //old 8
                ModelBuildTimeTest.runModelBuildTimeTest("UB", dataFilePath, seperator, k, l);
                ModelBuildTimeTest.runModelBuildTimeTest("UBLSH", dataFilePath, seperator, k, l);
                ModelBuildTimeTest.runModelBuildTimeTest("IB", dataFilePath, seperator, k, l);
                //ModelBuildTimeTest.runModelBuildTimeTest("IBLSH", dataFilePath, seperator, k, l);
                break;
            case "11":
                ClusterTests.runClusterTests1(dataFileBase, seperator, kNN, y);
                //ClusterTests.runClusterPredictionTests(dataFileBase, seperator, numOfRun, smoothRun, kNN, y);
                break;

            case "50":
                CFPredictionTests.runCFPredictionAndKTest(dataFilePath, dataFileBase, "UB", smoothRun, seperator, y);
                break;
            case "500":
                CFPredictionTests.runCFPredictionAndYTest(dataFilePath, dataFileBase, "UB", smoothRun, seperator, kNN);
                break;
            case "51":
                CFPredictionTests.runCFPredictionKAndY2DTest("UB", dataFileBase, numOfRun, smoothRun, seperator);
                break;
            case "52":
                CFPredictionTests.runCFPredictionAndKTest(dataFilePath, dataFileBase, "IB", smoothRun, seperator, y);
                break;
            case "520":
                CFPredictionTests.runCFPredictionAndYTest(dataFilePath, dataFileBase, "IB", smoothRun, seperator, kNN);
                break;
            case "53":
                CFPredictionTests.runCFPredictionKAndY2DTest("IB", dataFileBase, numOfRun, smoothRun, seperator);
                break;

            case "54":
                LSHPredictionTests.runLSHHashFunctionsAndKTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "55":
                LSHPredictionTests.runLSHHashFunctionsAndYTest("UBLSH", dataFileBase,"val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "56":
                LSHPredictionTests.runLSHHashTablesAndPrediction("UBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "560":
                LSHPredictionTests.runLSHHashTablesAndPrediction("IBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "57":
                LSHPredictionTests.runLSHHashFunctionsAndPrediction("UBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "570":
                LSHPredictionTests.runLSHHashFunctionsAndPrediction("IBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "58":
                LSHPredictionTests.runLSHAndKTest("UBLSH", dataFileBase, l, k, smoothRun, seperator, y);
                break;
            case "580":
                LSHPredictionTests.runLSHAndKTest("IBLSH", dataFileBase, l, k, smoothRun, seperator, y);
                break;
            case "59":
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
                LSHPredictionTests.runLSHHashTablesAndPrediction("LSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "64":
                LSHPredictionTests.runLSHHashFunctionsAndPrediction("LSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "65":
                LSHPredictionTests.runLSH2DHashFunctionsTablesTest("LSH", numOfRun, smoothRun, dataFileBase, seperator, kNN, y);
                break;

            case "70":
                PrecisionTests.runUBPrecisionAndKTests(dataFileBase, seperator, l, k, smoothRun, topN, y);
                break;
            case "71":
                PrecisionTests.runUBLSHPrecisionAndKTest(dataFileBase, seperator, l, k, smoothRun, topN, y);
                break;
            case "72":
                PrecisionTests.run2DPrecisionHashFunctionsAndKTests("UBLSH", dataFileBase, numOfRun, smoothRun, seperator, topN, y);
                break;
            case "73":
                PrecisionTests.run2DPrecisionHashTablesAndKTests("UBLSH", dataFileBase, numOfRun, smoothRun, seperator, topN, y);
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

            case "80":
                LSHPredictionTests.runLSHYAndKTest("IBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, l, k);
                break;
            case "81":
                LSHPredictionTests.runLSHHashFunctionsAndKTest("IBLSH", dataFileBase,"val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "82":
                LSHPredictionTests.runLSHHashTablesAndKTest("IBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "83":
                LSHPredictionTests.runLSH2DHashFunctionsTablesTest("IBLSH", numOfRun, smoothRun, dataFileBase, seperator, kNN, y);
                break;

            case "100":
                CFPredictionTests.runCFPredictionTests(dataFilePath, dataFileBase, "UB", smoothRun, seperator, kNN, y);
                break;

            case "101":
                CFPredictionTests.runCFPredictionTests(dataFilePath, dataFileBase, "IB", smoothRun, seperator, kNN, y);
                break;
            case "102":
                PrecisionTests.runUBPrecisionTests(dataFileBase, seperator, l, k, smoothRun, topN, kNN, y);
                break;
            case "103":
                PrecisionTests.runIBPrecisionTests(dataFileBase, seperator, l, k, smoothRun, topN, kNN, y);
                break;
            case "104":
                PrecisionTests.run2DLSHPrecisionTests("UBLSH", dataFileBase, numOfRun, smoothRun, seperator, topN, kNN, y);
                break;
            case "105":
                PrecisionTests.run2DLSHPrecisionTests("IBLSH", dataFileBase, numOfRun, smoothRun, seperator, topN, kNN, y);
                break;

            case "99":
                LOG.info("bye...\n");
                break;

            default:
                LOG.info("Command not recognized...\n");
                break;
        }

    }

    static void runCompTests(String dataFilePath, String dataFileBase, Scanner scanner, String seperator,
                             HashMap<String, HashMap<String, Integer>> userRateMap,
                             int numOfRun, int smoothRun, int kNN, int k, int l, int y, int topN) {
        runSelection("59", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("83", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("81", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("82", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("51", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("53", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("62", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("80", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("54", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("60", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

    }


    static void runValTests(String dataFilePath, String dataFileBase, Scanner scanner, String seperator,
                             HashMap<String, HashMap<String, Integer>> userRateMap,
                             int numOfRun, int smoothRun, int kNN, int k, int l, int y, int topN) {
        runSelection("50", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("52", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("500", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("520", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
    }

    static void runLSHValTests(String dataFilePath, String dataFileBase, Scanner scanner, String seperator,
                            HashMap<String, HashMap<String, Integer>> userRateMap,
                            int numOfRun, int smoothRun, int kNN, int k, int l, int y, int topN) {
        runSelection("100", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("101", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("56", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("560", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("57", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("570", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
    }

   
}
