package com.zaana.ml.recomm.lsh;

import com.zaana.ml.*;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maruf on 26/01/16.
 */
public class IBLSH1Recommender extends AbstractLSHRecommender {

    public IBLSH1Recommender() {
        super();
    }

    @Override
    public void buildModel(HashMap<String, HashMap<String, Integer>> userRateMap,
                           HashMap<String, HashMap<String, Integer>> itemRateMap,
                           int numOfBands, int numOfHashFunctions)
    {
        Set<String> userSet = userRateMap.keySet();
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap =
                Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
        LSH.buildModel(itemRateMap, vmap, numOfBands);
        hashTables = LSH.getHashTables();
        hashKeyLookupTable = LSH.getHashKeyLookupTable();
    }

    /**
     * IBRecommender with LSH based on frequency of items in the buckets.
     * Instead of merged similarity value we use frequency of items in candidate list.
     * @param userRateMap
     * @param userCandidateSet
     * @param userRatingList
     * @param userId
     * @param topN    @return       */
    @Override
    public Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            Set<String> userCandidateSet, Set<String> userRatingList, String userId, int topN)
    {
        HashMap<String, Integer> ratingsSet = userRateMap.get(userId);
        Set<String> ratedItemSet = userRateMap.get(userId).keySet();
        List<String> candidateList = new ArrayList<>();
        Set<String> uniqueueItemsSet = new HashSet<>();
        for (String ratedItemId : ratedItemSet) {
            Set<String> candidateSet = LSH.getCandidateItemSetFromHashTable
                    (hashTables, ratingsSet, ratedItemId, hashKeyLookupTable);
            candidateList.addAll(candidateSet);
            uniqueueItemsSet.addAll(candidateSet);
        }
        candidateItemListSize = candidateList.size();
        uniqueCandidateItemListSize = uniqueueItemsSet.size();

        return Common.getMostFrequentTopNElements(candidateList, topN);

    }

    @Override
    public Double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            String targetUserId,
            String itemId) {

        int frequency;
        double rating;
        double weightedRatingsTotal = 0.0;
        int weightsTotal = 0;
        Set<String> ratedItemsSet = userRateMap.get(targetUserId).keySet();
        List <String> candidateSetList =
                LSH.getCandidateListFromHashTables(hashTables, itemId, hashKeyLookupTable);
        Set<String> uniqueueCandidateSet = new HashSet<>(candidateSetList);
        ratedItemsSet.retainAll(uniqueueCandidateSet); //intersection with candidate set
        if (ratedItemsSet.isEmpty()) return null;
        HashMap <String, Integer> frequencyMap = Common.getFrequencyMap(candidateSetList);
        for (String item : ratedItemsSet) {
            frequency = frequencyMap.get(item);
            rating = userRateMap.get(targetUserId).get(item);
            weightedRatingsTotal += rating * frequency;
            weightsTotal += frequency;
        }
        candidateItemListSize = candidateSetList.size();
        uniqueCandidateItemListSize = uniqueueCandidateSet.size();
        if (weightsTotal != 0)
            return weightedRatingsTotal / weightsTotal;
        else
            return 0.0;
    }

}
