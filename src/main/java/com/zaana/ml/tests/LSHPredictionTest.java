package com.zaana.ml.tests;

import com.zaana.ml.*;
import com.zaana.ml.prediction.*;
import com.zaana.ml.recomm.lsh.AbstractLSHRecommender;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by maruf on 06/05/15.
 */
public class LSHPredictionTest extends AbstractTest
{


    /**
     * Runs LSH prediction tests for HashTables to detect the effect of changes
     * in HashTable to the prediction accuracy.*/
    public static void runLSHHashTablesAndPrediction(
            AbstractLSHRecommender lshRecommender, String type, String dataFileBase, String val, String separator,
            int numOfRun, int numberOfHashTables, int numOfHashFunctions,
            double smoothRun, int kNN, int y) {

        runLSHPredictionPerformanceTests(
                lshRecommender, type, "HashTables", dataFileBase, val, numOfRun, numberOfHashTables, numOfHashFunctions,
                separator, smoothRun, kNN, y);
    }


    /**
     * Runs LSH prediction tests for HashFunctions to detect the effect of changes
     * in HashFunctions to the prediction accuracy.*/
    public static void runLSHHashFunctionsAndPrediction(
            AbstractLSHRecommender lshRecommender, String type, String dataFileBase, String val, String separator,
            int numOfRun, int numberOfHashTables, int numOfHashFunctions,
            double smoothRun, int kNN, int y) {

        runLSHPredictionPerformanceTests(
                lshRecommender, type, "HashFunctions", dataFileBase, val, numOfRun, numberOfHashTables, numOfHashFunctions,
                separator, smoothRun, kNN, y);
    }


    /**
     * Runs 2D UB LSH HashTables and HashFunctions tests to determine the effect
     * of these parameters on prediction accuracy. Output will be 2D graph.*/
    private static void runLSHPredictionPerformanceTests(
            AbstractLSHRecommender lshRecommender, String type, String testType, String dataFileBase, String val,
            int numOfRun, int l, int k, String separator, double smoothRun, int kNN, int y)
    {
        int numOfBands;
        int numOfHashFunctions;
        if (testType == "HashFunctions") {
            numOfBands = l;
            numOfHashFunctions = 4;
        } else if (testType == "HashTables"){
            numOfBands = 4;
            numOfHashFunctions = k;
        } else {
            throw new UnsupportedOperationException("Invalid type.");
        }
        ArrayList<Double> runtimeList = new ArrayList<>();
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> predictedItemsList = new ArrayList<>();
        ArrayList<Double> candidate_set_list = new ArrayList<>();
        HashObjObjMap<Object, Object> hashTables;
        for (int i = 0; i < numOfRun; i++) {
            double runTime = 0;
            double mae = 0;
            double candidate_set_size = 0;
            double predictedItems = 0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForValidation(dataFileBase, (j+1), val, separator);
                lshRecommender.buildModel(userRateMap, itemRateMap, numOfBands, numOfHashFunctions);
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
                    predictedItems += UBKNNLSHPrediction.getOutputList().size();
                } else if (type == "IBKNNLSH") {
                    hashTables = lshRecommender.getHashTables();
                    hashKeyLookupTable = lshRecommender.getHashKeyLookupTable();
                    runTime += IBKNNLSHPrediction.
                            runItemBasedLSHPredictionOnTestData
                                    (itemRateMap, userRateMap, testDataMap, hashTables, hashKeyLookupTable, kNN, y);
                    candidate_set_size += IBKNNLSHPrediction
                            .getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            IBKNNLSHPrediction.getOutputList(),
                            IBKNNLSHPrediction.getTargetList());
                    predictedItems += IBKNNLSHPrediction.getOutputList().size();
                } else if (type == "UBLSH1" || type == "UBLSH2" || type == "UBLSH3") {
                    runTime += LSHPredictionTests.runUBLSHPredictionOnTestData
                            (userRateMap, itemRateMap, testDataMap, lshRecommender);
                    candidate_set_size += LSHPredictionTests.getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            LSHPredictionTests.getOutputList(),
                            LSHPredictionTests.getTargetList());
                    predictedItems += LSHPredictionTests.getOutputList().size();
                } else if (type == "IBLSH1" || type == "IBLSH2" ){
                    runTime += LSHPredictionTests.runIBLSHPredictionOnTestData
                            (userRateMap, itemRateMap, testDataMap, lshRecommender);
                    candidate_set_size += LSHPredictionTests.getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            LSHPredictionTests.getOutputList(),
                            LSHPredictionTests.getTargetList());
                    predictedItems += LSHPredictionTests.getOutputList().size();
                } else throw new UnsupportedOperationException("Invalid type.");

            }
            maeList.add(mae / smoothRun);
            runtimeList.add(runTime / smoothRun);
            candidate_set_list.add(candidate_set_size / smoothRun);
            predictedItemsList.add(predictedItems / smoothRun);

            LOG.info("numOfBands:" + numOfBands
                    + " numOfHashFunctions:" + numOfHashFunctions);
            LOG.info("Mae: " + mae/ smoothRun);
            LOG.info("Runtime: " + runTime/ smoothRun);
            LOG.info("Predicted Items :" + (predictedItems / smoothRun));
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
        LOG2.info(type + testType + "PredictedItemsList  = " + predictedItemsList.toString() + ";");
    }


}
