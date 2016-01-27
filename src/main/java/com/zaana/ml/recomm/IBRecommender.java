package com.zaana.ml.recomm;

import com.google.common.collect.MinMaxPriorityQueue;
import com.zaana.ml.Common;
import com.zaana.ml.prediction.IBNNPrediction;
import com.zaana.ml.prediction.Prediction;
import com.zaana.ml.similarity.Cosine;

import java.io.Serializable;
import java.util.*;

/**
 * Created by maytekin on 25.01.2016.
 */
public class IBRecommender implements CFRecommender{

    /**
     * This recommender implements Karypis' item-based top-N recommendation
     * algorithm: http://glaros.dtc.umn.edu/gkhome/node/127
     *
     * @param userRateMap
     * @param itemSimilarityMatrix
     * @param userId
     * @param topN
     * @return
     */
    public Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, MinMaxPriorityQueue<Map.Entry<String, Double>>> itemSimilarityMatrix,
            String userId, int topN) {

        Set<String> ratedItemSet = userRateMap.get(userId).keySet();
        // add candidate set to rated item list
        HashMap<String, Double> simList = new HashMap<>();
        for (String itemId : ratedItemSet) { //U
            MinMaxPriorityQueue<Map.Entry<String, Double>> neighbors = itemSimilarityMatrix.get(itemId);
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

        return Common.getTopNSet(simList, topN);
    }


}
