package com.zaana.ml.recomm.cf;

import com.google.common.collect.MinMaxPriorityQueue;
import com.zaana.ml.similarity.Cosine;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;

import java.io.*;
import java.util.*;

/**
 * Created by maytekin on 27.01.2016.
 */
public abstract class AbstractCFRecommender implements Serializable {

    HashMap<String, MinMaxPriorityQueue<Map.Entry<String, Double>>> model;

    public abstract List<String> recommendItems(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            String userId, int topN);

    public abstract void buildModel(final HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
                                    final HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
                                    int y, int k);

    /**
     * Builds similarity matrix from rating map and stores top k nearest
     * neighbors in a priority queue.
     *
     * @param ratingMap
     * @param y
     * @param k
     * @return model: similarity matrix.
     */
    protected void buildSimilarityMatrix(final HashObjObjMap<String, HashObjObjMap<String, Integer>> ratingMap,
                           int y, int k)
    {
        model = new HashMap<>();

        HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMapCopy = HashObjObjMaps.getDefaultFactory().newMutableMap(ratingMap);
        for (Map.Entry<String, HashObjObjMap<String, Integer>> userItemRatesPairA : ratingMap
                .entrySet()) {
            String objectIdA = userItemRatesPairA.getKey();
            HashObjObjMap<String, Integer> objectRatingsA = userItemRatesPairA
                    .getValue();
            Set<String> ratedItemIDSetA = objectRatingsA.keySet();
            for (Map.Entry<String, HashObjObjMap<String, Integer>> entryB : userRateMapCopy
                    .entrySet()) {
                String objectIdB = entryB.getKey();
                if (objectIdA.equals(objectIdB)) continue;
                HashObjObjMap<String, Integer> objectRatingsB = entryB.getValue();
                Set<String> ratedItemIDSetB = objectRatingsB.keySet();
                Set<String> intersectionAB = new HashSet<>(ratedItemIDSetA);
                intersectionAB.retainAll(ratedItemIDSetB);
                Double similarity;
                if (!intersectionAB.isEmpty()) {
                    similarity = Cosine.calculateCosineSimilarity(intersectionAB,
                            objectRatingsA, objectRatingsB, y);
                    if (model.containsKey(objectIdA)) {
                        model.get(objectIdA).offer(new HashMap.SimpleEntry<>(objectIdB, similarity));
                    } else {
                        MinMaxPriorityQueue<Map.Entry<String, Double>> q = MinMaxPriorityQueue
                                .orderedBy(new CustomComparator())
                                .maximumSize(k)
                                .create();
                        q.offer(new HashMap.SimpleEntry<>(objectIdB, similarity));
                        model.put(objectIdA, q);
                    }
                }
            }
        }
    }

    /**
     * Use following methods to create a similarity matrix in
     * HashMap format.
     */
    static class CustomComparator implements Comparator<Map.Entry <String, Double>>, Serializable {
        public int compare(Map.Entry <String, Double> o1, Map.Entry<String, Double> o2) {
            return Double.compare(o2.getValue(), o1.getValue());
        }
    }
}
