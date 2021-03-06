package com.zaana.ml.tests;

import com.zaana.ml.metrics.MAE;
import com.zaana.ml.prediction.*;
import com.zaana.ml.recomm.lsh.AbstractLSHRecommender;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.ArrayList;

/**
 * Created by maruf on 06/05/15.
 */
public class LSHPredictionTest extends AbstractTest {

    private static double runTime;
    private static Double candidate_set_size;
    private static double mae;
    private static double predictionCoverage;

    /**
     * Runs LSH prediction tests for HashTables to detect the effect of changes
     * in HashTable to the prediction accuracy.*/
    public static void runLSHHashTablesAndPrediction(
            AbstractLSHRecommender lshRecommender, String type, String dataFileBase, String separator,
            int numOfRun, int numberOfHashTables, int numOfHashFunctions,
            double cvFoldNum, int kNN, int y) {

        runLSHPredictionPerformanceTests(
                lshRecommender, type, "HashTables", dataFileBase, numOfRun, numberOfHashTables, numOfHashFunctions,
                separator, cvFoldNum, kNN, y);
    }


    /**
     * Runs LSH prediction tests for HashFunctions to detect the effect of changes
     * in HashFunctions to the prediction accuracy.*/
    public static void runLSHHashFunctionsAndPrediction(
            AbstractLSHRecommender lshRecommender, String type, String dataFileBase, String separator,
            int numOfRun, int numberOfHashTables, int numOfHashFunctions,
            double cvFoldNum, int kNN, int y) {

        runLSHPredictionPerformanceTests(
                lshRecommender, type, "HashFunctions", dataFileBase, numOfRun, numberOfHashTables, numOfHashFunctions,
                separator, cvFoldNum, kNN, y);
    }


    /**
     * Runs 2D LSH HashTables and HashFunctions tests to determine the effect
     * of these parameters on prediction accuracy. Output will be 2D graph.*/
    public static void runLSH2DHashFunctionsTablesTest(
            AbstractLSHRecommender lshRecommender, String type, String dataFileBase,
            String separator, int numOfRun, double cvFoldNum, int kNN, int y) {

        int numOfBands = 1;
        int numOfHashFunctions = 1;
        double avgMae;
        double avgCoverage;
        double avgRuntime;
        ArrayList<Object> runTimeList2D = new ArrayList<>();
        ArrayList<Object> maeList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        ArrayList<Object> prediction_coverage_list2D = new ArrayList<>();

        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> hashFuncRuntimeList = new ArrayList<>();
            ArrayList<Double> hashFuncMaeList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            ArrayList<Double> prediction_coverage_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                runTime = 0;
                mae = 0;
                candidate_set_size = 0.0;
                predictionCoverage = 0;
                runPrediction(lshRecommender, type, dataFileBase, separator, cvFoldNum, numOfBands, numOfHashFunctions, kNN, y);
                avgCoverage = predictionCoverage/cvFoldNum;
                avgMae = mae/cvFoldNum/avgCoverage;
                avgRuntime = runTime/cvFoldNum;
                LOG.debug("numOfBands:" + numOfBands + " numOfHashFunctions:" + numOfHashFunctions);
                LOG.debug(type + "MAE: " + avgMae);
                LOG.debug(type + "Runtime: " + avgRuntime);
                hashFuncMaeList.add(avgMae);
                hashFuncRuntimeList.add(avgRuntime);
                candidate_set_list.add(candidate_set_size / cvFoldNum);
                prediction_coverage_list.add(avgCoverage);
                numOfHashFunctions += 1;
            }
            runTimeList2D.add(hashFuncRuntimeList);
            maeList2D.add(hashFuncMaeList);
            candidate_set_list2D.add(candidate_set_list);
            prediction_coverage_list2D.add(prediction_coverage_list);

