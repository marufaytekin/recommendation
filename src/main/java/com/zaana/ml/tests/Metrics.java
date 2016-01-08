package com.zaana.ml.tests;

import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Diversity;
import com.zaana.ml.metrics.Novelty;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.LSHRecommendation;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by maruf on 13/12/15.
 */
public class Metrics {

    static Logger LOG = Logger.getLogger(AbstractTest.class);
    private static double diversity;
    private static double novelty;
    //private static double serendipity;
    private static int aggregateDiversity;
    private static double precision;
    private static double recall;
    private static int topNSize;
    private static double candidateSetSize;

    public static void calculateLSHMetrics(
            final HashMap<String, HashMap<String, Integer>> userRateMap,
            final HashMap<String, HashMap<String, Integer>> itemRateMap,
            final HashMap<String, HashMap<String, Integer>> testDataMap,
            final HashMap<Integer, HashMap<String, Set<String>>> hashTablesIB,
            HashMap<String, String> itemHashKeyTable, final Set<String> userSet,
            int topN, int y) {

        double totalLSHDiversity = 0;
        double totalLSHNovelty = 0;
        double totalLSHSerendipity = 0;
        double totalPrecision = 0;
        double totalRecall = 0;
        double totalCandidateSetSize = 0;
        int numOfSerendipityCalc = 0;
        int totalTopN = 0;
        int cnt = 0;
        long startTime ;
        long endTime;
        long totalTime = 0;
        Set<String> uniqueItemSetLSH = new HashSet<>();
        HashMap<String, Integer> itemSetCount = getCounter(itemRateMap);
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
                    LSHRecommendation.recommendFrequentItems(hashTablesIB, userRateList, itemHashKeyTable, topN);
            //Set<String> topNRecommendedItems =
            //        LSHRecommendation.recommendItems(hashTablesIB, userRateList, hashKeyLookupTable, topN);
            endTime = System.currentTimeMillis();
            totalTime += (endTime - startTime);
            totalCandidateSetSize += LSHRecommendation.getCandidateSetSize();
            //compute unique items recommended to test users
            //uniqueItemSetLSH.addAll(topNRecommendedItems);
            // compute the diversity, novelty, and serendipity
            //double LSHNovelty = Novelty.novelty(topNRecommendedItems, userSet,
            //        itemSetCount);
            //double LSHDiversity = Diversity.intraListDissimilarity(
            //        topNRecommendedItems, itemRateMap, y);
            //Double LSHSerendipity = Serendipity.serendipity(userRateMap,
            //        itemRateMap, topNRecommendedItems,
            //        targetUserId);
            //totalLSHDiversity += LSHDiversity;
            //totalLSHNovelty += LSHNovelty;

            //if (LSHSerendipity != null) {
            //    totalLSHSerendipity += LSHSerendipity;
            //    numOfSerendipityCalc += 1;
            //}
            totalPrecision += Precision
                    .getPrecision(topNRecommendedItems, entry);
            totalRecall += Recall
                    .getRecall(topNRecommendedItems, entry);
            totalTopN += topNRecommendedItems.size();
            cnt++;
        }
        LOG.info("Avg Top-N Rec Time = " + (double) totalTime/cnt);
        //diversity = totalLSHDiversity / cnt;
        //novelty = totalLSHNovelty / cnt;
        //serendipity = totalLSHSerendipity / numOfSerendipityCalc;
        //aggregateDiversity = uniqueItemSetLSH.size();
        precision = totalPrecision / cnt;
        recall = totalRecall / cnt;
        candidateSetSize = totalCandidateSetSize / cnt;
        topNSize = totalTopN / cnt;
    }


    private static HashMap<String, Integer> getCounter(HashMap<String, HashMap<String, Integer>> rateMap)
    {
        HashMap<String, Integer> map = new HashMap<>();

        for (Map.Entry<String, HashMap<String, Integer>> entry  : rateMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue().size());
        }
        return map;
    }

    public static double getDiversity() {
        return diversity;
    }

    public static double getNovelty() {
        return novelty;
    }

    //public static double getSerendipity() {
    //    return serendipity;
    //}

    public static int getAggregateDiversity() {
        return aggregateDiversity;
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

    public static double getCandidateSetSize() {
        return candidateSetSize;
    }


}
