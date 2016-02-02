package com.zaana.ml;

import com.zaana.ml.recomm.cf.IBCFRecommender;
import com.zaana.ml.recomm.cf.UBCFRecommender;
import com.zaana.ml.recomm.lsh.*;
import com.zaana.ml.tests.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by maytekin on 26.05.2015.
 */
public class TestDriver {
    static Logger LOG = Logger.getLogger(TestDriver.class);
    static void runSelection(final String selection, String dataFilePath, String dataFileBase, Scanner scanner, String seperator, HashMap<String, HashMap<String, Integer>> userRateMap, int numOfRun, int smoothRun, int kNN, int k, int l, int y, int topN)
    {
        IBLSHRecommender ibLshRecommender = new IBLSHRecommender();
        IBLSHRecommenderNew ibLshRecommenderNew = new IBLSHRecommenderNew();
        UBLSHRecommender ubLshRecommender = new UBLSHRecommender();
        UBLSHRecommenderNew ubLshRecommenderNew = new UBLSHRecommenderNew();
        switch (selection) {

            case "00":
                BucketDistTest.runBucketDistTest("test", dataFileBase, seperator, smoothRun, k, 1, 50);
                break;

            case "01": // prediction tests

                runSelection("60", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("61", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("62", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("63", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

                runSelection("70", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("71", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("72", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("73", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("74", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("75", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("76", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("77", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

                runSelection("54", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("55", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

                //precision tests
                runSelection("100", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("101", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("102", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("103", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("104", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("105", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("106", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("107", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("110", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("111", dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);
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
                Main.kNN = Integer.parseInt(scanner.nextLine());
                break;
            case "2":
                System.out.println("Enter number of bands ( l ) :");
                Main.l = Integer.parseInt(scanner.nextLine());
                System.out.println("Enter of hash functions ( k ) :");
                Main.k = Integer.parseInt(scanner.nextLine());
                break;
            case "3":
                System.out.println("Enter data file path: ");
                Main.dataFilePath = scanner.nextLine();
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
            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "54":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "UB", smoothRun, seperator, kNN, y);
                break;
            case "55":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "IB", smoothRun, seperator, kNN, y);
                break;
            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "60":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ubLshRecommender, "UBLSH-KNN", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "61":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ubLshRecommender, "UBLSH-KNN", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "62":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ibLshRecommender, "IBLSH-KNN", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "63":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ibLshRecommender, "IBLSH-KNN", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "70":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ibLshRecommender, "LSH1-IB", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "71":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ibLshRecommender, "LSH1-IB", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "72":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ibLshRecommenderNew, "LSH2-IB", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "73":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ibLshRecommenderNew, "LSH2-IB", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "74":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ubLshRecommender, "LSH1-UB", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "75":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ubLshRecommender, "LSH1-UB", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "76":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ubLshRecommenderNew, "LSH2-UB", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "77":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ubLshRecommenderNew, "LSH2-UB", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "100":
                LSHPrecisionRecallTests.runHashFunctionsLSHEvaluation(ibLshRecommender, dataFileBase, seperator, numOfRun, smoothRun, l, topN);
                break;
            case "101":
                LSHPrecisionRecallTests.runHashTablesLSHEvaluation(ibLshRecommender, dataFileBase, seperator, numOfRun, smoothRun, k, topN);
                break;
            case "102":
                LSHPrecisionRecallTests.runHashFunctionsLSHEvaluation(ibLshRecommenderNew, dataFileBase, seperator, numOfRun, smoothRun, l, topN);
                break;
            case "103":
                LSHPrecisionRecallTests.runHashTablesLSHEvaluation(ibLshRecommenderNew, dataFileBase, seperator, numOfRun, smoothRun, k, topN);
                break;
            case "104":
                LSHPrecisionRecallTests.runHashFunctionsLSHEvaluation(ubLshRecommender, dataFileBase, seperator, numOfRun, smoothRun, l, topN);
                break;
            case "105":
                LSHPrecisionRecallTests.runHashTablesLSHEvaluation(ubLshRecommender, dataFileBase, seperator, numOfRun, smoothRun, k, topN);
                break;
            case "106":
                LSHPrecisionRecallTests.runHashFunctionsLSHEvaluation(ubLshRecommenderNew, dataFileBase, seperator, numOfRun, smoothRun, l, topN);
                break;
            case "107":
                LSHPrecisionRecallTests.runHashTablesLSHEvaluation(ubLshRecommenderNew, dataFileBase, seperator, numOfRun, smoothRun, k, topN);
                break;
            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "110":
                IBCFRecommender ibRecommender = new IBCFRecommender();
                CFPrecisionRecallTests.runCFRecommendation(ibRecommender, dataFileBase, seperator, smoothRun, topN, y);
                break;
            case "111":
                UBCFRecommender ubRecommender = new UBCFRecommender();
                CFPrecisionRecallTests.runCFRecommendation(ubRecommender, dataFileBase, seperator, smoothRun, topN, y);
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
