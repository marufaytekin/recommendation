package com.zaana.ml;

import com.zaana.ml.recomm.IBLSHRecommendation;

import java.util.*;

/**
 * Created by maruf on 25/04/15.
 */
public class IBLSHPrecision extends Precision {

    public static double calculateIBLSHPrecision(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            final HashMap<Integer, HashMap<String, Set<String>>> hashTablesIB,
            final HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmapIB,
            Set<String> itemSet,
            int topN, int kNN, int y) {

        double totalPrecision = 0;

        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap.entrySet()) {
            String userId = entry.getKey();
            try {
                List<String> userBasedTopNRecom = IBLSHRecommendation.IBLSHRecommendItems(
                        userRateMap, itemRateMap, hashTablesIB, vmapIB, itemSet, userId, topN, kNN, y);
                Set<String> retrieved = new HashSet<>(userBasedTopNRecom);
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += calculatePrecision(relevant, retrieved, topN);
            } catch (NullPointerException e) {
                continue;
            }
        }

        int size = testDataMap.size();

        return totalPrecision / size;
    }
}