            numOfBands += 1;
            numOfHashFunctions = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " 2D ");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info(type + "Mae2D = " + maeList2D.toString() + ";");
        LOG2.info(type + "Runtime2D = " + runTimeList2D.toString() + ";");
        LOG2.info(type + "CandidateSetList2D = " + candidate_set_list2D.toString() + ";");
        LOG2.info(type + "PredictionCoverageList2D = " + prediction_coverage_list2D.toString() + ";");
    }


    /**
     * Runs 2D UB LSH HashTables and HashFunctions tests to determine the effect
     * of these parameters on prediction accuracy. Output will be 2D graph.*/
    private static void runLSHPredictionPerformanceTests(
            AbstractLSHRecommender lshRecommender, String type, String testType, String dataFileBase,
            int numOfRun, int l, int k, String separator, double cvFoldNum, int kNN, int y)
    {
        int numOfBands;
        int numOfHashFunctions;
        double avgMae;
        double avgCoverage;
        double avgRuntime;
        if (testType == "HashFunctions") {
            numOfBands = l;
            numOfHashFunctions = 1;
        } else if (testType == "HashTables"){
            numOfBands = 1;
            numOfHashFunctions = k;
        } else {
            throw new UnsupportedOperationException("Invalid type.");
        }
        ArrayList<Double> runtimeList = new ArrayList<>();
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> predictionCoverageList = new ArrayList<>();
        ArrayList<Double> candidate_set_list = new ArrayList<>();
        for (int i = 0; i < numOfRun; i++) {
            runTime = 0;
            mae = 0;
            candidate_set_size = 0.0;
            predictionCoverage = 0;
            runPrediction(lshRecommender, type, dataFileBase, separator, cvFoldNum, numOfBands, numOfHashFunctions, kNN, y);
            avgCoverage = predictionCoverage/cvFoldNum;
            avgMae = mae/cvFoldNum/avgCoverage;
            avgRuntime = runTime/cvFoldNum;
            maeList.add(avgMae);
            runtimeList.add(avgRuntime);
            predictionCoverageList.add(avgCoverage);
            candidate_set_list.add(candidate_set_size/cvFoldNum);
            LOG.debug("numOfBands:" + numOfBands + " numOfHashFunctions:" + numOfHashFunctions);
            LOG.debug("Mae: " + avgMae);
            LOG.debug("Runtime: " + avgRuntime);
            LOG.debug("Predicted Items :" + avgCoverage);
            if (testType == "HashFunctions") {
                numOfHashFunctions++;
            } else {
                numOfBands++;
            }
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + testType + " Prediction Performance");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info("numOfHashTables = " + l);
        LOG2.info("numOfHashFunctions = " + k);
        LOG2.info(type + testType+ "MaeList =  " + maeList.toString() + ";");
        LOG2.info(type + testType + "RuntimeList = " + runtimeList.toString() + ";");
        LOG2.info(type + testType + "CandidateSetList = " + candidate_set_list + ";");
        LOG2.info(type + testType + "PredictionCoverageList  = " + predictionCoverageList.toString() + ";");
    }



    private static void runPrediction(
            AbstractLSHRecommender lshRecommender,
            String type, String dataFileBase, String separator,
            double cvFoldNum, int numOfBands, int numOfHashFunctions,
            int kNN, int y) {
        HashObjObjMap<Object, Object> hashTables;
        for (int i = 0; i < cvFoldNum; i++) {
            preprocessDataForValidation(dataFileBase, i, separator);
            lshRecommender.buildModel(userRateMap, itemRateMap, numOfBands, numOfHashFunctions);
            //int testDataSize = testDataMap.size(  );
            if (type == "UBKNNLSH") {
                hashTables = lshRecommender.getHashTables();
                hashKeyLookupTable = lshRecommender.getHashKeyLookupTable();
                runTime += UBKNNLSHPrediction.runUserBasedLSHPredictionOnTestData(
                        userRateMap, itemRateMap, testDataMap, hashTables, kNN, y, hashKeyLookupTable);
                candidate_set_size += UBKNNLSHPrediction
                        .getAvg_candidate_set_size();
                mae += MAE.calculateMAE(
                        UBKNNLSHPrediction.getOutputList(),
                        UBKNNLSHPrediction.getTargetList());
                predictionCoverage +=
                        (double)UBKNNLSHPrediction.getOutputList().size()/UBKNNLSHPrediction.getTestQueryCnt();

            } else if (type == "IBKNNLSH") {
                hashTables = lshRecommender.getHashTables();
                hashKeyLookupTable = lshRecommender.getHashKeyLookupTable();
                runTime +=
                        IBKNNLSHPrediction.runItemBasedLSHPredictionOnTestData
                                (itemRateMap, userRateMap, testDataMap, hashTables, hashKeyLookupTable, kNN, y);
                candidate_set_size += IBKNNLSHPrediction
                        .getAvg_candidate_set_size();
                mae += MAE.calculateMAE(
                        IBKNNLSHPrediction.getOutputList(),
                        IBKNNLSHPrediction.getTargetList());
                predictionCoverage +=
                        (double)IBKNNLSHPrediction.getOutputList().size()/IBKNNLSHPrediction.getTestQueryCnt();
            } else if (type == "UBLSH1" || type == "UBLSH2" || type == "UBLSH3") {
                runTime += LSHPredictionTests.runUBLSHPredictionOnTestData
                        (userRateMap, itemRateMap, testDataMap, lshRecommender);
                candidate_set_size += LSHPredictionTests.getAvg_candidate_set_size();
                mae += MAE.calculateMAE(
                        LSHPredictionTests.getOutputList(),
                        LSHPredictionTests.getTargetList());
                predictionCoverage +=
                        (double) LSHPredictionTests.getOutputList().size()/LSHPredictionTests.getTestQueryCnt();
            } else if (type == "IBLSH1" || type == "IBLSH2" ){
                runTime += LSHPredictionTests.runIBLSHPredictionOnTestData
                        (userRateMap, itemRateMap, testDataMap, lshRecommender);
                candidate_set_size += LSHPredictionTests.getAvg_candidate_set_size();
                mae += MAE.calculateMAE(
                        LSHPredictionTests.getOutputList(),
                        LSHPredictionTests.getTargetList());
                predictionCoverage +=
                        (double) LSHPredictionTests.getOutputList().size()/LSHPredictionTests.getTestQueryCnt();
            } else throw new UnsupportedOperationException("Invalid type.");
        }
    }



}
