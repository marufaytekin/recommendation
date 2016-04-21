package com.zaana.ml;

import com.zaana.ml.recomm.cf.IBKNNRecommender;
import com.zaana.ml.recomm.cf.UBKNNRecommender;
import com.zaana.ml.recomm.lsh.*;
import com.zaana.ml.tests.*;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import org.apache.log4j.Logger;

import java.util.Scanner;

/**
 * Created by maytekin on 26.05.2015.
 */
public class TestDriver {
    static Logger LOG = Logger.getLogger(TestDriver.class);
    static Logger LOG2 = Logger.getLogger("RESULTS_LOGGER");
    static void runSelection(final String selection, String dataFilePath, String dataFileBase, Scanner scanner, String seperator, int numOfRun, int smoothRun, int kNN, int k, int l, int y, int topN)
    {
        IBLSH1Recommender ibLsh1Recommender = new IBLSH1Recommender();
        IBLSH2Recommender ibLsh2Recommender = new IBLSH2Recommender();
        UBLSH1Recommender ubLsh1Recommender = new UBLSH1Recommender();
        UBLSH2Recommender ubLsh2Recommender = new UBLSH2Recommender();
        switch (selection) {

            case "00":
                //BucketDistTest.runBucketDistTest("test", dataFileBase, seperator, smoothRun, k, 1, 50);
                BucketDistTest.runModelBuildTest(dataFileBase,seperator);

                break;

            case "001":
                dataFilePath = "data/android/andapps.data";
                dataFileBase = "data/android/andapps";
                runSelection("110", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("111", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                dataFilePath = "data/movies_tv/movies_tv.data";
                dataFileBase = "data/movies_tv/movies_tv";
                runSelection("03", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                dataFilePath = "data/1m/ml-1m.data";
                dataFileBase = "data/1m/ml-1m";
                runSelection("03", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                break;

            case "01": // prediction tests
                runSelection("60", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("61", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("62", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("63", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);

                runSelection("70", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("71", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("72", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("73", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("74", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("75", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("76", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("77", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);

                runSelection("54", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("55", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);

                //precision tests
                runSelection("100", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("101", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("102", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("103", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("104", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("105", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("106", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("107", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("110", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("111", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                break;

            case "02":
                DataParser.readTrainingDataFile(dataFilePath, seperator);
                HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap = DataParser.getUserRateMap();
                LOG2.info("User based histogram: " + dataFilePath);
                DataParser.calculateDataSetHistogram(userRateMap);
                LOG2.info("Item based histogram: " + dataFilePath);
                DataParser.calculateDataSetHistogram(DataParser.getItemRateMap());
                break;

            case "03":
                //evaluation tests
                runSelection("100", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("101", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("102", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("103", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("104", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("105", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("106", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("107", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("110", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("111", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                break;

            case "04":
                runSelection("70", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("71", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("72", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("73", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("74", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("75", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("76", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("77", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("60", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("61", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("62", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("63", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                break;

            case "05":

                runSelection("80", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("81", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("82", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("83", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("84", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                runSelection("85", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
                break;

            case "5000":
                runLSHPredictionTests(dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
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
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "UBKNN", smoothRun, seperator, kNN, y);
                break;
            case "55":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "IBKNN", smoothRun, seperator, kNN, y);
                break;
            case "56":
                CFPredictionTest.runCFPredictionTests(dataFilePath, dataFileBase, "R", smoothRun, seperator, kNN, y);
                break;

            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "60":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ubLsh1Recommender, "UBKNNLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "61":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ubLsh1Recommender, "UBKNNLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "62":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ibLsh1Recommender, "IBKNNLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "63":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ibLsh1Recommender, "IBKNNLSH", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;

            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "70":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ibLsh1Recommender, "IBLSH1", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "71":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ibLsh1Recommender, "IBLSH1", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "72":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ibLsh2Recommender, "IBLSH2", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "73":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ibLsh2Recommender, "IBLSH2", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "74":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ubLsh1Recommender, "UBLSH1", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "75":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ubLsh1Recommender, "UBLSH1", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "76":
                LSHPredictionTest.runLSHHashTablesAndPrediction(ubLsh2Recommender, "UBLSH2", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;
            case "77":
                LSHPredictionTest.runLSHHashFunctionsAndPrediction(ubLsh2Recommender, "UBLSH2", dataFileBase, "test", seperator,
                        numOfRun, l, k, smoothRun, kNN, y);
                break;

            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "80":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(ubLsh1Recommender, "UBKNNLSH", dataFileBase, "val", seperator,
                        numOfRun, smoothRun, kNN, y);
                break;
            case "81":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(ibLsh1Recommender, "IBKNNLSH", dataFileBase, "val", seperator,
                        numOfRun, smoothRun, kNN, y);
                break;
            case "82":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(ibLsh1Recommender, "IBLSH1", dataFileBase, "val", seperator,
                        numOfRun, smoothRun, kNN, y);
                break;
            case "83":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(ubLsh1Recommender, "UBLSH1", dataFileBase, "val", seperator,
                        numOfRun, smoothRun, kNN, y);
                break;
            case "84":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(ibLsh2Recommender, "IBLSH2", dataFileBase, "val", seperator,
                        numOfRun, smoothRun, kNN, y);
                break;
            case "85":
                LSHPredictionTest.runLSH2DHashFunctionsTablesTest(ubLsh2Recommender, "UBLSH2", dataFileBase, "val", seperator,
                        numOfRun, smoothRun, kNN, y);
                break;

            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "100":
                LSHTopNRecommTests.runHashFunctionsLSHEvaluation(ibLsh1Recommender, dataFileBase, seperator, numOfRun, smoothRun, l, topN);
                break;
            case "101":
                LSHTopNRecommTests.runHashTablesLSHEvaluation(ibLsh1Recommender, dataFileBase, seperator, numOfRun, smoothRun, k, topN);
                break;
            case "102":
                LSHTopNRecommTests.runHashFunctionsLSHEvaluation(ibLsh2Recommender, dataFileBase, seperator, numOfRun, smoothRun, l, topN);
                break;
            case "103":
                LSHTopNRecommTests.runHashTablesLSHEvaluation(ibLsh2Recommender, dataFileBase, seperator, numOfRun, smoothRun, k, topN);
                break;
            case "104":
                LSHTopNRecommTests.runHashFunctionsLSHEvaluation(ubLsh1Recommender, dataFileBase, seperator, numOfRun, smoothRun, l, topN);
                break;
            case "105":
                LSHTopNRecommTests.runHashTablesLSHEvaluation(ubLsh1Recommender, dataFileBase, seperator, numOfRun, smoothRun, k, topN);
                break;
            case "106":
                LSHTopNRecommTests.runHashFunctionsLSHEvaluation(ubLsh2Recommender, dataFileBase, seperator, numOfRun, smoothRun, l, topN);
                break;
            case "107":
                LSHTopNRecommTests.runHashTablesLSHEvaluation(ubLsh2Recommender, dataFileBase, seperator, numOfRun, smoothRun, k, topN);
                break;

            ////////////////////////////////////////////////////////////////////
            //
            ////////////////////////////////////////////////////////////////////
            case "110":
                IBKNNRecommender ibRecommender = new IBKNNRecommender();
                CFPrecisionRecallTests.runCFRecommendation(ibRecommender, dataFileBase, seperator, smoothRun, topN, y);
                break;
            case "111":
                UBKNNRecommender ubRecommender = new UBKNNRecommender();
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
                             int numOfRun, int smoothRun, int kNN, int k, int l, int y, int topN) {
        runSelection("59", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("83", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("81", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("82", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("51", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("53", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("62", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("80", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("54", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("60", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);

    }


    static void runValTests(String dataFilePath, String dataFileBase, Scanner scanner, String seperator,
                            int numOfRun, int smoothRun, int kNN, int k, int l, int y, int topN) {
        runSelection("50", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("52", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("500", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("520", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
    }

    static void runLSHPredictionTests(String dataFilePath, String dataFileBase, Scanner scanner, String seperator,
                                      int numOfRun, int smoothRun, int kNN, int k, int l, int y, int topN) {
        runSelection("100", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("101", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("56", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("560", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("57", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
        runSelection("570", dataFilePath, dataFileBase, scanner, seperator, numOfRun, smoothRun, kNN, k, l, y, topN);
    }


   
}
