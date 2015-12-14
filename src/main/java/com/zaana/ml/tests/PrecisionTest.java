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

    /**
     * Runs User-based precision against k parameter.
     */
    public static void runUBPrecisionAndKTests(String dataFileBase, String separator,
                                               int l, int k, double smoothRun, int topN, int y) {
        runPrecisionAndKTests(dataFileBase, "UB", separator, l, k, smoothRun, topN, y);
    }


    /**
     * Runs User-based LSH precision against k parameter.
     */
    public static void runUBLSHPrecisionAndKTest(String dataFileBase, String separator,
                                                 int l, int k, double smoothRun, int topN, int y) {
        runPrecisionAndKTests(dataFileBase, "UBLSH", separator, l, k, smoothRun, topN, y);
    }

    /**
     * Runs UBLSH precision 2D test for k and y parameters
     */
    public static void runUBLSHPrecisionKAndYTest(String dataFileBase, String separator,
                                                  int l, int k, int smoothRun, int topN, int numOfRun) {
        runPrecisionKAndYTest(dataFileBase, "UBLSH", separator, l, k, smoothRun, numOfRun, topN);
    }

    /**
     * Runs user-based precision 2D test for k and y parameters
     */
    public static void runUBPrecisionKAndYTest(String dataFileBase, String seperator,
                                               int l, int k, int smoothRun, int topN, int numOfRun) {
        runPrecisionKAndYTest(dataFileBase, "UB", seperator, l, k, smoothRun, numOfRun, topN);
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
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> precisionList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                double precision = 0;
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
                        precision += UBLSHPrecision.calculateUBLSHPrecision(
                                userRateMap, testDataMap, hashTables, vmap, itemSet, topN, kNN, y);
                        candidate_set_size += UBLSHPrecision.getCandidate_size();
                    } else if (testType == "IBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                        hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                        precision = IBLSHPrecision.calculateIBLSHPrecision(
                                userRateMap, itemRateMap, testDataMap, hashTables, vmap, itemSet, topN, kNN, y);
                    } else if (testType == "LSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                        hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                        itemHashKeyTable = LSH.getItemKeyTable();
                        precision = LSHPrecision.calculateLSHPrecision(
                                userRateMap, itemRateMap, testDataMap, hashTables, vmap, itemHashKeyTable, topN);
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                }
                LOG.info("numOfBands = " + numOfBands
                        + " numOfHashFunctions = " + numOfHashFunctions);
                LOG.info(testType + "precision = " + precision / smoothRun);
                precisionList.add(precision / smoothRun);
                candidate_set_list.add(candidate_set_size / smoothRun);
                numOfHashFunctions += 1;
            }
            precisionList2D.add(precisionList);
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
        LOG2.info(testType + "Candidate_Set_List2D = " + candidate_set_list2D.toString() + ";");
    }

    private static void runPrecisionAndKTests(
            String dataFileBase, String type, String separator,
            int l, int k, double smoothRun, int topN, int y) {
        LOG.info("Running precision simulation...");
        double totalPrecision;
        HashMap<Integer, HashMap<String, Set<String>>> tables;
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        ArrayList<Double> precisionList = new ArrayList<>();
        //prepareHashTables(dataFileBase, smoothRun, separator, l, k);
        int kNN = 1;
        for (int i = 0; i < 10; i++) {
            totalPrecision = 0.0;
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
                } else if (type == "UBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, l, k, itemSet);
                    tables = LSH.buildIndexTables(userRateMap, vmap, l);
                    precision = UBLSHPrecision.calculateUBLSHPrecision(
                            userRateMap, testDataMap, tables, vmap, itemSet, topN, kNN, y);
                } else if (type == "IBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, l, k, userSet);
                    tables = LSH.buildIndexTables(itemRateMap, vmap, l);
                    precision = IBLSHPrecision.calculateIBLSHPrecision(
                            userRateMap, itemRateMap, testDataMap, tables, vmap, itemSet, topN, kNN, y);
                } else {
                    throw new UnsupportedOperationException("Invalid operation for CF type.");
                }
                totalPrecision += precision;
            }
            LOG.info(type + "Precision = " + totalPrecision / smoothRun);
            precisionList.add(totalPrecision / smoothRun);

            kNN += 3;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Precision");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(type + "PrecisionAndKList = " + precisionList.toString());

    }


    /**
     * Runs 2D tests for k parameter and number of HashFunctions.
     */
    public static void run2DPrecisionHashFunctionsAndKTests(
            String testType, String dataFileBase,
            int numOfRun, double smoothRun, String separator, int topN, int y) {
        int numOfBands = 4; // set 1 band to measure only hash functions effect
        int numOfHashFunctions = 1;
        int kNN = 1;
        ArrayList<Object> precisionList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> precisionList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                double precision = 0;
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
                        precision += UBLSHPrecision.calculateUBLSHPrecision(
                                userRateMap, testDataMap, hashTables, vmap, itemSet, topN, kNN, y);
                        //int kNN, int topN, int y
                        candidate_set_size += UBLSHPrecision.getCandidate_size();
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                }
                LOG.info("numOfBands = " + numOfBands
                        + " numOfHashFunctions = " + numOfHashFunctions);
                LOG.info("k = " + kNN);
                LOG.info(testType + "precision = " + precision / smoothRun);
                precisionList.add(precision / smoothRun);
                candidate_set_list.add(candidate_set_size / smoothRun);
                numOfHashFunctions += 1;
            }
            precisionList2D.add(precisionList);
            candidate_set_list2D.add(candidate_set_list);

            kNN += 3;
            numOfHashFunctions = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + testType + " 2D - Hash Functions vs. k");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info(testType + "AndKPrecision2D = " + precisionList2D.toString() + ";");
        LOG2.info(testType + "AndKCandidate_Set_List2D = " + candidate_set_list2D.toString() + ";");
    }

    /**
     * Runs 2D tests for k parameter and number of HashTables.
     */
    public static void run2DPrecisionHashTablesAndKTests(
            String testType, String dataFileBase,
            int numOfRun, int smoothRun, String separator, int topN, int y) {
        int numOfBands = 1; // set 1 band to measure only hash functions effect
        int numOfHashFunctions = 4;
        int kNN = 1;
        ArrayList<Object> precisionList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> precisionList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                double precision = 0;
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
                        precision += UBLSHPrecision.calculateUBLSHPrecision(
                                userRateMap, testDataMap, hashTables, vmap, itemSet, topN, kNN, y);
                        candidate_set_size += UBLSHPrecision.getCandidate_size();

                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                }
                LOG.info("numOfBands = " + numOfBands
                        + " numOfHashFunctions = " + numOfHashFunctions);
                LOG.info("k = " + kNN);
                LOG.info(testType + "precision = " + precision / smoothRun);
                precisionList.add(precision / smoothRun);
                candidate_set_list.add(candidate_set_size / smoothRun);
                numOfBands += 1;
            }
            precisionList2D.add(precisionList);
            candidate_set_list2D.add(candidate_set_list);

            kNN += 3;
            numOfBands = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + testType + " 2D - Hash Tables vs. k");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info(testType + "AndKPrecision2D = " + precisionList2D.toString() + ";");
        LOG2.info(testType + "AndKCandidate_Set_List2D = " + candidate_set_list2D.toString() + ";");
    }


    /**
     * Runs precision test for Y parameter (signficance).
     */
    public static void runUBLSHPrecisionAndYTest(String dataFileBase, String type, String separator,
                                                 int l, int k, int smoothRun, int topN, int kNN) {
        LOG.info("Running precision simulation...");
        double totalPrecision;
        HashMap<Integer, HashMap<String, Set<String>>> tables;
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        ArrayList<Double> precisionList = new ArrayList<>();
        prepareHashTables(dataFileBase, smoothRun, separator, l, k);
        int y = 1;
        for (int i = 0; i < 10; i++) {
            totalPrecision = 0.0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForRecommendation(dataFileBase, (j + 1), separator, smoothRun, l, k);
                Set<String> itemSet = itemRateMap.keySet();
                Set<String> userSet = userRateMap.keySet();
                double precision;
                if (type == "UB") {
                    precision = UBPrecision.calculateUBPrecision(
                            userRateMap, testDataMap, itemSet, topN, kNN, y);
                } /*else if (type == "IB") {
                    precision = IBPrecision.calculateItemBasedPrecision(
                            userRateMap, itemRateMap, testDataMap, itemSet, kNN, topN);
                }*/ else if (type == "UBLSH") {
                    vmap = vmaps.get(j);
                    tables = hashTables.get(j);
                    precision = UBLSHPrecision.calculateUBLSHPrecision(
                            userRateMap, testDataMap, tables, vmap, itemSet, topN, kNN, y);
                }/*else if (type == "IBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, l, k, userSet);
                    hashTables = LSH.buildIndexTables(itemRateMap, vmap, l);
                    precision = IBLSHPrecision.calculateIBLSHPrecision(
                            userRateMap,itemRateMap,testDataMap,hashTables,vmap,itemSet,kNN,topN);
                }*/ else {
                    throw new UnsupportedOperationException("Invalid operation for CF type.");
                }
                totalPrecision += precision;
            }
            LOG.info(type + "Precision = " + totalPrecision / smoothRun);
            precisionList.add(totalPrecision / smoothRun);
            y += 3;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Precision");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(type + "PrecisionAndYList = " + precisionList.toString());
    }


    private static void runPrecisionKAndYTest(
            String dataFileBase, String type, String separator,
            int numOfBands, int numOfHashFunctions,
            int smoothRun, int numOfRun, int topN) {
        ArrayList<Object> precisionList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        prepareHashTables(dataFileBase, smoothRun, separator, numOfBands, numOfHashFunctions);
        int kNN = 1;
        int y = 1;
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Double> precisionList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                double precision = 0;
                double candidate_set_size = 0;
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForRecommendation(dataFileBase, (s + 1), separator, smoothRun, numOfBands, numOfHashFunctions);
                    Set<String> itemSet = itemRateMap.keySet();
                    Set<String> userSet = userRateMap.keySet();
                    if (type == "UB") {
                        precision += UBPrecision.calculateUBPrecision(
                                userRateMap, testDataMap, itemSet, topN, kNN, y);
                    } else if (type == "UBLSH") {
                        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap = vmaps.get(s);
                        HashMap<Integer, HashMap<String, Set<String>>> tables = hashTables.get(s);
                        precision += UBLSHPrecision.calculateUBLSHPrecision(
                                userRateMap, testDataMap, tables, vmap, itemSet, topN, kNN, y);
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                }
                LOG.info("k = " + kNN + " y = " + y);
                LOG.info(type + "precision = " + precision / smoothRun);
                precisionList.add(precision / smoothRun);
                candidate_set_list.add(candidate_set_size / smoothRun);
                y += 3;
            }
            precisionList2D.add(precisionList);
            candidate_set_list2D.add(candidate_set_list);

            kNN += 3;
            y = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " 2D - Precision (y & k) test");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("k = " + kNN);
        LOG2.info(type + "YAndKPrecision2D = " + precisionList2D.toString() + ";");
        LOG2.info(type + "YAndKCandidate_Set_List2D = " + candidate_set_list2D.toString() + ";");
    }
}
