package com.zaana.ml.prediction;

import com.zaana.ml.Common;
import com.zaana.ml.similarity.Cosine;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.*;
import java.util.Map.Entry;

public final class UBKNNPrediction extends AbstractPredictionTests
{

    private UBKNNPrediction()
    {
    }


    /**
     * This method uses k-NN algorithm to predict user/item rating.
     * 
     * k nearest neighbors
     * n users
     * m items
     * 
     * k-NN algorithm of predicting some user u's rating r for item i:
     *  
     * for every other user w   (n)
     *    compute similarity s between u and w (m)
     *    add similarity s to similarity list l in <w,s> format (c)
     * sort similarity list l based on similarity s ( n*log(n) ) "priority queue"
     * return the top k users, ranked by similarity, who rated for item i (n) 
     * compute u's preference for i, by using top k user's preference for item i weighted by s (k)
     *   
     *    O ( n*(m+c) + n*log(n) + n + k )
     *    O (n * m)
     *   
     * @param userRateMap
     * @param testDataMap
     * @param kNN
     * @param y
     * @return
     */
    public static double runUserBasedNNPredictionOnTestData(
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap,
            final int kNN, int y)
    {
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        LOG.info("Running UserBasedNN test...");
        Set<String> candidateUserSet = userRateMap.keySet();
        final long startTime = System.currentTimeMillis();
        for (Entry<String, HashObjObjMap<String, Integer>> testDataEntry : testDataMap.entrySet()) {
            String userId = testDataEntry.getKey();
            HashObjObjMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            LinkedHashMap<String, Double> similarityListMap =
                    Cosine.getSimilarityListWithCandidateSet(userId, candidateUserSet, userRateMap, y);
            predictRatingsForTestUsers
                    (testDataEntry, userRateMap, similarityListMap, outputList, targetList, kNN);
        }

        final long endTime = System.currentTimeMillis();
        double avgRunTime = (double)(endTime - startTime)/outputList.size();
        LOG.info( "UserBasedNN Running time: " + avgRunTime);

        return avgRunTime;

    }

    private static void predictRatingsForTestUsers(
            Entry<String, HashObjObjMap<String, Integer>> testDataEntry,
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            LinkedHashMap<String, Double> similarityListMap,
            LinkedList<Double> outputList,
            LinkedList<Integer> targetList,
            final int kNN)
    {
        HashObjObjMap<String, Integer> movieRatePair = testDataEntry.getValue();
        double prediction;
        for (Entry<String, Integer> entry : movieRatePair.entrySet()) {
            try {
                String movieId = entry.getKey();
                LinkedHashMap<String, Double> kNNList = Common.getkNNList(
                        similarityListMap, userRateMap, movieId, kNN);
                //if (kNNList != null && !kNNList.isEmpty() ) { //&& (kNNList.size()>= kNN)) { //BUG: used to compute prediction with lt k NN
                prediction = Prediction.calculateUserBasedPredicitonRate(
                            userRateMap, kNNList, movieId);
                outputList.add(prediction);
                targetList.add(entry.getValue());
                //}
            } catch (NullPointerException e) {
                // do nothing
            }
        }
    }


}
