package com.zaana.ml;


import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.UBLSHRecommender;

import java.util.*;

/**
 * Created by maruf on 25/04/15.
 */
public class UBLSHPrecisionRecall extends PrecisionRecall {

    public static void calculateUBLSHPrecisionRecall(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            final HashMap<Integer, HashMap<String, Set<String>>> hashTablesUB,
            final HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmapUB,
            HashMap<String, String> hashKeyLookupTable, Set<String> itemSet,
            int topN, int kNN, int y)
    {
        double totalPrecision = 0;
        double totalRecall = 0;
        double totalCandidateSetSize = 0;

        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            try {
                Set<String> retrieved =
                        UBLSHRecommender.recommendItems(
                                userRateMap, hashTablesUB, vmapUB, hashKeyLookupTable, itemSet, userId, topN, kNN, y);
                //int kNN, int topN, int y)
                totalCandidateSetSize += UBLSHRecommender.getCandidateSetSize();
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += Precision.calculatePrecision(relevant, retrieved);
                totalRecall += Recall.calculateRecall(relevant, retrieved);
            } catch (NullPointerException e) {
                LOG.error(e.getMessage());
            }
        }
        int size = testDataMap.size();
        candidate_size = totalCandidateSetSize / size;
        precision = totalPrecision/size;
        recall = totalRecall/size;
    }

}
