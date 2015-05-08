package com.zaana.ml;

import com.zaana.ml.recomm.UBRecommendation;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maytekin on 20.01.2015.
 */
public class UBPrecision extends Precision {

    public UBPrecision() {
    }

    public static double calculateUBPrecision(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            Set<String> itemSet,
            int kNN, int topN)
    {

        double totalPrecision = 0;

        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            Set<String> candidateUserSet = userRateMap.keySet();
            try {
                List<String> userBasedTopNRecom = UBRecommendation.recommendItems(
                        userRateMap, itemSet, userId, candidateUserSet, kNN, topN);
                Set<String> retrieved = new HashSet<>(userBasedTopNRecom);
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += calculatePrecision(relevant, retrieved, topN);
            } catch (NullPointerException e) {
                LOG.error(e.getLocalizedMessage());
            }
        }

        int size = testDataMap.size();

        return totalPrecision / size;
    }

}
