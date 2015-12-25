package com.zaana.ml;


import com.zaana.ml.metrics.Precision;
import com.zaana.ml.recomm.AbstractRecommendation;
import com.zaana.ml.recomm.UBLSHRecommendation;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maruf on 25/04/15.
 */
public class UBLSHPrecision extends Precision {

    static Logger LOG = Logger.getLogger(UBLSHPrecision.class);
    static double candidate_size;

    public static double getCandidate_size () {
        return candidate_size;
    }

    public static double calculateUBLSHPrecision(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            final HashMap<Integer, HashMap<String, Set<String>>> hashTablesUB,
            final HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmapUB,
            Set<String> itemSet,
            int topN, int kNN, int y)
    {
        double totalPrecision = 0;
        double totalCandidateSetSize = 0;

        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            try {
                List<String> topNRecom =
                        UBLSHRecommendation.recommendItems(
                                userRateMap, hashTablesUB, vmapUB, itemSet, userId, topN, kNN, y);
                //int kNN, int topN, int y)
                totalCandidateSetSize += AbstractRecommendation.getCandidateSetSize();

                if (topNRecom == null) {
                    LOG.info("topNRecom : " + topNRecom);
                }
                Set<String> retrieved = new HashSet<>(topNRecom);
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += calculatePrecision(relevant, retrieved);
            } catch (NullPointerException e) {
                LOG.error(e.getMessage());
            }
        }
        int size = testDataMap.size();
        candidate_size = totalCandidateSetSize / size;

        return totalPrecision / size;
    }
}
