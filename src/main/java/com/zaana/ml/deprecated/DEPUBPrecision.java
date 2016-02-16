package com.zaana.ml.deprecated;

import com.zaana.ml.metrics.Precision;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maytekin on 20.01.2015.
 */
public class DEPUBPrecision extends Precision {

    static Logger LOG = Logger.getLogger(DEPUBPrecision.class);
    public DEPUBPrecision() {
    }

    @Deprecated
    public static double calculateUBPrecision(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            Set<String> itemSet,
            int topN, int kNN, int y)
    {
        double totalPrecision = 0;
        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            Set<String> candidateUserSet = userRateMap.keySet();
            try {
                Set<String> retrieved = DEPUBRecommender.recommendItems(
                        userRateMap, itemSet, userId, candidateUserSet, topN, kNN, y);
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += calculatePrecision(relevant, retrieved);
            } catch (NullPointerException e) {
                LOG.error(e.getLocalizedMessage());
            }
        }

        int size = testDataMap.size();

        return totalPrecision / size;
    }

}
