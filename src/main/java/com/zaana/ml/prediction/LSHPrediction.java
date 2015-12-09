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
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap)
    {

        final long startTime = System.currentTimeMillis();
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();

        Integer total_candidate_set_size = 0;

        Iterator<Map.Entry<String, HashMap<String, Integer>>> testDataIter = testDataMap
                .entrySet().iterator();
        int cnt = 0;
        while (testDataIter.hasNext()) {
            Map.Entry<String, HashMap<String, Integer>> testDataEntry = testDataIter
                    .next();
            String userId = testDataEntry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            cnt++;
            List<String> candidateSetList = LSH.getCandidateSetsWithFrequency(hashTables, vmap, userRateList);
            Set <String> candidateSet = new HashSet<>(candidateSetList);
            total_candidate_set_size += candidateSet.size();
            predictRatingsForTestUsers(
                    testDataEntry, userRateMap, itemRateMap, candidateSetList, candidateSet, outputList, targetList);
        }

        final long endTime = System.currentTimeMillis();

        avg_candidate_set_size = (double) total_candidate_set_size / cnt;
        LOG.info("LSH Running time: " + (endTime - startTime) + " ms.");
        LOG.info("Avg Candidate Set Size: " + avg_candidate_set_size);

        return (endTime - startTime);

    }

    private static void predictRatingsForTestUsers(
            Map.Entry<String, HashMap<String, Integer>> testDataEntry,
            final HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            List<String> candidateSetList,
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
                    prediction = Prediction.calculateLSHBasedPredicitonRate(
                                userRateMap, intersectionOfCandidateRatedUserSets, candidateSetList, movieId);
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


}
