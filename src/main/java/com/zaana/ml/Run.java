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

            case "01":
                runSelection("50", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("52", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("58", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("580", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

            case "02":
                DataParser.processDataFile(dataFilePath, seperator,0,100);
                userRateMap = DataParser.getUserRateMap();
                System.out.println("User based histogram");
                DataParser.calculateDataSetHistogram(userRateMap);
                System.out.println("Item based histogram");
                DataParser.calculateDataSetHistogram(DataParser.getItemRateMap());
                break;

            case "5000":
                runLSHPredictionTests(dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
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
                ModelBuildTimeTest.runModelBuildTimeTest("IBLSH", dataFilePath, seperator, k, l);
                break;
            case "11":
                ClusterTest.runClusterTests1(dataFileBase, seperator, kNN, y);

                break;
            case "12":
                ClusterTest.runClusterPredictionTests(dataFileBase, seperator, numOfRun, smoothRun, kNN, y);
                break;

            case "50":
                CFPredictionValidationTest.runCFPredictionAndKTest(dataFilePath, dataFileBase, "UB", smoothRun, seperator, y);
                break;
            case "500":
                CFPredictionValidationTest.runCFPredictionAndYTest(dataFilePath, dataFileBase, "UB", smoothRun, seperator, kNN);
                break;
            case "51":
                CFPredictionValidationTest.runCFPredictionKAndY2DTest("UB", dataFileBase, numOfRun, smoothRun, seperator);
                break;
            case "52":
                CFPredictionValidationTest.runCFPredictionAndKTest(dataFilePath, dataFileBase, "IB", smoothRun, seperator, y);
                break;
            case "520":
                CFPredictionValidationTest.runCFPredictionAndYTest(dataFilePath, dataFileBase, "IB", smoothRun, seperator, kNN);
                break;
            case "53":
                CFPredictionValidationTest.runCFPredictionKAndY2DTest("IB", dataFileBase, numOfRun, smoothRun, seperator);
                break;

            case "54":
                LSHPredictionTest.runLSHHashFunctionsAndKTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "55":
                LSHPredictionTest.runLSHHashFunctionsAndYTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "56":
                LSHPredictionTest.runLSHHashTablesAndPrediction("UBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "560":
                LSHPredictionTest.runLSHHashTablesAndPrediction("IBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "57":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction("UBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "570":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction("IBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "58":
                LSHPredictionTest.runLSHAndKTest("UBLSH", dataFileBase, l, k, smoothRun, seperator, y);
                break;
            case "580":
                LSHPredictionTest.runLSHAndKTest("IBLSH", dataFileBase, l, k, smoothRun, seperator, y);
                break;
            case "59":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest("UBLSH", numOfRun, smoothRun, dataFileBase, seperator, kNN, y);
                break;

            case "60":
                LSHPredictionTest.runLSHHashTablesAndKTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "61":
                LSHPredictionTest.runLSHHashTablesAndYTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "62":
                LSHPredictionTest.runLSHYAndKTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, l, k);
                break;
            case "63":
                LSHPredictionTest.runLSHHashTablesAndPrediction("LSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "64":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction("LSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "65":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest("LSH", numOfRun, smoothRun, dataFileBase, seperator, kNN, y);
                break;

            case "70":
                PrecisionTest.runUBPrecisionAndKTests(dataFileBase, seperator, l, k, smoothRun, topN, y);
                break;
            case "71":
                PrecisionTest.runUBLSHPrecisionAndKTest(dataFileBase, seperator, l, k, smoothRun, topN, y);
                break;
            case "72":
                PrecisionTest.run2DPrecisionHashFunctionsAndKTests("UBLSH", dataFileBase, numOfRun, smoothRun, seperator, topN, y);
                break;
            case "73":
                PrecisionTest.run2DPrecisionHashTablesAndKTests("UBLSH", dataFileBase, numOfRun, smoothRun, seperator, topN, y);
                break;
            case "74":
                PrecisionTest.runUBLSHPrecisionAndYTest(dataFileBase, "UBLSH", seperator, l, k, smoothRun, topN, kNN);
                break;
            case "75":
                PrecisionTest.runUBLSHPrecisionKAndYTest(dataFileBase, seperator, l, k, smoothRun, topN, numOfRun);
                break;
            case "76":
                PrecisionTest.runUBPrecisionKAndYTest(dataFileBase, seperator, l, k, smoothRun, topN, numOfRun);
                break;

            case "80":
                LSHPredictionTest.runLSHYAndKTest("IBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, l, k);
                break;
            case "81":
                LSHPredictionTest.runLSHHashFunctionsAndKTest("IBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "82":
                LSHPredictionTest.runLSHHashTablesAndKTest("IBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
                break;
            case "83":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest("IBLSH", numOfRun, smoothRun, dataFileBase, seperator, kNN, y);
                break;

            case "100":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "UB", smoothRun, seperator, kNN, y);
                break;

            case "101":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "IB", smoothRun, seperator, kNN, y);
                break;
            case "102":
                PrecisionTest.runUBPrecisionTests(dataFileBase, seperator, l, k, smoothRun, topN, kNN, y);
                break;
            case "103":
                PrecisionTest.runIBPrecisionTests(dataFileBase, seperator, l, k, smoothRun, topN, kNN, y);
                break;
            case "104":
                PrecisionTest.run2DLSHPrecisionTests("UBLSH", dataFileBase, numOfRun, smoothRun, seperator, topN, kNN, y);
                break;
            case "105":
                PrecisionTest.run2DLSHPrecisionTests("IBLSH", dataFileBase, numOfRun, smoothRun, seperator, topN, kNN, y);
                break;
            case "106":
                PrecisionTest.run2DLSHPrecisionTests("LSH", dataFileBase, numOfRun, smoothRun, seperator, topN, kNN, y);
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

    static void runLSHPredictionTests(String dataFilePath, String dataFileBase, Scanner scanner, String seperator,
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
