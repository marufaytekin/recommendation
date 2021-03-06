package com.zaana.ml.prediction;

import com.zaana.ml.similarity.Cosine;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.*;
import java.util.Map.Entry;

public final class IBKNNPrediction extends AbstractPredictionTests {

    private IBKNNPrediction() {
    }

    /**
     * This method uses k-NN algorithm to predict user/item rating.
     * 
     * Algorithm to predict user u's rating for item i:
     * 
     * for each item j that user u has rated compute the similarity s between i
     * and j add similarity in list l in <j, s> format sort similarity list
     * based on similarity s retrieve top N item, ranked by similarity s, that
     * user u rated for compute u's preference for i, by using user u's
     * preference for top k item, weighted by s
     * 
     * User u's prediction for item i:
     * 
     * Retrieve similar item list of item i as follows:
     * 
     * for item i for every other item j (m) 
     *     compute similarity s between i and j (n) 
     *     add similarity s to similarity list l in <j,s> format (c) 
     *     sort similarity list l based on similarity s ( m*log(m) ) 
     * return the top k items that user u rated for, ranked by similarity 
     * compute u's preference for i, by using top k item's rated by user u, weighted by s (k)
     * @param itemRateMap
     * @param userRateMap
     * @param testDataMap
     * @param kNN    @return
     * @param y           */
    public static double runItemBasedNNPredictionOnTestData(
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap,
            final int kNN, int y) {

        LOG.info("Running prediction tests on test data set...");
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        int testDataSize = testDataMap.size();
        LOG.info("testDataMap size :" + testDataSize);
        final long startTime = System.currentTimeMillis();
        for (Entry<String, HashObjObjMap<String, Integer>> testDataEntry : testDataMap
                .entrySet()) {
            String testUserId = testDataEntry.getKey();
            HashObjObjMap<String, Integer> userRateList = userRateMap.get(testUserId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            Set<String> ratedItemsSet = userRateMap.get(testUserId).keySet();
            predictRatingsForTestUsers(testDataEntry, itemRateMap, ratedItemsSet, testUserId, outputList, targetList, kNN, y);
        }
        final long endTime = System.currentTimeMillis();
        final double runningTime = (double) (endTime - startTime)/outputList.size();

        LOG.info("ItemBasedNN Running time: " + runningTime);

        return runningTime;

    }


    public static void predictRatingsForTestUsers(
            Entry<String, HashObjObjMap<String, Integer>> testDataEntry,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            Set<String> ratedItemsSet,
            String testUserId,
            LinkedList<Double> outputList,
            LinkedList<Integer> targetList,
            int kNN, int y)
    {
        HashObjObjMap<String, Integer> testMovieList = testDataEntry.getValue();
        double prediction;
        for (Entry<String, Integer> entry : testMovieList.entrySet()) {
            try {
                String testMovieId = entry.getKey();
                LinkedHashMap<String, Double> kNNList = getSimilarItemsListRatedByUser(
                        itemRateMap, testMovieId, ratedItemsSet, kNN, y);
                //if (kNNList != null && !kNNList.isEmpty()) { //BUG: computing prediction with lt k NN
                prediction = Prediction.calculateItemBasedPredicitonRate(
                        itemRateMap, kNNList, testUserId);
                targetList.add(entry.getValue()); // add rating to targetList
                outputList.add(prediction);
                //}
            } catch (NullPointerException e) {
                //do nothing
            }
        }
    }

    /**
     * This method computes and returns the similar items list that testUserId
     * is rated.
     * @param itemRateMap
     * @param itemId
     * @param ratedItemsSet
     * @param kNN   @return
     * @param y        */
    public static LinkedHashMap<String, Double> getSimilarItemsListRatedByUser(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            String itemId, Set<String> ratedItemsSet, int kNN, int y)
    {
        LinkedHashMap<String, Double> sortedSimilarItemsListMap =
                Cosine.getSimilarityListWithCandidateSet(itemId, ratedItemsSet, itemRateMap, y);
        if (sortedSimilarItemsListMap == null) { return null; }
        try {
            Iterator<Entry<String, Double>> iter = sortedSimilarItemsListMap.entrySet().iterator();
            LinkedHashMap<String, Double> kNNList = new LinkedHashMap<>();
            // K:movieId, V: similarity
            int numNN = 0;
            while (numNN < kNN && iter.hasNext()) {
                Entry<String, Double> nearestNeighbor = iter.next();
                kNNList.put(nearestNeighbor.getKey(),
                        nearestNeighbor.getValue());
                numNN++;
            }
            if (kNNList.isEmpty()) { return null; }
            else return kNNList;
        } catch (NullPointerException e) { return null; }
    }
}
