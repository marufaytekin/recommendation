package com.zaana.ml;

import com.zaana.ml.metrics.Precision;
import com.zaana.ml.recomm.IBRecommendation;

import java.util.*;

/**
 * Created by maruf on 25/04/15.
 */
public class IBPrecision extends Precision {

    public static double calculateItemBasedPrecision(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            Set<String> itemSet,
            int topN, int kNN, int y) {

        double totalPrecision = 0;

        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            try {
                List<String> itemBasedTopNRecommendationList = IBRecommendation.recommendItems(userRateMap,
                        itemRateMap, itemSet, userId, kNN, topN, y);
                Set<String> retrieved = new HashSet<>(itemBasedTopNRecommendationList);
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
