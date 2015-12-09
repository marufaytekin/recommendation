package com.zaana.ml.prediction;

import clustering.Cluster;
import com.zaana.ml.Clusters;
import com.zaana.ml.Common;
import com.zaana.ml.similarity.Cosine;

import java.util.*;

/**
 * Created by maruf on 28/05/15.
 */
public class ClusterPrediction extends AbstractPrediction {

    /**
     * Runs LSH prediction on test data */
    public static long runClusterPredictionOnTestData(
            Cluster clusters,
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap, int depth, int kNN, int y) {

        final long startTime = System.currentTimeMillis();
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        double totalNbrSize = 0;
        Iterator<Map.Entry<String, HashMap<String, Integer>>> testDataIter = testDataMap
                .entrySet().iterator();
        while (testDataIter.hasNext()) {
            Map.Entry<String, HashMap<String, Integer>> testDataEntry = testDataIter
                    .next();
            String userId = testDataEntry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            List<String> neighborsList = Clusters.getClusterNeighborsWithName(clusters,userId,depth);
            Set <String> neighborsSet = new HashSet<>(neighborsList);
            //Using clustered results
              predictRatingsForTestUsers(
                      testDataEntry, userRateMap, itemRateMap, neighborsSet, outputList, targetList);
            // Using clustered results with k-NN
            //predictRatingsForTestUsers(
            //       testDataEntry, userRateMap, itemRateMap, neighborsSet, userId, outputList, targetList, kNN, y);
            totalNbrSize += neighborsList.size();
        }

        avg_candidate_set_size = totalNbrSize / testDataMap.size();
        final long endTime = System.currentTimeMillis();
        LOG.info("Cluster Running time: " + (endTime - startTime) + " ms.");

        return (endTime - startTime);

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
                    } else {
                        prediction = 0;
                    }
                    outputList.add(prediction);
                    targetList.add(givenRating);
                }
            } catch (NullPointerException e) {
                // do nothing
            }
        }
    }

    private static void predictRatingsForTestUsers(
            Map.Entry<String, HashMap<String, Integer>> testDataEntry,
            final HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            Set<String> candidateSet, LinkedList<Double> outputList,
            LinkedList<Integer> targetList)
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
                if (!intersectionOfCandidateRatedUserSets.isEmpty()) {
                    prediction = calculateClusterBasedPredicitonRate(
                            userRateMap, intersectionOfCandidateRatedUserSets, movieId);
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


    /**
     * Calculates Cluster  prediction */
    private static double calculateClusterBasedPredicitonRate(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            Set<String> intersectionOfCandidateRatedUserSets,
            String movieId) {
        Iterator<String> entry = intersectionOfCandidateRatedUserSets.iterator();
        double weightedRatingsTotal = 0;
        while (entry.hasNext()) {
            String candidateUser = entry.next();
            Integer rating = userRateMap.get(candidateUser).get(movieId);
            weightedRatingsTotal += rating;
        }
        if (intersectionOfCandidateRatedUserSets.size() != 0)
            return weightedRatingsTotal / intersectionOfCandidateRatedUserSets.size();
        else
            return 0;
    }



}
