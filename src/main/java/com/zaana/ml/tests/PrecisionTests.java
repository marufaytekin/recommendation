package com.zaana.ml.tests;

import com.zaana.ml.LSH;
import com.zaana.ml.UBLSHPrecision;
import com.zaana.ml.UBPrecision;
import com.zaana.ml.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by maruf on 07/05/15.
 */
public class PrecisionTests extends AbstractTests {

    /**
     * Runs User-based precision against k parameter. */
    public static void runUBPrecisionTests(String dataFileBase, String separator,
                                           int l, int k, double smoothRun, int topN) {
        runPrecisionTests(dataFileBase, "UB", separator, l, k, smoothRun, topN);
    }

    /**
     * Runs User-based LSH precision against k parameter. */
    public static void runUBLSHPrecisionTest(String dataFileBase, String separator,
                          int l, int k, double smoothRun, int topN) {
        runPrecisionTests(dataFileBase, "UBLSH", separator, l, k, smoothRun, topN);
    }

    private static void runPrecisionTests(
            String dataFileBase, String type, String separator,
            int l, int k, double smoothRun, int topN)
    {
        LOG.info("Running precision simulation...");
        double totalPrecision;
        HashMap<Integer, HashMap<String, Set<String>>> hashTables;
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        ArrayList<Double> precisionList = new ArrayList<>();
        int kNN = 1;
        for (int i = 0; i < 10; i++) {
            totalPrecision = 0.0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForRecommendation(dataFileBase, (j+1), separator);
                Set<String> itemSet = itemRateMap.keySet();
                Set<String> userSet = userRateMap.keySet();
                double precision;
                if (type == "UB") {
                    precision = UBPrecision.calculateUBPrecision(
                            userRateMap, testDataMap, itemSet, kNN, topN);
                } /*else if (type == "IB") {
                    precision = IBPrecision.calculateItemBasedPrecision(
                            userRateMap, itemRateMap, testDataMap, itemSet, kNN, topN);
                }*/ else if (type == "UBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, l, k, itemSet);
                    hashTables = LSH.buildIndexTables(userRateMap, vmap, l);
                    precision = UBLSHPrecision.calculateUBLSHPrecision(
                            userRateMap, testDataMap, hashTables, vmap, itemSet, kNN, topN);
                }/*else if (type == "IBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, l, k, userSet);
                    hashTables = LSH.buildIndexTables(itemRateMap, vmap, l);
                    precision = IBLSHPrecision.calculateIBLSHPrecision(
                            userRateMap,itemRateMap,testDataMap,hashTables,vmap,itemSet,kNN,topN);
                }*/
                else {
                    throw new UnsupportedOperationException("Invalid operation for CF type.");
                }
                totalPrecision += precision;
            }
            LOG.info(type + "Precision = " + totalPrecision / smoothRun);
            precisionList.add(totalPrecision / smoothRun);

            kNN+=3;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Precision");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(type + "PrecisionList = " + precisionList.toString());

    }


    /**
     * Runs 2D tests for k parameter and number of HashFunctions.*/
    public static void run2DPrecisionHashFunctionsAndKTests(
            String testType, String dataFileBase,
            int numOfRun, double smoothRun, String separator, int topN)
    {
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
                    preprocessDataForRecommendation(dataFileBase, (s+1), separator);
                    Set<String> itemSet = itemRateMap.keySet();
                    Set<String> userSet = userRateMap.keySet();
                    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                    if (testType == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildIndexTables(userRateMap, vmap, numOfBands);
                        precision += UBLSHPrecision.calculateUBLSHPrecision(
                                userRateMap, testDataMap, hashTables, vmap, itemSet, kNN, topN);
                        candidate_set_size += UBLSHPrecision.getCandidate_size();

                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                }
                LOG.info("numOfBands = " + numOfBands
                        + " numOfHashFunctions = " + numOfHashFunctions);
                LOG.info("k = " + kNN);
                LOG.info(testType + "precision = " + precision /smoothRun);
                precisionList.add(precision / smoothRun);
                candidate_set_list.add(candidate_set_size /smoothRun);
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
     * Runs 2D tests for k parameter and number of HashTables.*/
    public static void run2DPrecisionHashTablesAndKTests(
            String testType, String dataFileBase,
            int numOfRun, int smoothRun, String separator, int topN)
    {
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
                    preprocessDataForRecommendation(dataFileBase, (s+1), separator);
                    Set<String> itemSet = itemRateMap.keySet();
                    Set<String> userSet = userRateMap.keySet();
                    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                    if (testType == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildIndexTables(userRateMap, vmap, numOfBands);
                        precision += UBLSHPrecision.calculateUBLSHPrecision(
                                userRateMap, testDataMap, hashTables, vmap, itemSet, kNN, topN);
                        candidate_set_size += UBLSHPrecision.getCandidate_size();

                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                }
                LOG.info("numOfBands = " + numOfBands
                        + " numOfHashFunctions = " + numOfHashFunctions);
                LOG.info("k = " + kNN);
                LOG.info(testType + "precision = " + precision /smoothRun);
                precisionList.add(precision / smoothRun);
                candidate_set_list.add(candidate_set_size /smoothRun);
                numOfBands += 1;
            }
            precisionList2D.add(precisionList);
            candidate_set_list2D.add(candidate_set_list);

            kNN += 2;
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


}
