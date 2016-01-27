package com.zaana.ml.recomm;

import com.zaana.ml.Common;
import com.zaana.ml.LSH;
import com.zaana.ml.prediction.IBNNPrediction;
import com.zaana.ml.prediction.Prediction;

import java.util.*;

/**
 * Created by maruf on 20/02/15.
 */
public final class IBLSHRecommender {

    private static int candidateSetSize;
    public  int getCandidateSetSize() {
        return candidateSetSize;
    }

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
     * @param hashKeyLookupTable
     * @param itemSet
     * @param userId
     * @param topN
     * @param kNN
     * @param y      @return topNRecommendationForUser
     * */
    @Deprecated
    public static Set<String> IBLSHRecommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            HashMap<String, String> hashKeyLookupTable,
            Set<String> itemSet,
            String userId, int topN, int kNN, int y)
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
            Set<String> candidateSet = LSH.getCandidateSetFromHashTables(hashTables, itemId, hashKeyLookupTable);
            int candidateSetSize = candidateSet.size();
            cnt++;
            total_candidate_set_size += candidateSetSize;
            candidateSet.retainAll(ratedItemsSet); //intersection
            LinkedHashMap<String, Double> kNNList =
                    IBNNPrediction.getSimilarItemsListRatedByUser(itemRateMap, itemId, candidateSet, kNN, y);
            if (kNNList != null && !kNNList.isEmpty()) { //BUG: used to calculate prediction for lt k NN
                Double prediction = Prediction.calculateItemBasedPredicitonRate(itemRateMap, kNNList,
                        userId);
                predictionList.put(itemId, prediction);
            }
        }
        Set<String> topNRecommendationForUser = Common.getTopN(predictionList, topN);

        candidateSetSize = total_candidate_set_size / cnt;

        return topNRecommendationForUser;
    }


}
