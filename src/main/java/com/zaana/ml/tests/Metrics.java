package com.zaana.ml.tests;

import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.lsh.AbstractLSHReccommender;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by maruf on 13/12/15.
 */
public class Metrics {

    static Logger LOG = Logger.getLogger(AbstractTest.class);
    private static double avgRecommTime;
    private static double precision;
    private static double recall;
    private static int topNSize;
    private static double candidateItemListSize;
    private static double uniqueItemListSize;

    public static void calculateLSHMetrics(
            final HashMap<String, HashMap<String, Integer>> userRateMap,
            final HashMap<String, HashMap<String, Integer>> testDataMap,
            AbstractLSHReccommender recommender, int topN)
    {
        double totalCandidateItemList = 0;
        double totalUniqueItemList = 0;
        double totalPrecision = 0;
        double totalRecall = 0;
        int totalTopN = 0;
        int cnt = 0;
        long startTime ;
        long endTime;
        long totalTime = 0;
        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            if (entry.getValue().size() < 5) continue;
            String targetUserId = entry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(targetUserId);
            if (userRateList == null || userRateList.size() < 5) {
                continue;
            }
            startTime = System.currentTimeMillis();
            Set<String> topNRecommendedItems =
                    recommender.recommendItems(userRateMap, targetUserId, topN);
            endTime = System.currentTimeMillis();
            totalTime += (endTime - startTime);
            totalPrecision += Precision
                    .getPrecision(topNRecommendedItems, entry);
            totalRecall += Recall
                    .getRecall(topNRecommendedItems, entry);
            totalTopN += topNRecommendedItems.size();
            totalCandidateItemList += recommender.getCandidateItemListSize();
            totalUniqueItemList += recommender.getUniqueCandidateItemListSize();
            cnt++;
        }
        LOG.info("Avg Top-N Rec Time to one user = " + (double) totalTime/cnt);

        precision = totalPrecision / cnt;
        recall = totalRecall/cnt;
        topNSize = totalTopN/cnt;
        avgRecommTime = (double) totalTime/cnt;
        candidateItemListSize = totalCandidateItemList/cnt;
        uniqueItemListSize = totalUniqueItemList/cnt;
    }

    public static double getAvgRecommTime() {
        return avgRecommTime;
    }

    public static double getPrecision() {
        return precision;
    }

    public static double getRecall() {
        return recall;
    }

    public static int getTopNSize() {
        return topNSize;
    }

    public static double getUniqueItemListSize() {
        return uniqueItemListSize;
    }

    public static double getCandidateItemListSize() {
        return candidateItemListSize;
    }

}
