package com.zaana.ml.prediction;

import com.zaana.ml.Common;
import com.zaana.ml.LSH;
import com.zaana.ml.similarity.Cosine;

import java.util.*;

/**
 * Created by ${USER} on ${DATE}.
 */
public class UBKNNLSHPrediction extends AbstractPredictionTests {

    /**
     * This method uses LSH algorithm with k-NN to predict user/item rating.
     *
     * k nearest neighbors
     * n users
     * m items
     * b number of hash tables (bands)
     * h number of hash functions
     * p candidate list size
     *
     * LSH - k-NN algorithm of predicting some user u's rating r for item i:
     *
     * for each band(hashtable) in hash tables get candidate set as follows: (b)
     *     get hashkey from hashKeyLookupTable (1)
     *     retreive candidate set from hash table based on hashKey(1)
     * for each user w in candidate set (p)
     *    compute similarity s between u and w (m)
     *    add similarity s to similarity list l in <w,s> format (c)
     * sort similarity list l based on similarity s ( p*log(p) )
     * return the top k users, ranked by similarity, who rated for item i (p)
     * compute u's preference for i, by using top k user's preference for item i weighted by s (k)
     *
     *    O ( b * (h+c) + p* (m+c) + p*log(p) + p + k)
     *
     * @param userRateMap
     * @param itemRateMap
     * @param testDataMap
     * @param kNN
     * @param y
     * @param hashKeyLookupTable
     * @return
     * */
    public static double runUserBasedLSHPredictionOnTestData(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            int kNN, int y, HashMap<String, String> hashKeyLookupTable)
    {
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        Integer total_candidate_set_size = 0;
        int cnt = 0;
        final long startTime = System.currentTimeMillis();
        for (Map.Entry<String, HashMap<String, Integer>> testDataEntry : testDataMap
                .entrySet()) {
            String userId = testDataEntry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            cnt++;
            Set<String> candidateSet = LSH.getCandidateSetFromHashTables(hashTables, userId, hashKeyLookupTable);
            total_candidate_set_size += candidateSet.size();
            predictRatingsForTestUsers(
                    testDataEntry, userRateMap, itemRateMap, candidateSet, userId, outputList, targetList, kNN, y);
        }
        final long endTime = System.currentTimeMillis();
        double avgTime = (double)(endTime - startTime)/outputList.size();
        avg_candidate_set_size = (double) total_candidate_set_size / cnt;

        LOG.info("UB-LSH Running time: " + avgTime + " ms.");
        LOG.info("Avg Candidate Set Size: " + avg_candidate_set_size);
        return avgTime;

    }


    private static void predictRatingsForTestUsers(
            Map.Entry<String, HashMap<String, Integer>> testDataEntry,
            final HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            Set<String> candidateSet, String userId,
            LinkedList<Double> outputList,
            LinkedList<Integer> targetList,
            final int kNN, int y)
    {
        HashMap <String, Integer> movieRatePair = testDataEntry.getValue();
        double prediction;
        for (Map.Entry<String, Integer> entry : movieRatePair.entrySet()) {
            try {
                String movieId = entry.getKey();
                Integer givenRating = entry.getValue();
                LinkedHashMap<String, Double> similarityListMap;
                Set<String> ratedUserSet = itemRateMap.get(movieId).keySet();
                Set<String> intersectionOfCandidateRatedUserSets = new HashSet<>(ratedUserSet);
                intersectionOfCandidateRatedUserSets.retainAll(candidateSet);
                if (!intersectionOfCandidateRatedUserSets.isEmpty()) {
                    similarityListMap =
                            Cosine.getSimilarityListWithCandidateSet(userId,
                                    intersectionOfCandidateRatedUserSets, userRateMap, y);
                    LinkedHashMap<String, Double> kNNList = Common.getkNNList(
                            similarityListMap, userRateMap, movieId, kNN);
                    if (!kNNList.isEmpty()) { // && kNNList.size() >= kNN) { //BUG: calculating prediction with lt k NN
                        prediction = Prediction.calculateUserBasedPredicitonRate(
                                userRateMap, kNNList, movieId);
                        outputList.add(prediction);
                        targetList.add(givenRating);
                    }
                }
            } catch (NullPointerException e) {
                // do nothing
            }
        }
    }
}
