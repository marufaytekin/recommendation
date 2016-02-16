package com.zaana.ml.recomm.lsh;

import com.zaana.ml.*;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * This recommender implements Karypis' user-based top-N recommendation with LSH.
 * Evaluation of Item-Based Top-N Recommendation Algorithms paper:
 * http://www.dtic.mil/dtic/tr/fulltext/u2/a439546.pdf
 */
public class UBLSH1Recommender extends AbstractLSHRecommender {

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

        return Common.getMostFrequentTopNElements(ratedItemList, topN);

    }

    @Override
    public Double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            String targetUserId,
            String movieId) {

        HashMap<String, Integer> itemRatings = itemRateMap.get(movieId);
        if (itemRatings == null) return null;
        List<String> candidateSetList =
                LSH.getCandidateListFromHashTables(hashTables, targetUserId, hashKeyLookupTable);
        Set<String> candidateSet = new HashSet<>(candidateSetList);
        Set<String> ratedUserSet = itemRatings.keySet();
        Set<String> intersectionOfCandidateRatedUserSets = new HashSet<>(ratedUserSet);
        intersectionOfCandidateRatedUserSets.retainAll(candidateSet);
        if (intersectionOfCandidateRatedUserSets.isEmpty()) return null;
        double weightedRatingsTotal = 0;
        double weightsTotal = 0;
        Integer frequency;
        Integer rating;
        HashMap <String, Integer> frequencyMap = Common.getFrequencyMap(candidateSetList);
        for (String candidateUser : intersectionOfCandidateRatedUserSets) {
            frequency = frequencyMap.get(candidateUser);
            rating = userRateMap.get(candidateUser).get(movieId);
            weightedRatingsTotal += rating * frequency;
            weightsTotal += frequency;
        }
        candidateItemListSize = candidateSetList.size();
        uniqueCandidateItemListSize = candidateSet.size();
        if (weightsTotal != 0)
            return weightedRatingsTotal / weightsTotal;
        else
            return 0.0;
    }


}
