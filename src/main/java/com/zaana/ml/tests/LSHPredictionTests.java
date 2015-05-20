package com.zaana.ml.tests;

import com.zaana.ml.*;
import com.zaana.ml.prediction.AbstractPrediction;
import com.zaana.ml.prediction.LSHPrediction;
import com.zaana.ml.prediction.UBLSHPrediction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by maruf on 06/05/15.
 */
public class LSHPredictionTests extends AbstractTests
{

    /**
     * Runs the Number of Hash Functions and k near neighbor parameter tests.
     * To determine if k and Hash Functions correlate. */
    public static void runLSHHashFunctionsAndKTest(
            String type, String dataFileBase, String val,
            int numOfRun, double smoothRun,
            String separator, int kNN, int y) {
        runLSH2DHashFunctionsAndParamTests(type, dataFileBase, "k", val, numOfRun, smoothRun, separator, kNN, y);
    }


    /**
     * Runs the Number of Hash Functions and significance (y) parameter tests.
     * To determine if y and Hash Functions correlate */
    public static void runLSHHashFunctionsAndYTest(
            String type, String dataFileBase, String val,
            int numOfRun, int smoothRun,
            String separator, int kNN, int y) {
        runLSH2DHashFunctionsAndParamTests(type, dataFileBase, "y", val, numOfRun, smoothRun, separator, kNN, y);
    }


    /**
     * Runs the Number of Hash Tables and k near neighbor parameter tests.
     * To determine if k and Hash Functions correlate. */
    public static void runLSHHashTablesAndKTest(
            String type, String dataFileBase, String val,
            int numOfRun, double smoothRun,
            String separator, int kNN, int y) {
        runLSH2DHashTablesAndParamTests(type, dataFileBase, "k", val, numOfRun, smoothRun, separator, kNN, y);
    }


    /**
     * Runs the Number of Hash Tables and significance (y) parameter tests.
     * To determine if y and Hash Functions correlate */
    public static void runLSHHashTablesAndYTest(
            String type, String dataFileBase, String val,
            int numOfRun, int smoothRun,
            String separator, int kNN, int y) {
        runLSH2DHashTablesAndParamTests(type, dataFileBase, "y", val, numOfRun, smoothRun, separator, kNN, y);
    }


    /**
     * Runs LSH prediction tests for HashTables to detect the effect of changes
     * in HashTable to the prediction accuracy.*/
    public static void runLSHHashTablesAndPrediction(
            String type, String dataFileBase, String val, String separator,
            int numOfRun, int numberOfHashTables, int numOfHashFunctions,
            double smoothRun, int kNN, int y) {

        runLSHPredictionPerformanceTests(
                type, "HashTables", dataFileBase, val, numOfRun, numberOfHashTables, numOfHashFunctions,
                separator, smoothRun, kNN, y);
    }


    /**
     * Runs LSH prediction tests for HashFunctions to detect the effect of changes
     * in HashFunctions to the prediction accuracy.*/
    public static void runLSHHashFunctionsAndPrediction(
            String type, String dataFileBase, String val, String separator,
            int numOfRun, int numberOfHashTables, int numOfHashFunctions,
            double smoothRun, int kNN, int y) {

        runLSHPredictionPerformanceTests(
                type, "HashFunctions", dataFileBase, val, numOfRun, numberOfHashTables, numOfHashFunctions,
                separator, smoothRun, kNN, y);
    }

    /**
     * Runs the k and y parameters 2D test for LSH.*/
    public static void runLSHYAndKTest(
            String type, String dataFileBase, String val,
            int numOfRun, double smoothRun,
            String separator, int l, int k) {
        runLSH2DYAndKTests(type, dataFileBase, val, numOfRun, smoothRun, separator, l, k);
    }

