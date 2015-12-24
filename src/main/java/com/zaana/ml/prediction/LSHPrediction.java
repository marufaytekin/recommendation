package com.zaana.ml.prediction;

import com.zaana.ml.LSH;
import java.util.*;

/**
 * Created by maruf on 19/05/15.
 */
public class LSHPrediction extends AbstractPrediction {

    /**
     * Runs LSH prediction on test data */
    public static long runLSHPredictionOnTestData(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            HashMap<String, String> hashKeyTable, int kNN)
    {

        final long startTime = System.currentTimeMillis();
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();

        Integer total_candidate_set_size = 0;
        int cnt = 0;
        for (Map.Entry<String, HashMap<String, Integer>> testDataEntry : testDataMap.entrySet()) {
            String userId = testDataEntry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            cnt++;
            //List<String> candidateSetList = LSH.getCandidateSetsWithFrequency(hashTables, vmap, userRateList);
            List<String> candidateSetList = LSH.getCandidateUserSetFromHashTable(hashTables, userId, hashKeyTable);
            Set<String> candidateSet = new HashSet<>(candidateSetList);
            total_candidate_set_size += candidateSet.size();
            predictRatingsForTestUsers(
                    testDataEntry, userRateMap, itemRateMap, candidateSetList, candidateSet, outputList, targetList, kNN);
        }

        final long endTime = System.currentTimeMillis();

        avg_candidate_set_size = (double) total_candidate_set_size / cnt;
        LOG.info("LSH Running time: " + (endTime - startTime) + " ms.");
        LOG.info("Avg Candidate Set Size: " + avg_candidate_set_size);

        return (endTime - startTime);

    }

    /**
     * This method runs prediction tests for LSH algorithm.
     *
     * For each hash table:
     *   compute a hash key for the target user
     *   retrieve the candidate users with the hash key from the hash table
     *   add candidate set to a running list, C.
     * Weight the candidate users with the number of occurrences in hash tables.
     * Find the users who rated for the target item in candidate list
     * Use the ratings of this users weighted with frequency to compute the prediction
     *  @param testDataEntry
     * @param userRateMap
     * @param itemRateMap
     * @param candidateSetList
     * @param candidateSet
     * @param outputList
     * @param targetList
     * @param kNN
     */
    private static void predictRatingsForTestUsers(
            Map.Entry<String, HashMap<String, Integer>> testDataEntry,
            final HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            List<String> candidateSetList,
            Set<String> candidateSet,
            LinkedList<Double> outputList,
            LinkedList<Integer> targetList, int kNN)
    {
        HashMap <String, Integer> movieRatePair = testDataEntry.getValue();
        double prediction;
        for (Map.Entry<String, Integer> entry : movieRatePair.entrySet()) {
            try {
                String movieId = entry.getKey();
                Integer givenRating = entry.getValue();
                Set<String> ratedUserSet = itemRateMap.get(movieId).keySet();
                Set<String> intersectionOfCandidateRatedUserSets = new HashSet<>(ratedUserSet);
                intersectionOfCandidateRatedUserSets.retainAll(candidateSet);
                //Set<String> frequentCandidateUsers = getFrequentCandidateList(
                //        intersectionOfCandidateRatedUserSets, candidateSetList, kNN);
                if (!intersectionOfCandidateRatedUserSets.isEmpty()) {
                    prediction = Prediction.calculateLSHFreqBasedPredictionRate(
                            userRateMap, intersectionOfCandidateRatedUserSets, candidateSetList, movieId);
                    //prediction = Prediction.calculateLSHBasedPredictionRate(
                    //        userRateMap, frequentCandidateUsers, movieId);
                    if (prediction != 0) {
                        outputList.add(prediction);
                        targetList.add(givenRating);
                    }
                }
            } catch (NullPointerException e) {
                // do nothing
            }
        }
    }


    private static Set<String> getFrequentCandidateList(
            Set<String> intersectionOfCandidateRatedUserSets, List<String> candidateSetList, int kNN) {
        Set<String> userSet = new HashSet<>();
        String userId;
        int size = candidateSetList.size();
        for (int i = candidateSetList.size(); i >= 0 && userSet.size() < kNN; i--) {
            int idx = (int) Math.floor(Math.random()*size);
            //System.out.println("idx:" + idx);
            userId = candidateSetList.get(idx);
            if (intersectionOfCandidateRatedUserSets.contains(userId))
                userSet.add(userId);
        }
        return userSet;
    }


}
