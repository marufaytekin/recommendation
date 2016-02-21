package com.zaana.ml.prediction;

import com.zaana.ml.LSH;
import com.zaana.ml.LSH2;
import com.zaana.ml.recomm.lsh.AbstractLSHRecommender;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.*;

/**
 * Created by maruf on 19/05/15.
 */
public class LSHPredictionTests extends AbstractPredictionTests {

    /**
     * Runs LSH prediction on test data */
    public static double runUBLSHPredictionOnTestData(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            AbstractLSHRecommender lshRecommender)
    {
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        Integer total_candidate_set_size = 0;
        int cnt = 0;
        long totalTime = 0;
        long startTime;
        long endTime;
        HashObjObjMap<Object, Object> hashTables = lshRecommender.getHashTables();
        HashObjObjMap<Object, Object> hashKeyLookupTable = lshRecommender.getHashKeyLookupTable();
        for (Map.Entry<String, HashMap<String, Integer>> testDataEntry : testDataMap.entrySet()) {
            String userId = testDataEntry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            Double prediction;
            HashMap <String, Integer> userRatings = testDataEntry.getValue();
            String targetUserId = testDataEntry.getKey();
            for (Map.Entry<String, Integer> entry : userRatings.entrySet()) {
                try {
                    String movieId = entry.getKey();
                    Integer givenRating = entry.getValue();
                    HashMap<String, Integer> itemRatings = itemRateMap.get(movieId);
                    if (itemRatings == null) continue;
                    List<String> candidateSetList =
                            LSH2.getCandidateListFromHashTables(hashTables, targetUserId, hashKeyLookupTable);
                    Set<String> candidateSet = new HashSet<>(candidateSetList);

                    Set<String> ratedUserSet = itemRatings.keySet();

                    Set<String> intersectionOfCandidateRatedUserSets = new HashSet<>(candidateSet);
                    intersectionOfCandidateRatedUserSets.retainAll(ratedUserSet);
                    if (intersectionOfCandidateRatedUserSets.isEmpty()) continue;
                    Collections.shuffle(candidateSetList);
                    startTime = System.currentTimeMillis();
                    prediction = lshRecommender.calculatePrediction(userRateMap, itemRateMap, targetUserId, movieId,
                            intersectionOfCandidateRatedUserSets, candidateSetList);
                    endTime = System.currentTimeMillis();
                    totalTime += endTime - startTime;
                    total_candidate_set_size += candidateSet.size();
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
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            AbstractLSHRecommender lshRecommender)
    {
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        Integer total_candidate_set_size = 0;
        int cnt = 0;
        long totalTime = 0;
        long startTime;
        long endTime;
        HashObjObjMap<Object, Object> hashTables = lshRecommender.getHashTables();
        HashObjObjMap<Object, Object> hashKeyLookupTable = lshRecommender.getHashKeyLookupTable();
        for (Map.Entry<String, HashMap<String, Integer>> testDataEntry : testDataMap.entrySet()) {
            String userId = testDataEntry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            Double prediction;
            HashMap <String, Integer> userRatings = testDataEntry.getValue();
            String targetUserId = testDataEntry.getKey();
            for (Map.Entry<String, Integer> entry : userRatings.entrySet()) {
                try {
                    String movieId = entry.getKey();
                    Integer givenRating = entry.getValue();
                    Set<String> ratedItemsSet = userRateMap.get(targetUserId).keySet();
                    List <String> candidateSetList =
                            LSH2.getCandidateListFromHashTables(hashTables, movieId, hashKeyLookupTable);
                    Set<String> intersecItemsCandidateSet = new HashSet<>(candidateSetList);
                    intersecItemsCandidateSet.retainAll(ratedItemsSet);
                    if (intersecItemsCandidateSet.isEmpty()) continue;
                    startTime = System.currentTimeMillis();
                    prediction = lshRecommender.calculatePrediction(
                            userRateMap, itemRateMap, targetUserId, movieId, intersecItemsCandidateSet, candidateSetList);
                    endTime = System.currentTimeMillis();
                    totalTime += endTime - startTime;
                    Set<String> candidateSet = new HashSet<>(candidateSetList);
                    total_candidate_set_size += candidateSet.size();
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
