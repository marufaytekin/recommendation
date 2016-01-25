package com.zaana.ml;

import com.google.common.collect.MinMaxPriorityQueue;
import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.IBRecommendation;
import com.zaana.ml.recomm.IBRecommendation2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by maruf on 25/04/15.
 */
public class IBPrecisionRecall extends Precision {

    public static double getPrecision() {
        return precision;
    }

    public static double getRecall() {
        return recall;
    }

    static double precision;
    static double recall;

    public static void calculateItemBasedPrecisionRecall(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            HashMap<String, MinMaxPriorityQueue<Map.Entry<String, Double>>> itemSimilarityMatrix, int topN) {

        double totalPrecision = 0;
        double totalRecall = 0;

        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            try {
                Set<String> retrieved =
                        IBRecommendation2.recommendItems(userRateMap, itemSimilarityMatrix, userId, topN);
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += calculatePrecision(relevant, retrieved);
                totalRecall += Recall.calculateRecall(relevant, retrieved);
            } catch (NullPointerException e) {
                //LOG.error(e.getLocalizedMessage());
            }
        }

        int size = testDataMap.size();

        precision = totalPrecision / size;
        recall = totalRecall / size;

    }

}
