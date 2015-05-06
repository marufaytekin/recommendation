package com.zaana.ml;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

public final class UBNNPrediction extends AbstractPrediction
{

    private UBNNPrediction()
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
     * @param simType
     * @param kNN
     * @param y
     * @return
     */
    public static long runUserBasedNNPredictionOnTestData(
            final HashMap<String, HashMap<String, Integer>> userRateMap,
            final HashMap<String, HashMap<String, Integer>> testDataMap,
            final int simType, final int kNN, int y)
    {
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        LOG.info("Running UserBasedNN test...");
        final long startTime = System.currentTimeMillis();
        Iterator<Entry<String, HashMap<String, Integer>>> testDataIter =
                testDataMap.entrySet().iterator();
        Set<String> candidateUserSet = userRateMap.keySet();
        while (testDataIter.hasNext()) {
            Entry <String, HashMap<String, Integer>> testDataEntry = testDataIter.next();
            String userId = testDataEntry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            LinkedHashMap<String, Double> similarityListMap =
                    Similarity.getCosineSimilarityListWithCandidateSet(userId, candidateUserSet, userRateMap, y);
            predictRatingsForTestUsers(testDataEntry, userRateMap, similarityListMap, outputList, targetList, kNN);
        }

        final long endTime = System.currentTimeMillis();
        LOG.info( "UserBasedNN Running time: " );
        LOG.info( endTime - startTime );

        return (endTime - startTime);
    }

    private static void predictRatingsForTestUsers(
            Entry<String, HashMap<String, Integer>> testDataEntry,
            final HashMap<String, HashMap<String, Integer>> userRateMap,
            LinkedHashMap<String, Double> similarityListMap,
            LinkedList<Double> outputList,
            LinkedList<Integer> targetList,
            final int kNN)
    {

        HashMap <String, Integer> movieRatePair = testDataEntry.getValue();
        double prediction;
        for (Entry<String, Integer> entry : movieRatePair.entrySet()) {
            try {
                String movieId = entry.getKey();
                LinkedHashMap<String, Double> kNNList = Common.getkNNList(
                        similarityListMap, userRateMap, movieId, kNN);
                if (!kNNList.isEmpty() ) { //&& (kNNList.size()>= kNN)) { //BUG: used to compute prediction with lt k NN
                    prediction = Prediction.calculateUserBasedPredicitonRate(
                            userRateMap, kNNList, movieId);
                    outputList.add(prediction);
                    targetList.add(entry.getValue());
                }
            } catch (NullPointerException e) {
                // do nothing
            }
        }
    }

}
