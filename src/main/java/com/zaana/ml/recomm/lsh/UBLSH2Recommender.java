package com.zaana.ml.recomm.lsh;

import com.zaana.ml.LSH;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maytekin on 28.01.2016.
 */
public class UBLSH2Recommender extends AbstractLSHRecommender {

    @Override
    public void buildModel(HashMap<String, HashMap<String, Integer>> userRateMap,
                           HashMap<String, HashMap<String, Integer>> itemRateMap,
                           int numOfBands, int numOfHashFunctions) {
        Set<String> itemSet = itemRateMap.keySet();
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap =
                Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
        hashTables = LSH.buildModel(userRateMap, vmap, numOfBands);
        hashKeyLookupTable = LSH.getHashKeyLookupTable();
    }

    @Override
    public Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            String userId, int topN)
    {
        Set<String> userRatingList = userRateMap.get(userId).keySet();
        Set<String> userCandidateSet =
                LSH.getCandidateSetFromHashTables(hashTables, userId, hashKeyLookupTable);
        Set<String> neighborsRatingList;
        Set<String> uniqueueItemsSet = new HashSet<>();
        List<String> ratedItemList = new ArrayList<>();
        for (String neighborId : userCandidateSet) {
            neighborsRatingList = userRateMap.get(neighborId).keySet();
            neighborsRatingList.removeAll(userRatingList);
            ratedItemList.addAll(neighborsRatingList);
            uniqueueItemsSet.addAll(neighborsRatingList);
        }
        candidateItemListSize = ratedItemList.size();
        uniqueCandidateItemListSize = uniqueueItemsSet.size();
        Set<String> recSet = new HashSet<>();
        int size = ratedItemList.size();
        int idx;
        for (int i = ratedItemList.size(); i > 0 && recSet.size() < topN; i--) {
            idx = (int) Math.floor(Math.random()*size);
            recSet.add(ratedItemList.get(idx));
        }
        return recSet;
    }

    @Override
    public Double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            String targetUserId,
            String movieId)
    {
        HashMap<String, Integer> itemRatings = itemRateMap.get(movieId);
        if (itemRatings == null) return null;
        List<String> candidateSetList =
                LSH.getCandidateListFromHashTables(hashTables, targetUserId, hashKeyLookupTable);
        Set<String> candidateSet = new HashSet<>(candidateSetList);
        Set<String> ratedUserSet = itemRatings.keySet();
        Set<String> intersectionOfCandidateRatedUserSets = new HashSet<>(ratedUserSet);
        intersectionOfCandidateRatedUserSets.retainAll(candidateSet);
        if (intersectionOfCandidateRatedUserSets.isEmpty()) return null;
        double weightedRatingsTotal = 0.0;
        Integer rating;
        for (String candidateUser : intersectionOfCandidateRatedUserSets) {
            rating = userRateMap.get(candidateUser).get(movieId);
            weightedRatingsTotal += rating;
        }
        candidateItemListSize = candidateSetList.size();
        uniqueCandidateItemListSize = candidateSet.size();
        return weightedRatingsTotal/intersectionOfCandidateRatedUserSets.size();
    }

}