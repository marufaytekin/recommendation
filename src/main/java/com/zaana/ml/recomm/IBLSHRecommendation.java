package com.zaana.ml.recomm;

import com.zaana.ml.Common;
import com.zaana.ml.LSH;
import com.zaana.ml.prediction.IBNNPredictionTest;
import com.zaana.ml.prediction.Prediction;

import java.util.*;

/**
 * Created by maruf on 20/02/15.
 */
public final class IBLSHRecommendation extends LSHRecommendation
{

    /**
     * for each item i that user u has not rated yet
     *     retrieve similar items list l to item i
     *     compute predicted rating of item i
     *     add prediction to a running prediction list pl
     * return top N recommendation list from prediction list pl
     * @param userRateMap
     * @param itemRateMap
     * @param hashTables
     * @param vmap
     * @param itemSet
     * @param userId
     * @param topN
     * @param kNN
     * @param y
     * @return topNRecommendationForUser
     */
    public static List<String> IBLSHRecommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            Set<String> itemSet, String userId, int topN, int kNN, int y)
    {

        Set<String> nonRatedItemSet = Common.getUserNonRatedItemList(
                userRateMap, itemSet, userId);
        Iterator<String> iter = nonRatedItemSet.iterator();
        LinkedHashMap<String, Double> predictionList = new LinkedHashMap<>();
        Set<String> ratedItemsSet = userRateMap.get(userId).keySet();
        // add candidate set to rated item list
        int total_candidate_set_size = 0;
        int cnt = 0;
        while (iter.hasNext()) {
            String itemId = iter.next();
            HashMap<String, Integer> itemRateList = itemRateMap.get(itemId);
            if (itemRateList == null) continue;
            Set<String> candidateSet = LSH.getCandidateSet(hashTables, vmap, itemId, itemRateList);
            int candidateSetSize = candidateSet.size();
            cnt++;
            total_candidate_set_size += candidateSetSize;
            Set<String> intersectionOfCandidateRatedItemSets = new HashSet<>(candidateSet);
            intersectionOfCandidateRatedItemSets.retainAll(ratedItemsSet);
            LinkedHashMap<String, Double> kNNList = IBNNPredictionTest
                    .getSimilarItemsListRatedByUser(itemRateMap,
                            itemId, intersectionOfCandidateRatedItemSets, kNN, y);
            if (kNNList != null && !kNNList.isEmpty()) { //BUG: used to calculate prediction for lt k NN
                Double prediction = Prediction.calculateItemBasedPredicitonRate(itemRateMap, kNNList,
                        userId);
                predictionList.put(itemId, prediction);
            }
        }
        List<String> topNRecommendationForUser = Common.getTopN(
                predictionList, topN);

        candidateSetSize = total_candidate_set_size / cnt;
        return topNRecommendationForUser;
    }


}
