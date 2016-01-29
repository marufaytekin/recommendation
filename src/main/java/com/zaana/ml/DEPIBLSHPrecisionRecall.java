package com.zaana.ml;

import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.lsh.DEPIBLSHRecommender;

import java.util.*;

/**
 * Created by maruf on 25/04/15.
 */
public class DEPIBLSHPrecisionRecall extends DEPPrecisionRecall {



    public static void calculateIBLSHPrecisionRecall(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            final HashMap<Integer, HashMap<String, Set<String>>> hashTablesIB,
            final HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmapIB,
            HashMap<String, String> hashKeyLookupTable, Set<String> itemSet,
            int topN, int kNN, int y) {

        double totalPrecision = 0;
        double totalRecall = 0;

        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap.entrySet()) {
            String userId = entry.getKey();
            try {
                Set<String> retrieved = DEPIBLSHRecommender.IBLSHRecommendItems(
                        userRateMap, itemRateMap, hashTablesIB, vmapIB, hashKeyLookupTable, itemSet, userId, topN, kNN, y);
                //Set<String> retrieved = new HashSet<>(userBasedTopNRecom);
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += Precision.calculatePrecision(relevant, retrieved);
                totalRecall += Recall.calculateRecall(relevant, retrieved);
            } catch (NullPointerException e) {
            }
        }

        int size = testDataMap.size();
        precision = totalPrecision/size;
        recall = totalRecall/size;
    }
}
