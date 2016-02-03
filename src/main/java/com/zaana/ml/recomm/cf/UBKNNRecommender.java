package com.zaana.ml.recomm.cf;

import com.google.common.collect.MinMaxPriorityQueue;
import com.zaana.ml.Common;

import java.util.*;

/**
 * Created by maytekin on 27.01.2016.
 */
public class UBKNNRecommender extends AbstractCFRecommender {

    @Override
    public void buildModel(HashMap<String, HashMap<String, Integer>> userRateMap,
                           HashMap<String, HashMap<String, Integer>> itemRateMap,
                           int y, int k) {
        buildSimilarityMatrix(userRateMap, y, k);
    }

    /**
     * This recommender implements Karypis' user-based top-N recommendation described
     * in Evaluation of Item-Based Top-N Recommendation Algorithms paper:
     * http://www.dtic.mil/dtic/tr/fulltext/u2/a439546.pdf
     *
     * @param userRateMap
     * @param userId
     * @param topN
     * @return
     */
    @Override
    public Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            String userId, int topN)
    {
        if (model == null) {
            return null;
        }
        MinMaxPriorityQueue<Map.Entry<String, Double>> neighbors = model.get(userId);
        List<String> ratedItemList = new ArrayList<>();
        Set <String> userRatingList = userRateMap.get(userId).keySet();
        Set <String> neighborsRatingList;
        String neighborId;
        for (Map.Entry<String, Double> entry : neighbors) {
            neighborId = entry.getKey();
            neighborsRatingList = userRateMap.get(neighborId).keySet();
            neighborsRatingList.removeAll(userRatingList);
            ratedItemList.addAll(neighborsRatingList);
        }

        return Common.getMostFrequentTopNElements(ratedItemList, topN);

    }

}
