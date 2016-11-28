package com.zaana.ml.prediction;

import com.zaana.ml.lsh.LSH;
import com.zaana.ml.recomm.lsh.AbstractLSHRecommender;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import net.openhft.koloboke.collect.set.hash.HashObjSets;

import java.util.*;

/**
 * Created by maruf on 19/05/15.
 */
public class LSHPredictionTests extends AbstractPredictionTests {

    /**
     * Runs LSH prediction on test data */
    public static double runUBLSHPredictionOnTestData(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap,
            AbstractLSHRecommender lshRecommender)
    {
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        Integer total_candidate_set_size = 0;
        int cnt = 0;
        long totalTime = 0;
        long startTime;
        long endTime;
        testQueryCnt = 0;
        HashObjObjMap<Object, Object> hashTables = lshRecommender.getHashTables();
        HashObjObjMap<Object, Object> hashKeyLookupTable = lshRecommender.getHashKeyLookupTable();
        for (Map.Entry<String, HashObjObjMap<String, Integer>> testDataEntry : testDataMap.entrySet()) {
            String userId = testDataEntry.getKey();
            HashObjObjMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            Double prediction;
            HashObjObjMap<String, Integer> userRatings = testDataEntry.getValue();
            String targetUserId = testDataEntry.getKey();
            for (Map.Entry<String, Integer> entry : userRatings.entrySet()) {
                try {
                    String movieId = entry.getKey();
                    Integer givenRating = entry.getValue();
                    HashObjObjMap<String, Integer> itemRatings = itemRateMap.get(movieId);
                    if (itemRatings == null) continue;
                    testQueryCnt++;
                    Set<String> ratedUserSet = itemRatings.keySet();
                    List<String> candidateSetList =
                            LSH.getCandidateListFromHashTables(hashTables, targetUserId, hashKeyLookupTable);
                    HashObjSet<String> intersectionOfCandidateRatedUserSets = HashObjSets.getDefaultFactory().newMutableSet(candidateSetList);
                    intersectionOfCandidateRatedUserSets.retainAll(ratedUserSet);
                    //if (intersectionOfCandidateRatedUserSets.isEmpty()) continue;
                    //////////////////////////////////
                    startTime = System.currentTimeMillis();
                    prediction = lshRecommender.calculatePrediction
                            (userRateMap, itemRateMap, targetUserId, movieId, intersectionOfCandidateRatedUserSets, candidateSetList);
                    endTime = System.currentTimeMillis();
                    //////////////////////////////////
                    totalTime += endTime - startTime;
                    total_candidate_set_size += (new HashSet<>(candidateSetList)).size();
                    cnt++;
                    if (prediction != null) {
                        outputList.add(prediction);
                        targetList.add(givenRating);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        double avgTime = (double) totalTime/cnt;
        avg_candidate_set_size = (double) total_candidate_set_size/cnt;
        LOG.info("LSH Running time: " + avgTime + " ms.");
        LOG.info("Avg Candidate Set Size: " + avg_candidate_set_size);

        return avgTime;

    }


    /**
     * Runs LSH prediction on test data */
    public static double runIBLSHPredictionOnTestData(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap,
            AbstractLSHRecommender lshRecommender)
    {
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        testQueryCnt = 0;
        Integer total_candidate_set_size = 0;
        int cnt = 0;
        long totalTime = 0;
        long startTime;
        long endTime;
        HashObjObjMap<Object, Object> hashTables = lshRecommender.getHashTables();
        HashObjObjMap<Object, Object> hashKeyLookupTable = lshRecommender.getHashKeyLookupTable();
        for (Map.Entry<String, HashObjObjMap<String, Integer>> testDataEntry : testDataMap.entrySet()) {
            String userId = testDataEntry.getKey();
            HashObjObjMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            Double prediction;
            HashObjObjMap<String, Integer> userRatings = testDataEntry.getValue();
            String targetUserId = testDataEntry.getKey();
            for (Map.Entry<String, Integer> entry : userRatings.entrySet()) {
                try {
                    String movieId = entry.getKey();
                    Integer givenRating = entry.getValue();
                    Set<String> ratedItemsSet = userRateMap.get(targetUserId).keySet();
                    List <String> candidateSetList =
                            LSH.getCandidateListFromHashTables(hashTables, movieId, hashKeyLookupTable);
                    HashObjSet<String> intersecItemsCandidateSet =
                            HashObjSets.getDefaultFactory().newMutableSet(candidateSetList);
                    intersecItemsCandidateSet.retainAll(ratedItemsSet);
                    testQueryCnt ++;
                    //if (intersecItemsCandidateSet.isEmpty()) continue;
                    //////////////////////////////////
                    startTime = System.currentTimeMillis();
                    prediction = lshRecommender.calculatePrediction(
                            userRateMap, itemRateMap, targetUserId, movieId, intersecItemsCandidateSet, candidateSetList);
                    endTime = System.currentTimeMillis();
                    //////////////////////////////////
                    totalTime += (endTime - startTime);
                    total_candidate_set_size += (new HashSet<>(candidateSetList)).size();
                    cnt++;
                    if (prediction != null) {
                        outputList.add(prediction);
                        targetList.add(givenRating);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        double avgTime = (double) totalTime/cnt;
        avg_candidate_set_size = (double) total_candidate_set_size/cnt;
        LOG.info("LSH Running time: " + avgTime + " ms.");
        LOG.info("Avg Candidate Set Size: " + avg_candidate_set_size);

        return avgTime;

    }


}
