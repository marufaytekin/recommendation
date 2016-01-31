package com.zaana.ml.recomm.cf;

import com.google.common.collect.MinMaxPriorityQueue;
import com.zaana.ml.Common;

import java.util.*;

/**
 * Created by maytekin on 25.01.2016.
 */
public class IBCFRecommender extends AbstractCFRecommender {

    @Override
    public void buildModel(HashMap<String, HashMap<String, Integer>> userRateMap,
                           HashMap<String, HashMap<String, Integer>> itemRateMap,
                           int y, int k) {
        buildSimilarityMatrix(itemRateMap, y, k);
    }

    /**
     * This recommender implements Karypis' item-based top-N recommendation
     * algorithm described Item-Based Top-N Recommendation Algorithms paper:
     * http://glaros.dtc.umn.edu/gkhome/node/127
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
        Set<String> ratedItemSet = userRateMap.get(userId).keySet();
        // add candidate set to rated item list
        HashMap<String, Double> simList = new HashMap<>();
        for (String itemId : ratedItemSet) { //U
            MinMaxPriorityQueue<Map.Entry<String, Double>> neighbors = model.get(itemId);
            for (Map.Entry<String, Double> entry : neighbors) {
                String neighborId = entry.getKey();
                double simValue = entry.getValue();
                if (!ratedItemSet.contains(neighborId)) {
                    if (simList.containsKey(neighborId)) {
                        simList.put(neighborId, simList.get(neighborId) + simValue);
                    } else {
                        simList.put(neighborId, simValue);
                    }
                }
            }
        }

        return Common.sortByValueAndGetTopN(simList, topN);
    }


}
