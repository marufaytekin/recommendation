package com.zaana.ml.recomm;

import com.zaana.ml.Common;
import com.zaana.ml.prediction.Prediction;
import com.zaana.ml.similarity.Cosine;

import java.util.*;

public final class UBRecommendation extends AbstractRecommendation
{

    public static List<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            Set<String> itemSet, String targetUserId, Set<String> candidateUserSet,
            final int kNN, final int topN)
    {

        try {
            Set<String> nonRatedItemSet = Common.getUserNonRatedItemList(userRateMap,
                    itemSet, targetUserId);
            LinkedHashMap<String, Double> targetUserSimilarityList =
                    Cosine.getSimilarityListWithCandidateSet(targetUserId, candidateUserSet, userRateMap, 1);
            LinkedHashMap<String, Double> itemPredictionList = new LinkedHashMap<>();
            Iterator<String> iter = nonRatedItemSet.iterator();
            while (iter.hasNext()) {
                String movieId = iter.next();
                LinkedHashMap<String, Double> kNNList = Common.getkNNList(
                        targetUserSimilarityList, userRateMap, movieId, kNN);
                if (kNNList != null && !kNNList.isEmpty()) { //BUG: used to compute prediction with lt k NN.
                    double prediction = Prediction.calculateUserBasedPredicitonRate(userRateMap,
                            kNNList, movieId);
                    itemPredictionList.put(movieId, prediction);
                }
            }
            List<String> topNRecList = Common.getTopN(itemPredictionList, topN);
            return topNRecList;
        } catch (NullPointerException e) {
            throw e;
        }
    }

}
