package com.zaana.ml.recomm;

import com.zaana.ml.Common;
import com.zaana.ml.prediction.IBNNPrediction;
import com.zaana.ml.prediction.Prediction;

import java.util.*;

public final class IBRecommendation {
    /**
     * for each item i that user u has not rated yet 
     *     retrieve similar items list l to item i 
     *     compute predicted rating of item i 
     *     add prediction to a running prediction list pl 
     * return top N recommendation list from prediction list pl
     * 
     * @param userRateMap
     * @param itemRateMap
     * @param itemSet
     * @param userId
     * @param kNN
     * @param topN
     * @param y
     * @return topNRecommendationForUser
     */
    @Deprecated
    public static Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            Set<String> itemSet, String userId, int kNN, int topN, int y) {

        Set<String> nonRatedItemSet = Common.getUserNonRatedItemList(
                userRateMap, itemSet, userId);
        Iterator<String> iter = nonRatedItemSet.iterator();

        LinkedHashMap<String, Double> predictionList = new LinkedHashMap<>();
        Set<String> ratedItemsSet = userRateMap.get(userId).keySet();
        // add candidate set to rated item list
        if (ratedItemsSet == null) return null;

        while (iter.hasNext()) {
            String itemId = iter.next();
            if (itemRateMap.get(itemId) == null) continue;
            LinkedHashMap<String, Double> kNNList = IBNNPrediction
                    .getSimilarItemsListRatedByUser(itemRateMap,
                            itemId, ratedItemsSet, kNN, y);
            if (kNNList != null && !kNNList.isEmpty()) { //BUG:
                Double prediction = Prediction.calculateItemBasedPredicitonRate(itemRateMap, kNNList,
                        userId);
                predictionList.put(itemId, prediction);
            }
        }
        Set<String> topNRecommendationForUser = Common.getTopN(
                predictionList, topN);

        return topNRecommendationForUser;

    }

}
