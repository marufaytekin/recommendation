package com.zaana.ml.prediction;

import com.zaana.ml.lsh.LSH;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.*;

/**
 * Created by maruf on 06/02/15.
 */
public class IBKNNLSHPrediction extends AbstractPredictionTests
{
    public static double runItemBasedLSHPredictionOnTestData(
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap,
            final HashObjObjMap<Object, Object> hashTables,
            HashObjObjMap<Object, Object> hashKeyLookupTable,
            int kNN, int y) {

        LOG.info("Running runItemBasedLSHPredictionOnTestData...");
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        Integer total_candidate_set_size = 0;
        testQueryCnt = 0;
        int cnt = 0;
        final long startTime = System.currentTimeMillis();
        for (Map.Entry<String, HashObjObjMap<String, Integer>> testDataEntry : testDataMap
                .entrySet()) {
            String userId = testDataEntry.getKey();
            HashObjObjMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null) {
                continue;
            }
            cnt++;
            total_candidate_set_size += predictRatingsForTestEntry(
                    testDataEntry, userRateMap, itemRateMap, hashTables, hashKeyLookupTable, outputList, targetList, kNN, y);
        }

        final long endTime = System.currentTimeMillis();
        final double runningTime = (double) (endTime - startTime)/outputList.size();
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
     * @param hashKeyLookupTable
     * @param outputList
     * @param targetList
     * @param kNN
     * @param y
     */
    private  static Integer predictRatingsForTestEntry(
            Map.Entry<String, HashObjObjMap<String, Integer>> testDataEntry,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            HashObjObjMap<Object, Object> hashTables,
            HashObjObjMap<Object, Object> hashKeyLookupTable,
            LinkedList<Double> outputList,
            LinkedList<Integer> targetList,
            final int kNN, int y)
    {
        String testUserId = testDataEntry.getKey();
        HashObjObjMap<String, Integer> testMovieList = testDataEntry.getValue();
        Set<String> ratedItemsSet = new HashSet<>(userRateMap.get(testUserId).keySet());
        Integer total_candidate_set_size = 0;
        double prediction;
        for (Map.Entry<String, Integer> entry : testMovieList.entrySet()) {
            try {
                String testMovieId = entry.getKey();
                Set<String> candidateSet = LSH.getCandidateSetFromHashTables(hashTables, testMovieId, hashKeyLookupTable);
                total_candidate_set_size += candidateSet.size();
                Set<String> intersectionOfCandidateRatedItemSets = new HashSet<>(candidateSet);
                intersectionOfCandidateRatedItemSets.retainAll(ratedItemsSet);
                LinkedHashMap<String, Double> kRatedSimilarItemsList;
                testQueryCnt++;
                if (!intersectionOfCandidateRatedItemSets.isEmpty()) {
                    kRatedSimilarItemsList = IBKNNPrediction.getSimilarItemsListRatedByUser(
                            itemRateMap, testMovieId, intersectionOfCandidateRatedItemSets, kNN, y);
                    LOG.debug("kRatedSimilarItemsList :" + kRatedSimilarItemsList.toString());
                    //if (kRatedSimilarItemsList != null && !kRatedSimilarItemsList.isEmpty()) {
                        prediction = Prediction.calculateItemBasedPredicitonRate(itemRateMap,
                                kRatedSimilarItemsList, testUserId);
                        targetList.add(entry.getValue());
                        outputList.add(prediction);
                    //}
                } else {
                    //targetList.add(entry.getValue());
                    //outputList.add(2.5);
                }
            }catch (NullPointerException e) {
                //LOG.error(e.getLocalizedMessage());
            }
        }
        return total_candidate_set_size / testMovieList.size();
    }


}
