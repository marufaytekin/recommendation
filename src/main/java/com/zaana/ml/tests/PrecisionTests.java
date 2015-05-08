package com.zaana.ml.tests;

import com.zaana.ml.UBPrecision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by maruf on 07/05/15.
 */
public class PrecisionTests extends AbstractTests {

    /**
     * Runs User-based precision against k parameter. */
    public static void runUBPrecisionTests(String dataFilePath, String separator,
                                           int l, int k, double smoothRun, int topN) {
        runPrecisionTests(dataFilePath,50, 5, "UB", separator, l, k, smoothRun, topN);
    }

    private static void runPrecisionTests(String dataFilePath, int numOfUserPercentage,
                                          int numOfRatingsForUsers, String type, String separator,
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
                preprocessDataForRecommendation(dataFilePath,separator,numOfUserPercentage,numOfRatingsForUsers);
                Set<String> itemSet = itemRateMap.keySet();
                Set<String> userSet = userRateMap.keySet();
                double precision;
                if (type == "UB") {
                    precision = UBPrecision.calculateUBPrecision(
                            userRateMap, testDataMap, itemSet, kNN, topN);
                } /*else if (type == "IB") {
                    precision = IBPrecision.calculateItemBasedPrecision(
                            userRateMap, itemRateMap, testDataMap, itemSet, kNN, topN);
                } else if (type == "UBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, l, k, itemSet);
                    hashTables = LSH.buildIndexTables(userRateMap, vmap, l);
                    precision = UBLSHPrecision.calculateUBLSHPrecision(
                            userRateMap,testDataMap,hashTables,vmap,itemSet,kNN,topN);
                }else if (type == "IBLSH") {
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
        LOG2.info("dataFileBase = " + dataFilePath);
        LOG2.info(type + "PrecisionList = " + precisionList.toString());

    }
}
