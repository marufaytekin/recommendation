package com.zaana.ml;

import com.zaana.ml.recomm.cf.IBKNNRecommender;
import com.zaana.ml.recomm.cf.UBKNNRecommender;
import com.zaana.ml.recomm.lsh.*;
import com.zaana.ml.tests.*;
import com.zaana.ml.utils.DataParser;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import org.apache.log4j.Logger;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by maytekin on 26.05.2015.
 */
public class TestDriver {
    static Logger LOG = Logger.getLogger(TestDriver.class);
    static Logger LOG2 = Logger.getLogger("RESULTS_LOGGER");
    static void runSelection(final String selection, String dataFilePath, String dataFileBase, Scanner scanner, String seperator, int numOfRun, int cvFoldK, int kNN, int k, int l, int y, int topN)
    {
        IBLSH1Recommender ibLsh1Recommender = new IBLSH1Recommender();
        IBLSH2Recommender ibLsh2Recommender = new IBLSH2Recommender();
        UBLSH1Recommender ubLsh1Recommender = new UBLSH1Recommender();
        UBLSH2Recommender ubLsh2Recommender = new UBLSH2Recommender();

        switch (selection) {

            case "00":
                //BucketDistTest.runBucketDistTest("test", dataFileBase, seperator, cvFoldK, k, 1, 50);
                BucketDistTest.runModelBuildTest(dataFileBase, seperator);
                break;

            case "02":
                DataParser.readTrainingDataFile(dataFilePath,0, seperator);
                HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap = DataParser.getUserRateMap();
                LOG2.info("User based histogram: " + dataFilePath);
                DataParser.calculateDataSetHistogram(userRateMap);
                LOG2.info("Item based histogram: " + dataFilePath);
                DataParser.calculateDataSetHistogram(DataParser.getItemRateMap());
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
                Main.dataFilePath = scanner.nextLine();
                break;

            case "10": //old 8
                ModelBuildTimeTest.runModelBuildTimeTest("UB", dataFilePath, seperator, k, l);
                ModelBuildTimeTest.runModelBuildTimeTest("UBLSH", dataFilePath, seperator, k, l);
                ModelBuildTimeTest.runModelBuildTimeTest("IB", dataFilePath, seperator, k, l);
                ModelBuildTimeTest.runModelBuildTimeTest("IBLSH", dataFilePath, seperator, k, l);
                break;

            case "50":
                LOG.info("Processing ======================== " + dataFileBase + " ========================");
                CFPredictionValidationTest.runCFPredictionKAndY2DTest(dataFileBase, "UB", 10, 5, seperator);
                CFPredictionValidationTest.runCFPredictionKAndY2DTest(dataFileBase, "IB", 10, 5, seperator);
                break;

            case "51":
                LOG.info("Processing ======================== " + dataFileBase + " ========================");
                CFPredictionValidationTest.runCFPredictionAndKTest(dataFilePath, dataFileBase, "IB", cvFoldK, seperator, y);
                CFPredictionValidationTest.runCFPredictionAndKTest(dataFilePath, dataFileBase, "UB", cvFoldK, seperator, y);
                CFPredictionValidationTest.runCFPredictionAndYTest(dataFilePath, dataFileBase, "UB", 5, seperator, 20);
                CFPredictionValidationTest.runCFPredictionAndYTest(dataFilePath, dataFileBase, "IB", 5, seperator, 20);
                break;

            ////////////////////////////////////////////////////////////////////
            // All Prediction Tests
            ////////////////////////////////////////////////////////////////////
            case "100":
                LOG.info("Processing =========================" + dataFileBase + "=========================================");
                LOG.info("Starting prediction tests...");
                runSelection("60", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("61", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("62", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("63", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);

                runSelection("70", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("71", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("72", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("73", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("74", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("75", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("76", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("77", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                break;

            ////////////////////////////////////////////////////////////////////
            // All top-N recommendation tests
            ////////////////////////////////////////////////////////////////////
            case "101":
                LOG.info("Processing ======================== " + dataFileBase + " ========================");
                LOG.info("Starting top-n recommendation tests...");
                LSHTopNRecommTests.runHashFunctionsLSHEvaluation(ibLsh1Recommender, dataFileBase, seperator, numOfRun, cvFoldK, l, topN);
                LSHTopNRecommTests.runHashTablesLSHEvaluation(ibLsh1Recommender, dataFileBase, seperator, numOfRun, cvFoldK, k, topN);
                LSHTopNRecommTests.runHashFunctionsLSHEvaluation(ibLsh2Recommender, dataFileBase, seperator, numOfRun, cvFoldK, l, topN);
                LSHTopNRecommTests.runHashTablesLSHEvaluation(ibLsh2Recommender, dataFileBase, seperator, numOfRun, cvFoldK, k, topN);
                LSHTopNRecommTests.runHashFunctionsLSHEvaluation(ubLsh1Recommender, dataFileBase, seperator, numOfRun, cvFoldK, l, topN);
                LSHTopNRecommTests.runHashTablesLSHEvaluation(ubLsh1Recommender, dataFileBase, seperator, numOfRun, cvFoldK, k, topN);
                LSHTopNRecommTests.runHashFunctionsLSHEvaluation(ubLsh2Recommender, dataFileBase, seperator, numOfRun, cvFoldK, l, topN);
                LSHTopNRecommTests.runHashTablesLSHEvaluation(ubLsh2Recommender, dataFileBase, seperator, numOfRun, cvFoldK, k, topN);
                break;

            case "102":
                LOG.info("Processing =========================" + dataFileBase + "=========================================");
                LOG.info("Starting 2D prediction tests...");
                runSelection("80", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("81", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("82", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("83", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("84", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                runSelection("85", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
                break;

            case "103":
                UBKNNRecommender ubRecommender = new UBKNNRecommender();
                CFPrecisionRecallTests.runCFRecommendation(ubRecommender, dataFileBase, seperator, cvFoldK, topN, y);
                IBKNNRecommender ibRecommender = new IBKNNRecommender();
                CFPrecisionRecallTests.runCFRecommendation(ibRecommender, dataFileBase, seperator, cvFoldK, topN, y);
                break;

            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "54":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "UBKNN", cvFoldK, seperator, kNN, y);
                break;
            case "55":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "IBKNN", cvFoldK, seperator, kNN, y);
                break;

            case "60":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ubLsh1Recommender, "UBKNNLSH", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "61":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ubLsh1Recommender, "UBKNNLSH", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "62":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ibLsh1Recommender, "IBKNNLSH", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "63":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ibLsh1Recommender, "IBKNNLSH", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;

            case "70":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ibLsh1Recommender, "IBLSH1", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "71":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ibLsh1Recommender, "IBLSH1", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "72":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ibLsh2Recommender, "IBLSH2", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "73":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ibLsh2Recommender, "IBLSH2", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "74":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ubLsh1Recommender, "UBLSH1", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "75":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ubLsh1Recommender, "UBLSH1", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "76":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ubLsh2Recommender, "UBLSH2", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;
            case "77":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ubLsh2Recommender, "UBLSH2", dataFileBase, seperator,
                        numOfRun, l, k, cvFoldK, kNN, y);
                break;

            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "80":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(
                        ubLsh1Recommender, "UBKNNLSH", dataFileBase, seperator, numOfRun, cvFoldK, kNN, y);
                break;
            case "81":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(
                        ibLsh1Recommender, "IBKNNLSH", dataFileBase, seperator, numOfRun, cvFoldK, kNN, y);
                break;
            case "82":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(
                        ibLsh1Recommender, "IBLSH1", dataFileBase, seperator, numOfRun, cvFoldK, kNN, y);
                break;
            case "83":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(
                        ubLsh1Recommender, "UBLSH1", dataFileBase, seperator, numOfRun, cvFoldK, kNN, y);
                break;
            case "84":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(
                        ibLsh2Recommender, "IBLSH2", dataFileBase, seperator, numOfRun, cvFoldK, kNN, y);
                break;
            case "85":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(
                        ubLsh2Recommender, "UBLSH2", dataFileBase, seperator, numOfRun, cvFoldK, kNN, y);
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
                             int numOfRun, int cvFoldK, int kNN, int k, int l, int y, int topN) {
        runSelection("59", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("83", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("81", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("82", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("51", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("53", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("62", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("80", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("54", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("60", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);

    }


    static void runValTests(String dataFilePath, String dataFileBase, Scanner scanner, String seperator,
                            int numOfRun, int cvFoldK, int kNN, int k, int l, int y, int topN) {
        runSelection("50", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("52", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("500", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("520", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
    }

    static void runLSHPredictionTests(String dataFilePath, String dataFileBase, Scanner scanner, String seperator,
                                      int numOfRun, int cvFoldK, int kNN, int k, int l, int y, int topN) {
        runSelection("100", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("101", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("56", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("560", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("57", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
        runSelection("570", dataFilePath, dataFileBase, scanner, seperator, numOfRun, cvFoldK, kNN, k, l, y, topN);
    }


   
}
