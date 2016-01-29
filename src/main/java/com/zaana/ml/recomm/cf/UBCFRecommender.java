package com.zaana.ml.recomm.cf;

import com.google.common.collect.MinMaxPriorityQueue;
import com.zaana.ml.Common;

import java.util.*;

/**
 * Created by maytekin on 27.01.2016.
 */
public class UBCFRecommender implements CFRecommender{
    /**
     * This recommender implements Karypis' user-based top-N recommendation described
     * in Evaluation of Item-Based Top-N Recommendation Algorithms paper:
     * http://www.dtic.mil/dtic/tr/fulltext/u2/a439546.pdf
     *
     * @param userRateMap
     * @param similarityMatrix
     * @param userId
     * @param topN
     * @return
     */
    @Override
    public Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, MinMaxPriorityQueue<Map.Entry<String, Double>>> similarityMatrix,
            String userId, int topN) {

        MinMaxPriorityQueue<Map.Entry<String, Double>> neighbors = similarityMatrix.get(userId);
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
        Queue<AbstractMap.SimpleEntry<String, Integer>> q =
                Common.buildFrequencyBasedPriorityQueue(ratedItemList);
        Set<String> recSet = new HashSet<>();
        HashMap.SimpleEntry<String, Integer> entry;
        for (int i=0; i < q.size() && recSet.size() < topN; i++)
            try {
                entry = q.remove();
                recSet.add(entry.getKey());
            } catch (NoSuchElementException ignored) {}

        return recSet;
    }
}
