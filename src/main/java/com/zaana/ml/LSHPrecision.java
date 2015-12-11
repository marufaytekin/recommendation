package com.zaana.ml;


import com.zaana.ml.recomm.AbstractRecommendation;
import com.zaana.ml.recomm.LSHRecommendation;

import java.util.*;

/**
 * Created by maruf on 25/04/15.
 */
public class LSHPrecision extends AbstractRecommendation {

    public static double calculateLSHPrecision(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            final HashMap<Integer, HashMap<String, Set<String>>> hashTablesIB,
            final HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmapIB,
            HashMap<String, String> itemHashKeyTable, int topN)
    {
        double totalPrecision = 0;

        long startTime ;
        long endTime;
        long totalTime = 0;

        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            startTime = System.currentTimeMillis();
            try {
                Set<String> retrieved =
                        LSHRecommendation.recommendItems(itemRateMap, hashTablesIB,
                                vmapIB, userRateMap.get(userId), itemHashKeyTable, topN);
                //if (retrieved == null) {
                //    LOG.info("topNRecom : " + null);
                //}
                //Set<String> retrieved = new HashSet<>(topNRecom);
                endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += Precision.calculatePrecision(relevant, retrieved, topN);
            } catch (NullPointerException e) {
                LOG.error(e.getMessage());
            }

        }
        int size = testDataMap.size();
        LOG.info("Total Time = " + totalTime + " ms");
        LOG.info("Size = " + size);
        LOG.info("Avg top-N Rec Time = " + totalTime / size + " ms");
        return totalPrecision / size;
    }
}
