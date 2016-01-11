package com.zaana.ml.tests;

import com.zaana.ml.*;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maruf on 07/05/15.
 */
public class PrecisionTest extends AbstractTest {

    /**
     * Runs User-based precision against k parameter.
     */
    public static void runUBPrecisionTests(String dataFileBase, String separator,
                                           int l, int k, double smoothRun, int topN, int kNN, int y) {
        runCFPrecisionTests(dataFileBase, "UB", separator, l, k, smoothRun, topN, kNN, y);
    }

    /**
     * Runs Item-based precision
     */
    public static void runIBPrecisionTests(String dataFileBase, String separator,
                                           int l, int k, double smoothRun, int topN, int kNN, int y) {
        runCFPrecisionTests(dataFileBase, "IB", separator, l, k, smoothRun, topN, kNN, y);
    }

    private static void runCFPrecisionTests(
            String dataFileBase, String type, String separator,
            int l, int k, double smoothRun, int topN, int kNN, int y) {
        LOG.info("Running precision simulation...");
        double totalPrecision = 0.0;
        for (int j = 0; j < smoothRun; j++) {
            preprocessDataForRecommendation(dataFileBase, (j + 1), separator, smoothRun, l, k);
            Set<String> itemSet = itemRateMap.keySet();
            Set<String> userSet = userRateMap.keySet();
            double precision;
            if (type == "UB") {
                precision = UBPrecision.calculateUBPrecision(
                        userRateMap, testDataMap, itemSet, topN, kNN, y);
            } else if (type == "IB") {
                precision = IBPrecision.calculateItemBasedPrecision(
                        userRateMap, itemRateMap, testDataMap, itemSet, topN, kNN, y);
            } else {
                throw new UnsupportedOperationException("Invalid operation for CF type.");
            }
            totalPrecision += precision;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Precision");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(type + "Precision = " + totalPrecision / smoothRun);

    }

    /**
     * Runs 2D LSH Precision tests.
     */
    public static void run2DLSHPrecisionTests(
            String testType, String dataFileBase,
            int numOfRun, double smoothRun, String separator, int topN, int kNN, int y) {
        int numOfBands = 1;
        int numOfHashFunctions = 1;
        ArrayList<Object> precisionList2D = new ArrayList<>();
        ArrayList<Object> recallList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> precisionList = new ArrayList<>();
            ArrayList<Double> recallList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                double precision = 0;
                double recall = 0;
                double candidate_set_size = 0;
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForRecommendation(dataFileBase, (s + 1), separator, smoothRun, numOfBands, numOfHashFunctions);
                    Set<String> itemSet = itemRateMap.keySet();
                    Set<String> userSet = userRateMap.keySet();
                    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                    if (testType == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildIndexTables(userRateMap, vmap, numOfBands);
                        hashKeyLookupTable = LSH.getHashKeyLookupTable();
                        UBLSHPrecisionRecall.calculateUBLSHPrecisionRecall(
                                userRateMap, testDataMap, hashTables, vmap, hashKeyLookupTable, itemSet, topN, kNN, y);
                        precision += UBLSHPrecisionRecall.getPrecision();
                        recall += UBLSHPrecisionRecall.getRecall();
                        candidate_set_size += UBLSHPrecisionRecall.getCandidate_size();
                    } else if (testType == "IBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                        hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                        hashKeyLookupTable = LSH.getHashKeyLookupTable();
                        IBLSHPrecisionRecall.calculateIBLSHPrecisionRecall(
                                userRateMap, itemRateMap, testDataMap, hashTables, vmap, hashKeyLookupTable, itemSet, topN, kNN, y);
                        precision += IBLSHPrecisionRecall.getPrecision();
                        recall += IBLSHPrecisionRecall.getRecall();
                    } else if (testType == "LSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                        hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                        hashKeyLookupTable = LSH.getHashKeyLookupTable();
                        LSHPrecisionRecall.calculateLSHPrecision(
                                userRateMap, itemRateMap, testDataMap, hashTables, vmap, hashKeyLookupTable, topN);
                        precision = LSHPrecisionRecall.getPrecision();
                        recall = LSHPrecisionRecall.getRecall();
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                }
                LOG.info("numOfBands = " + numOfBands + " numOfHashFunctions = " + numOfHashFunctions);
                LOG.info(testType + "Precision = " + precision/smoothRun);
                LOG.info(testType + "Recall = " + recall/smoothRun);
                precisionList.add(precision/smoothRun);
                recallList.add(recall/smoothRun);
                candidate_set_list.add(candidate_set_size/smoothRun);
                numOfHashFunctions += 1;
            }
            precisionList2D.add(precisionList);
            recallList2D.add(recallList);
            candidate_set_list2D.add(candidate_set_list);
            numOfHashFunctions = 1;
            numOfBands += 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + testType + " 2D - test");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info("y = " + y);
        LOG2.info(testType + "Precision2D = " + precisionList2D.toString() + ";");
        LOG2.info(testType + "Recall2D = " + recallList2D.toString() + ";");
        LOG2.info(testType + "Candidate_Set_List2D = " + candidate_set_list2D.toString() + ";");
    }

    /**
     * Precision Hash Function Test
     * @param testType
     * @param dataFileBase
     * @param numOfBands
     * @param numOfHashFunctions
     * @param numOfRun
     * @param smoothRun
     * @param separator
     * @param topN
     * @param kNN
     * @param y
     */
    public static void runLSHPrecisionHashFunctionsTests(
            String testType, String dataFileBase,
            int numOfBands, int numOfHashFunctions, int numOfRun, double smoothRun,
            String separator, int topN, int kNN, int y)
    {
        numOfHashFunctions = 1;
        ArrayList<Double> precisionList = new ArrayList<>();
        ArrayList<Double> recallList = new ArrayList<>();
        ArrayList<Double> candidate_set_list = new ArrayList<>();
        for (int j = 0; j < numOfRun; j++) {
            double precision = 0;
            double recall = 0;
            double candidate_set_size = 0;
            for (int s = 0; s < smoothRun; s++) {
                preprocessDataForRecommendation(dataFileBase, (s + 1), separator, smoothRun, numOfBands, numOfHashFunctions);
                Set<String> itemSet = itemRateMap.keySet();
                Set<String> userSet = userRateMap.keySet();
                HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                if (testType == "UBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                    hashTables = LSH.buildIndexTables(userRateMap, vmap, numOfBands);
                    hashKeyLookupTable = LSH.getHashKeyLookupTable();
                    UBLSHPrecisionRecall.calculateUBLSHPrecisionRecall(
                            userRateMap, testDataMap, hashTables, vmap, hashKeyLookupTable, itemSet, topN, kNN, y);
                    precision += UBLSHPrecisionRecall.getPrecision();
                    recall += UBLSHPrecisionRecall.getRecall();
                    candidate_set_size += UBLSHPrecisionRecall.getCandidate_size();
                } else if (testType == "IBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                    hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                    hashKeyLookupTable = LSH.getHashKeyLookupTable();
                    IBLSHPrecisionRecall.calculateIBLSHPrecisionRecall(
                            userRateMap, itemRateMap, testDataMap, hashTables, vmap, hashKeyLookupTable, itemSet, topN, kNN, y);
                    precision += IBLSHPrecisionRecall.getPrecision();
                    recall += IBLSHPrecisionRecall.getRecall();
                } else if (testType == "LSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                    hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                    hashKeyLookupTable = LSH.getHashKeyLookupTable();
                    LSHPrecisionRecall.calculateLSHPrecision(
                            userRateMap, itemRateMap, testDataMap, hashTables, vmap, hashKeyLookupTable, topN);
                    precision += LSHPrecisionRecall.getPrecision();
                    recall += LSHPrecisionRecall.getRecall();
                } else {
                    throw new UnsupportedOperationException("Invalid type.");
                }
            }
            LOG.info("numOfBands = " + numOfBands + " numOfHashFunctions = " + numOfHashFunctions);
            LOG.info(testType + "Precision = " + precision/smoothRun);
            LOG.info(testType + "Recall = " + recall / smoothRun);
            precisionList.add(precision/smoothRun);
            recallList.add(recall/smoothRun);
            candidate_set_list.add(candidate_set_size/smoothRun);
            numOfHashFunctions += 1;
            }

        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + testType + "HashFunctions - test");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info("y = " + y);
        LOG2.info(testType + "HashFunctionsPrecision = " + precisionList.toString() + ";");
        LOG2.info(testType + "HashFunctionsRecall = " + recallList.toString() + ";");
        LOG2.info(testType + "HashFunctionsCandidateSetList = " + candidate_set_list.toString() + ";");
    }


    /**
     * Precision Hash Tables Test
     * @param testType
     * @param dataFileBase
     * @param numOfBands
     * @param numOfRun
     * @param numOfHashFunctions
     * @param smoothRun
     * @param separator
     * @param topN
     * @param kNN
     * @param y
     */
    public static void runLSHPrecisionHashTablesTests(
            String testType, String dataFileBase,
            int numOfBands, int numOfHashFunctions, int numOfRun, double smoothRun,
            String separator, int topN, int kNN, int y)
    {

        ArrayList<Double> precisionList = new ArrayList<>();
        ArrayList<Double> recallList = new ArrayList<>();
        ArrayList<Double> candidate_set_list = new ArrayList<>();
        numOfBands = 1;
        for (int j = 0; j < numOfRun; j++) {
            double precision = 0;
            double recall = 0;
            double candidate_set_size = 0;
            for (int s = 0; s < smoothRun; s++) {
                preprocessDataForRecommendation(dataFileBase, (s + 1), separator, smoothRun, numOfBands, numOfHashFunctions);
                Set<String> itemSet = itemRateMap.keySet();
                Set<String> userSet = userRateMap.keySet();
                HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                if (testType == "UBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                    hashTables = LSH.buildIndexTables(userRateMap, vmap, numOfBands);
                    hashKeyLookupTable = LSH.getHashKeyLookupTable();
                    UBLSHPrecisionRecall.calculateUBLSHPrecisionRecall(
                            userRateMap, testDataMap, hashTables, vmap, hashKeyLookupTable, itemSet, topN, kNN, y);
                    precision += UBLSHPrecisionRecall.getPrecision();
                    recall += UBLSHPrecisionRecall.getRecall();
                    candidate_set_size += UBLSHPrecisionRecall.getCandidate_size();
                } else if (testType == "IBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                    hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                    hashKeyLookupTable = LSH.getHashKeyLookupTable();
                    IBLSHPrecisionRecall.calculateIBLSHPrecisionRecall(
                            userRateMap, itemRateMap, testDataMap, hashTables, vmap, hashKeyLookupTable, itemSet, topN, kNN, y);
                    precision += IBLSHPrecisionRecall.getPrecision();
                    recall += IBLSHPrecisionRecall.getRecall();
                } else if (testType == "LSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                    hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                    hashKeyLookupTable = LSH.getHashKeyLookupTable();
                    LSHPrecisionRecall.calculateLSHPrecision(
                            userRateMap, itemRateMap, testDataMap, hashTables, vmap, hashKeyLookupTable, topN);
                    precision = LSHPrecisionRecall.getPrecision();
                    recall = LSHPrecisionRecall.getRecall();
                } else {
                    throw new UnsupportedOperationException("Invalid type.");
                }
            }
            LOG.info("numOfBands = " + numOfBands + " numOfHashFunctions = " + numOfHashFunctions);
            LOG.info(testType + "Precision = " + precision/smoothRun);
            LOG.info(testType + "Recall = " + recall/smoothRun);
            precisionList.add(precision/smoothRun);
            recallList.add(recall/smoothRun);
            candidate_set_list.add(candidate_set_size/smoothRun);
            numOfBands += 1;
        }

        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + testType + "HashTables - test");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info("y = " + y);
        LOG2.info(testType + "HashTablesPrecision = " + precisionList.toString() + ";");
        LOG2.info(testType + "HashTablesRecall = " + recallList.toString() + ";");
        LOG2.info(testType + "HashTablesCandidateSetList = " + candidate_set_list.toString() + ";");
    }

}
