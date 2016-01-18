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

            case "00":
                BucketDistTest.runBucketDistTest("test", dataFileBase, seperator, smoothRun, k, 1, 50);
                break;

            case "01": // prediction tests

                runSelection("54", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("55", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("56", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("57", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("58", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("59", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("63", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("64", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("65", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("66", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

                //precision tests
                runSelection("90", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("91", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("92", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("93", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

                runSelection("102", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("103", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("104", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("106", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("107", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("108", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                break;

            case "02":
                DataParser.processDataFile(dataFilePath, seperator,5,95);
                userRateMap = DataParser.getUserRateMap();
                System.out.println("User based histogram");
                DataParser.calculateDataSetHistogram(userRateMap);
                System.out.println("Item based histogram");
                DataParser.removeDuplicateData(userRateMap, DataParser.getItemRateMap(), DataParser.getTestDataMap());
                DataParser.calculateDataSetHistogram(DataParser.getItemRateMap());
                break;

            case "03":
                runSelection("100", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("101", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                break;

            case "04":
                AbstractTest.preprocessDataForValidation(dataFileBase, smoothRun, "test", seperator);
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

//            case "50":
//                CFPredictionValidationTest.runCFPredictionAndKTest(dataFilePath, dataFileBase, "UB", smoothRun, seperator, y);
//                break;
//            case "500":
//                CFPredictionValidationTest.runCFPredictionAndYTest(dataFilePath, dataFileBase, "UB", smoothRun, seperator, kNN);
//                break;
//            case "51":
//                CFPredictionValidationTest.runCFPredictionKAndY2DTest("UB", dataFileBase, numOfRun, smoothRun, seperator);
//                break;
//            case "52":
//                CFPredictionValidationTest.runCFPredictionAndKTest(dataFilePath, dataFileBase, "IB", smoothRun, seperator, y);
//                break;
//            case "520":
//                CFPredictionValidationTest.runCFPredictionAndYTest(dataFilePath, dataFileBase, "IB", smoothRun, seperator, kNN);
//                break;
//            case "53":
//                CFPredictionValidationTest.runCFPredictionKAndY2DTest("IB", dataFileBase, numOfRun, smoothRun, seperator);
//                break;

//            case "54":
//                LSHParameterTest.runLSHHashFunctionsAndKTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
//                break;
//            case "55":
//                LSHParameterTest.runLSHHashFunctionsAndYTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
//                break;

            case "54":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "UB", smoothRun, seperator, kNN, y);
                break;
            case "55":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "IB", smoothRun, seperator, kNN, y);
                break;
            case "56":
                LSHPredictionTest.runLSHHashTablesAndPrediction("UBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "57":
                LSHPredictionTest.runLSHHashTablesAndPrediction("IBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "58":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction("UBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "59":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction("IBLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
//            case "58":
//                LSHParameterTest.runLSHAndKTest("UBLSH", dataFileBase, l, k, smoothRun, seperator, y);
//                break;
//            case "580":
//                LSHParameterTest.runLSHAndKTest("IBLSH", dataFileBase, l, k, smoothRun, seperator, y);
//                break;

//            case "60":
//                LSHParameterTest.runLSHHashTablesAndKTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
//                break;
//            case "61":
//                LSHParameterTest.runLSHHashTablesAndYTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
//                break;
//            case "62":
//                LSHParameterTest.runLSHYAndKTest("UBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, l, k);
//                break;

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
            case "66":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest("UBLSH", numOfRun, smoothRun, dataFileBase, seperator, kNN, y);
                break;
            case "67":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest("IBLSH", numOfRun, smoothRun, dataFileBase, seperator, kNN, y);
                break;
//            case "80":
//                LSHParameterTest.runLSHYAndKTest("IBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, l, k);
//                break;
//            case "81":
//                LSHParameterTest.runLSHHashFunctionsAndKTest("IBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
//                break;
//            case "82":
//                LSHParameterTest.runLSHHashTablesAndKTest("IBLSH", dataFileBase, "val", numOfRun, smoothRun, seperator, kNN, y);
//                break;

            case "90":
                PrecisionTest.runLSHPrecisionHashFunctionsTests("UBLSH", dataFileBase, l, k, numOfRun, smoothRun, seperator, topN, kNN, y);
                break;
            case "91":
                PrecisionTest.runLSHPrecisionHashFunctionsTests("IBLSH", dataFileBase, l, k, numOfRun, smoothRun, seperator, topN, kNN, y);
                break;
            case "92":
                PrecisionTest.runLSHPrecisionHashTablesTests("UBLSH", dataFileBase, l, k, numOfRun, smoothRun, seperator, topN, kNN, y);
                break;
            case "93":
                PrecisionTest.runLSHPrecisionHashTablesTests("IBLSH", dataFileBase, l, k, numOfRun, smoothRun, seperator, topN, kNN, y);
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
            case "107":
                LSHMetricsTest.runHashFunctionsLSHEvaluation(dataFileBase, numOfRun, smoothRun, seperator, l, topN, y);
                break;
            case "108":
                LSHMetricsTest.runHashTablesLSHEvaluation(dataFileBase, numOfRun, smoothRun, seperator, k, topN, y);
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