    private static void runLSH2DYAndKTests(String type, String dataFileBase, String val,
                                           int numOfRun, double smoothRun, String separator, int l, int k) {
        ArrayList<Object> runTimeList2D = new ArrayList<>();
        ArrayList<Object> maeList2D = new ArrayList<>();
        HashMap<Integer, HashMap<String, Set<String>>> hashTables;
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        int kNN = 1;
        int y = 1;
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> runtimeList = new ArrayList<>();
            ArrayList<Double> maeList = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                double totalMae = 0;
                long runTimeTotal = 0;
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForValidation(dataFileBase, (s + 1), val, separator);
                    Set<String> itemSet = itemRateMap.keySet();
                    if (type == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, l, k, itemSet);
                        hashTables = LSH.buildIndexTables(userRateMap, vmap, l);
                        runTimeTotal += UBLSHPrediction.runUserBasedLSHPredictionOnTestData(
                                userRateMap, itemRateMap, testDataMap, hashTables, vmap, kNN, y);
                    } else {
                        throw new UnsupportedOperationException("Invalid operation for LSH type.");
                    }
                    totalMae += MAE.calculateMAE(
                            AbstractPrediction.getOutputList(),
                            AbstractPrediction.getTargetList());
                }
                LOG.info("k: " + kNN);
                LOG.info("y: " + y);
                LOG.info(type + "Mae2D: " + totalMae / smoothRun);
                LOG.info(type + "Runtime2D: " + runTimeTotal / smoothRun);
                maeList.add(totalMae / smoothRun);
                runtimeList.add(runTimeTotal / smoothRun);
                y += 5;
            }
            runTimeList2D.add(runtimeList);
            maeList2D.add(maeList);
            kNN += 3;
            y = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " k and y 2D ");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(type + "MaeKAndYList2D = " + maeList2D.toString() + ";");
        LOG2.info(type + "RunTimeKAndYList2D = " + runTimeList2D.toString() + ";");
    }


    /**
     *  Runs LSH prediction tests against k nearest neighbor parameter.
     *  To determine the best k parameter for pre configured LSH model.
     *  Number of hash functions and tables are set. */
    public static void runLSHAndKTest(
            String type, String dataFileBase,
            int numberOfHashTables, int numOfHashFunctions, double smoothRun, String separator, int y)
    {
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> runTimeList = new ArrayList<>();
        ArrayList<Double> candidateSetList = new ArrayList<>();
        HashMap<Integer, HashMap<String, Set<String>>> hashTables;
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        int kNN = 1;
        LOG.info("Running LSH Prediction Simulation for k = " + kNN);
        for (int i = 0; i < 10; i++) {
            double totalMae = 0.0;
            double runTimeTotal = 0;
            double totalCandSize = 0.0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForValidation(dataFileBase, (j + 1), "test", separator);
                Set<String> itemSet = itemRateMap.keySet();
                if (type == "UBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numberOfHashTables, numOfHashFunctions, itemSet);
                    hashTables = LSH.buildIndexTables(userRateMap, vmap, numberOfHashTables);
                    runTimeTotal += UBLSHPrediction.runUserBasedLSHPredictionOnTestData(
                            userRateMap, itemRateMap, testDataMap, hashTables, vmap, kNN, y);
                } else {
                    throw new UnsupportedOperationException("Invalid operation for LSH type.");
                }
                totalMae += MAE.calculateMAE(
                        AbstractPrediction.getOutputList(),
                        AbstractPrediction.getTargetList());

                totalCandSize += UBLSHPrediction.getAvg_candidate_set_size();
            }
            maeList.add(totalMae / smoothRun);
            runTimeList.add(runTimeTotal / smoothRun);
            candidateSetList.add(totalCandSize / smoothRun);
            kNN += 3;
            LOG.info("k = " + kNN);
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case " + type + " vs. k - Prediction");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(type + "MaeList = " + maeList.toString());
        LOG2.info(type + "Runtime = " + runTimeList.toString());
        LOG2.info(type + "CandidateSetList = " + candidateSetList.toString());

    }


    /**
     * Runs 2D LSH HashTables and HashFunctions tests to determine the effect
     * of these parameters on prediction accuracy. Output will be 2D graph.*/
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
                    preprocessDataForValidation(dataFileBase, (s + 1), "val", separator);
                    Set<String> itemSet = itemRateMap.keySet();
                    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                    if (testType == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildIndexTables(userRateMap, vmap,
                                numOfBands);
                        runTime += UBLSHPrediction
                                .runUserBasedLSHPredictionOnTestData(userRateMap,
                                        itemRateMap, testDataMap, hashTables, vmap, kNN, y);
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                    candidate_set_size += UBLSHPrediction.getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            UBLSHPrediction.getOutputList(),
                            UBLSHPrediction.getTargetList());
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


    /**
     * Runs 2D tests for k and y parameters and number of HashFunctions.
     * Callled from runLSHHashFunctionsAndKTest and runLSHHashFunctionsAndYTest methods.*/
    private  static void runLSH2DHashFunctionsAndParamTests(
            String testType, String dataFileBase, String param, String val,
            int numOfRun, double smoothRun, String separator, int kNN, int y)
    {
        int numOfBands = 4; // set 1 band to measure only hash functions effect
        int numOfHashFunctions = 1;
        ArrayList<Object> runTimeList2D = new ArrayList<>();
        ArrayList<Object> maeList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        if (param == "k") {
           kNN = 1;
        } else if(param == "y") {
           y = 1;
        } else {
            throw new UnsupportedOperationException("Invalid parameter: " + param);
        }
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> hashFuncRuntimeList = new ArrayList<>();
            ArrayList<Double> hashFuncMaeList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                long runTime = (long) 0;
                double mae = 0;
                double candidate_set_size = 0;
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForValidation(dataFileBase, (s + 1), val,separator);
                    Set<String> itemSet = itemRateMap.keySet();
                    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                    if (testType == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildIndexTables(userRateMap, vmap,
                                numOfBands);
                        runTime += UBLSHPrediction.runUserBasedLSHPredictionOnTestData(
                                userRateMap, itemRateMap, testDataMap, hashTables, vmap, kNN, y);
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                    candidate_set_size += UBLSHPrediction.getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            UBLSHPrediction.getOutputList(),
                            UBLSHPrediction.getTargetList());
                }
                LOG.info("numOfBands = " + numOfBands
                        + " numOfHashFunctions = " + numOfHashFunctions);
                LOG.info("k = " + kNN);
                LOG.info("y = " + y);
                LOG.info(testType + "MAE = " + mae /smoothRun);
                LOG.info(testType + "Runtime = " + runTime /smoothRun);
                hashFuncMaeList.add(mae /smoothRun);
                hashFuncRuntimeList.add(runTime /smoothRun);
                candidate_set_list.add(candidate_set_size /smoothRun);
                numOfHashFunctions += 1;
            }
            runTimeList2D.add(hashFuncRuntimeList);
            maeList2D.add(hashFuncMaeList);
            candidate_set_list2D.add(candidate_set_list);
            if (param == "k") {
               kNN += 3;
            } else if(param == "y") {
               y += 3;
            }
            numOfHashFunctions = 1;
        }
       LOG2.info("# ========================================================");
       LOG2.info("# test case: " + testType + " 2D - Hash Functions vs. " + param);
       LOG2.info("# ========================================================");
       LOG2.info("dataFileBase = " + dataFileBase);
       LOG2.info("k = " + kNN);
       LOG2.info("y = " + y);
       LOG2.info(testType + param + "HashFunctionsMae2D = " + maeList2D.toString() + ";");
       LOG2.info(testType + param + "Runtime2D = " + runTimeList2D.toString() + ";");
       LOG2.info(testType + param + "Candidate_Set_List2D = " + candidate_set_list2D.toString() + ";");

    }



    /**
     * Runs 2D tests for k and y parameters and number of HashTables.
     * Callled from runLSHHashTablesAndKTest and runLSHHashTablesAndYTest methods.*/
    private  static void runLSH2DHashTablesAndParamTests(
            String testType, String dataFileBase, String param, String val,
            int numOfRun, double smoothRun, String separator, int kNN, int y)
    {
        int numOfBands = 1;
        int numOfHashFunctions = 4;
        ArrayList<Object> runTimeList2D = new ArrayList<>();
        ArrayList<Object> maeList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        if (param == "k") {
            kNN = 1;
        } else if(param == "y") {
            y = 1;
        } else {
            throw new UnsupportedOperationException("Invalid parameter: " + param);
        }
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> hashFuncRuntimeList = new ArrayList<>();
            ArrayList<Double> hashFuncMaeList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                long runTime = (long) 0;
                double mae = 0;
                double candidate_set_size = 0;
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForValidation(dataFileBase, (s + 1), val,separator);
                    Set<String> itemSet = itemRateMap.keySet();
                    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                    if (testType == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildIndexTables(userRateMap, vmap,
                                numOfBands);
                        runTime += UBLSHPrediction.runUserBasedLSHPredictionOnTestData(
                                userRateMap, itemRateMap, testDataMap, hashTables, vmap, kNN, y);
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                    candidate_set_size += UBLSHPrediction.getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            UBLSHPrediction.getOutputList(),
                            UBLSHPrediction.getTargetList());
                }
                LOG.info("numOfBands = " + numOfBands
                        + " numOfHashFunctions = " + numOfHashFunctions);
                LOG.info("k = " + kNN);
                LOG.info("y = " + y);
                LOG.info(testType + "MAE = " + mae /smoothRun);
                LOG.info(testType + "Runtime = " + runTime /smoothRun);
                hashFuncMaeList.add(mae /smoothRun);
                hashFuncRuntimeList.add(runTime /smoothRun);
                candidate_set_list.add(candidate_set_size /smoothRun);
                numOfBands += 1;
            }
            runTimeList2D.add(hashFuncRuntimeList);
            maeList2D.add(hashFuncMaeList);
            candidate_set_list2D.add(candidate_set_list);
            if (param == "k") {
                kNN += 3;
            } else if(param == "y") {
                y += 3;
            }
            numOfBands = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + testType + " 2D - Hash Tables vs. " + param);
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info("y = " + y);
        LOG2.info(testType + param + "HashTablesMae2D = " + maeList2D.toString() + ";");
        LOG2.info(testType + param + "Runtime2D = " + runTimeList2D.toString() + ";");
        LOG2.info(testType + param + "Candidate_Set_List2D = " + candidate_set_list2D.toString() + ";");

    }


    /**
     * Runs 2D UB LSH HashTables and HashFunctions tests to determine the effect
     * of these parameters on prediction accuracy. Output will be 2D graph.*/
    private static void runLSHPredictionPerformanceTests(
            String type, String testType, String dataFileBase, String val,
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
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        HashMap<Integer, HashMap<String, Set<String>>> hashTables;
        for (int i = 0; i < numOfRun; i++) {
            long runTime = (long) 0;
            double mae = 0;
            double candidate_set_size = 0;
            double predictedItems = 0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForValidation(dataFileBase, (j + 1), val, separator);
                Set<String> itemSet = itemRateMap.keySet();
                Set<String> userSet = userRateMap.keySet();
                if (type == "UBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                    hashTables = LSH.buildIndexTables(userRateMap, vmap,
                            numOfBands);
                    runTime += UBLSHPrediction.runUserBasedLSHPredictionOnTestData(
                            userRateMap, itemRateMap, testDataMap, hashTables, vmap, kNN, y);
                } else if (type == "LSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                    hashTables = LSH.buildIndexTables(userRateMap, vmap,
                            numOfBands);
                    runTime += LSHPrediction.runLSHPredictionOnTestData(
                            userRateMap, itemRateMap, testDataMap, hashTables, vmap, kNN, y);
                } else {
                    throw new UnsupportedOperationException("Invalid type.");
                }
                candidate_set_size = AbstractPrediction
                        .getAvg_candidate_set_size();
                mae += MAE.calculateMAE(
                        AbstractPrediction.getOutputList(),
                        AbstractPrediction.getTargetList());
                predictedItems += AbstractPrediction.getOutputList().size();
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
        LOG2.info(type + "CandidateSetList = " + candidate_set_list + ";");
        LOG2.info("PredictedItemsList  = " + predictedItemsList.toString() + ";");
    }


}
