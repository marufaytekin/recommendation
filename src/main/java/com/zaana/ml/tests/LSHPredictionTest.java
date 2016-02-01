package com.zaana.ml.tests;

import com.zaana.ml.*;
import com.zaana.ml.prediction.*;
import com.zaana.ml.recomm.lsh.AbstractLSHRecommender;

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

/*

    */
/**
     * Runs 2D LSH HashTables and HashFunctions tests to determine the effect
     * of these parameters on prediction accuracy. Output will be 2D graph.*//*

    public static void runLSH2DHashFunctionsTablesTest(
            String testType, int numOfRun, double smoothRun, String dataFileBase, String separator, int kNN, int y) {
        int numOfBands = 1;
        int numOfHashFunctions = 1;
        ArrayList<Object> runTimeList2D = new ArrayList<>();
        ArrayList<Object> maeList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> hashFuncRuntimeList = new ArrayList<>();
            ArrayList<Double> hashFuncMaeList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                double runTime = 0;
                double mae = 0;
                double candidate_set_size = 0;
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForValidation(dataFileBase, (s + 1), "test", separator);
                    Set<String> itemSet = itemRateMap.keySet();
                    Set<String> userSet = userRateMap.keySet();
                    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                    if (testType == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildModel(userRateMap, vmap,
                                numOfBands);
                        hashKeyLookupTable = LSH.getHashKeyLookupTable();
                        runTime += UBKNNLSHPrediction
                                .runUserBasedLSHPredictionOnTestData(userRateMap,
                                        itemRateMap, testDataMap, hashTables, kNN, y, hashKeyLookupTable);
                        candidate_set_size += UBKNNLSHPrediction.getAvg_candidate_set_size();
                        mae += MAE.calculateMAE(
                                UBKNNLSHPrediction.getOutputList(),
                                UBKNNLSHPrediction.getTargetList());
                    } else if (testType == "IBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                        hashTables = LSH.buildModel(itemRateMap, vmap, numOfBands);
                        hashKeyLookupTable = LSH.getHashKeyLookupTable();
                        runTime += IBKNNLSHPrediction.
                                runItemBasedLSHPredictionOnTestData(itemRateMap, userRateMap,
                                        testDataMap, hashTables, hashKeyLookupTable, kNN, y);
                        candidate_set_size += IBKNNLSHPrediction.getAvg_candidate_set_size();
                        mae += MAE.calculateMAE(
                                IBKNNLSHPrediction.getOutputList(),
                                IBKNNLSHPrediction.getTargetList());
                    } else if (testType == "LSH1") {
                        LSH1UBPrediction lshEstimator = new LSH1UBPrediction();
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildModel(userRateMap, vmap,
                                numOfBands);
                        hashKeyLookupTable =  LSH.getHashKeyLookupTable();
                        runTime += LSHPredictionTests.runLSHPredictionOnTestData(
                                userRateMap, itemRateMap, testDataMap, hashTables, hashKeyLookupTable, lshEstimator);
                        candidate_set_size += LSHPredictionTests.getAvg_candidate_set_size();
                        mae += MAE.calculateMAE(
                                LSHPredictionTests.getOutputList(),
                                LSHPredictionTests.getTargetList());
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                }
                LOG.info("numOfBands:" + numOfBands
                        + " numOfHashFunctions:" + numOfHashFunctions);
                LOG.info(testType + "MAE: " + mae / smoothRun);
                LOG.info(testType + "Runtime: " + runTime / smoothRun);
                hashFuncMaeList.add(mae / smoothRun);
                hashFuncRuntimeList.add(runTime / smoothRun);
                candidate_set_list.add(candidate_set_size / smoothRun);
                numOfHashFunctions += 1;
            }
            runTimeList2D.add(hashFuncRuntimeList);
            maeList2D.add(hashFuncMaeList);
            candidate_set_list2D.add(candidate_set_list);

            numOfBands += 1;
            numOfHashFunctions = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + testType + " 2D ");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info(testType + "Mae2D = " + maeList2D.toString() + ";");
        LOG2.info(testType + "Runtime2D = " + runTimeList2D.toString() + ";");
        LOG2.info(testType + "Candidate_Set_List2D = " + candidate_set_list2D.toString() + ";");

    }

*/

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
            numOfHashFunctions = 1;
        } else if (testType == "HashTables"){
            numOfBands = 1;
            numOfHashFunctions = k;
        } else {
            throw new UnsupportedOperationException("Invalid type.");
        }
        ArrayList<Double> runtimeList = new ArrayList<>();
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> predictedItemsList = new ArrayList<>();
        ArrayList<Double> candidate_set_list = new ArrayList<>();
        HashMap<Integer, HashMap<String, Set<String>>> hashTables;
        for (int i = 0; i < numOfRun; i++) {
            long runTime = (long) 0;
            double mae = 0;
            double candidate_set_size = 0;
            double predictedItems = 0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForValidation(dataFileBase, (j+1), val, separator);
                lshRecommender.buildModel(userRateMap, itemRateMap, numOfBands, numOfHashFunctions);
                if (type == "UBLSH-KNN") {
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
                } else if (type == "IBLSH-KNN") {
                    hashTables = lshRecommender.getHashTables();
                    hashKeyLookupTable = lshRecommender.getHashKeyLookupTable();
                    runTime += IBKNNLSHPrediction.
                                runItemBasedLSHPredictionOnTestData(itemRateMap, userRateMap,
                                        testDataMap, hashTables, hashKeyLookupTable, kNN, y);
                    candidate_set_size += IBKNNLSHPrediction
                            .getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            IBKNNLSHPrediction.getOutputList(),
                            IBKNNLSHPrediction.getTargetList());
                    predictedItems += IBKNNLSHPrediction.getOutputList().size();
                } else if (type == "LSH1-UB" || type == "LSH2-UB" || type == "LSH1-IB" || type == "LSH2-IB" ) {
                    runTime += LSHPredictionTests.runLSHPredictionOnTestData(
                            userRateMap, itemRateMap, testDataMap, lshRecommender);
                    candidate_set_size += LSHPredictionTests.getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            LSHPredictionTests.getOutputList(),
                            LSHPredictionTests.getTargetList());
                    predictedItems += LSHPredictionTests.getOutputList().size();
                } else {
                    throw new UnsupportedOperationException("Invalid type.");
                }
            }
            maeList.add(mae / smoothRun);
            runtimeList.add(runTime / smoothRun);
            candidate_set_list.add(candidate_set_size / smoothRun);
            predictedItemsList.add(predictedItems / smoothRun);

            LOG.info("numOfBands:" + numOfBands
                    + " numOfHashFunctions:" + numOfHashFunctions);
            LOG.info("Mae: " + mae/ smoothRun);
            LOG.info("Runtime: " + runTime/ smoothRun);
            LOG.info("Predicted Items :" + (predictedItems/ smoothRun));
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
