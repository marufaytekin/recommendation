package com.zaana.ml.recomm.cf;

import com.google.common.collect.MinMaxPriorityQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by maytekin on 27.01.2016.
 */
public interface CFRecommender {

    Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, MinMaxPriorityQueue<Map.Entry<String, Double>>> itemSimilarityMatrix,
            String userId, int topN);

}
