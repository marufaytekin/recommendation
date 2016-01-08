package com.zaana.ml;


import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.AbstractRecommendation;
import com.zaana.ml.recomm.LSHRecommendation;

import java.util.*;

/**
 * Created by maruf on 25/04/15.
 */
public class LSHPrecisionRecall extends PrecisionRecall {

    public static void calculateLSHPrecision(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            final HashMap<Integer, HashMap<String, Set<String>>> hashTablesIB,
            final HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmapIB,
            HashMap<String, String> hashKeyLookupTable, int topN)
    {
        double totalPrecision = 0;
        double totalRecall = 0;

        long startTime ;
        long endTime;
        long totalTime = 0;

        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            startTime = System.currentTimeMillis();
            try {
                Set<String> retrieved =
                        LSHRecommendation.recommendFrequentItems(hashTablesIB, userRateMap.get(userId), hashKeyLookupTable, topN);
                //Set<String> retrieved =
                // LSHRecommendation.recommendItems(hashTablesIB, userRateMap.get(userId), hashKeyLookupTable, topN);
                //Set<String> retrieved = LSHRecommendation.recommendMostFrequentItems(hashTablesIB, userRateMap.get(userId), hashKeyLookupTable, topN);
                //if (retrieved == null) {
                //    LOG.info("topNRecom : " + null);
                //}
                //Set<String> retrieved = new HashSet<>(topNRecom);
                endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += Precision.calculatePrecision(relevant, retrieved);
                totalRecall += Recall.calculateRecall(relevant, retrieved);
            } catch (NullPointerException e) {
                LOG.error(e.getMessage());
            }

        }
        int size = testDataMap.size();
        precision = totalPrecision/size;
        recall = totalRecall/size;
        LOG.info("Total Time = " + totalTime + " ms");
        LOG.info("Size = " + size);
        LOG.info("Avg top-N Rec Time = " + (double) totalTime / size + " ms");

    }
}
