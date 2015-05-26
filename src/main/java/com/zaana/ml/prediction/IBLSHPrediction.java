package com.zaana.ml.prediction;

import com.zaana.ml.LSH;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maruf on 06/02/15.
 */
public class IBLSHPrediction extends LSHPrediction
{
    public static long runItemBasedLSHPredictionOnTestData(
            final HashMap<String, HashMap<String, Integer>> itemRateMap,
            final HashMap<String, HashMap<String, Integer>> userRateMap,
            final HashMap<String, HashMap<String, Integer>> testDataMap,
            final HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            final HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            int kNN, int y) {

        LOG.info("Running runItemBasedLSHPredictionOnTestData...");
        final long startTime = System.currentTimeMillis();
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        Integer total_candidate_set_size = 0;
        int cnt = 0;
        Iterator<Map.Entry<String, HashMap<String, Integer>>> testDataIter = testDataMap
                .entrySet().iterator();
        while (testDataIter.hasNext()) {
            Map.Entry<String, HashMap<String, Integer>> testDataEntry = testDataIter
                    .next();
            String userId = testDataEntry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null) {
                continue;
            }
            total_candidate_set_size += predictRatingsForTestEntry(
                    testDataEntry, userRateMap, itemRateMap, hashTables, vmap, outputList, targetList, kNN, y);
            cnt++;

        }

        final long endTime = System.currentTimeMillis();
        final long runningTime = (endTime - startTime);
        avg_candidate_set_size = (double) total_candidate_set_size / cnt;
        LOG.info("ItemBasedLSH Running time: " + runningTime);
        LOG.info("Avg Candidate Set Size: " + avg_candidate_set_size);

        return runningTime;

    }

    /**
     * query user u's ratedItemsList
     * for each item i that user u rated:
     *  compute LSH candidate_set for item i
     *  compute intersection of candidate_set and ratedItemsList
     *  if ratedItemsList size > candidate_set size: // if rated items list size too large we take subset of potentially more similar ones.
     *                                               // eliminate heavy calculations
     *      compute similar items list on intersection
     *  else:  // if rated items list size not very large don't use intersection since That throws away very little useful information.
     *         // Use all rated items directly since they are not too much
     *      compute similar items list on ratedItemsList
     * @param testDataEntry
     * @param userRateMap
     * @param itemRateMap
     * @param hashTables
     * @param vmap
     * @param outputList
     * @param targetList
     * @param kNN
     * @param y
     */
    private  static int predictRatingsForTestEntry(
            Map.Entry<String, HashMap<String, Integer>> testDataEntry,
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            LinkedList<Double> outputList,
            LinkedList<Integer> targetList,
            final int kNN, int y)
    {
        String testUserId = testDataEntry.getKey();
        HashMap<String, Integer> testMovieList = testDataEntry.getValue();
        Set<String> ratedItemsSet = new HashSet<>(userRateMap.get(testUserId).keySet());
        Integer total_candidate_set_size = 0;
        int cnt = 0;
        for (Map.Entry<String, Integer> entry : testMovieList.entrySet()) {
            try {
                String testMovieId = entry.getKey();
                HashMap<String, Integer> movieRateList = itemRateMap.get(testMovieId);
                Set<String> candidateSet = LSH.getCandidateSet(hashTables, vmap, testMovieId, movieRateList);
                int candidateSetSize = candidateSet.size();
                total_candidate_set_size += candidateSetSize;
                cnt++;
                Set<String> intersectionOfCandidateRatedItemSets = new HashSet<>(candidateSet);
                intersectionOfCandidateRatedItemSets.retainAll(ratedItemsSet);
                LinkedHashMap<String, Double> kRatedSimilarItemsList;

                if (!intersectionOfCandidateRatedItemSets.isEmpty()) {
                    kRatedSimilarItemsList = IBNNPrediction.getSimilarItemsListRatedByUser(
                            itemRateMap, testMovieId, intersectionOfCandidateRatedItemSets, kNN, y);
                    LOG.debug("kRatedSimilarItemsList :" + kRatedSimilarItemsList.toString());
                    if (kRatedSimilarItemsList != null && !kRatedSimilarItemsList.isEmpty()) {
                        double prediction = Prediction.calculateItemBasedPredicitonRate(itemRateMap,
                                kRatedSimilarItemsList, testUserId);
                        targetList.add(entry.getValue());
                        outputList.add(prediction);
                    }
                }
            }catch (NullPointerException e) {
                LOG.error(e.getLocalizedMessage());
                return 0;
            }
        }
        return total_candidate_set_size / cnt;
    }


}