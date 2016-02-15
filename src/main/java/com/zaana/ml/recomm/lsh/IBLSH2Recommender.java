package com.zaana.ml.recomm.lsh;

import com.zaana.ml.*;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maruf on 09/05/15.
 */
public class IBLSH2Recommender extends AbstractLSHRecommender {

    public IBLSH2Recommender() {
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

    @Override
    public Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            Set<String> userCandidateSet, Set<String> userRatingList, String userId, int topN)
    {
        HashMap<String, Integer> ratingsSet = userRateMap.get(userId);
        //Set<String> userRatingSet = ratingsSet.keySet(); // use all items rated by user.
        Set<String> userRatingSet = Common.sortByValueAndGetTopNItems(ratingsSet, 20);// selects top n liked items
        //Set<String> uniqueueItemsSet = new HashSet<>();
        List<String> candidateList = new ArrayList<>();
        for (String testItemId : userRatingSet) {
            Set<String> candidateSet = LSH.getCandidateItemSetFromHashTable
                    (hashTables, ratingsSet, testItemId, hashKeyLookupTable);
            candidateList.addAll(candidateSet);
            //uniqueueItemsSet.addAll(candidateSet);
        }
        //candidateItemListSize = candidateList.size();
        //uniqueCandidateItemListSize = uniqueueItemsSet.size();
        Set<String> recSet = new HashSet<>();
        int size = candidateList.size();
        for (int i = candidateList.size(); i > 0 && recSet.size() < topN; i--) {
            int idx = (int) Math.floor(Math.random()*size);
            recSet.add(candidateList.get(idx));
        }

        return recSet;
    }

    @Override
    public Double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            String targetUserId, String itemId) {

        double rating;
        double weightedRatingsTotal = 0;
        Set<String> ratedItemsSet = userRateMap.get(targetUserId).keySet();
        List <String> candidateSetList =
                LSH.getCandidateListFromHashTables(hashTables, itemId, hashKeyLookupTable);
        Set <String> uniqueueCandidateSet = new HashSet<>(candidateSetList);
        ratedItemsSet.retainAll(uniqueueCandidateSet); //take intersection with candidate set
        if (ratedItemsSet.isEmpty()) return null;
        for (String item : ratedItemsSet) {
            rating = userRateMap.get(targetUserId).get(item);
            weightedRatingsTotal += rating;
        }
        candidateItemListSize = candidateSetList.size();
        uniqueCandidateItemListSize = uniqueueCandidateSet.size();
        return weightedRatingsTotal / ratedItemsSet.size();
    }
}
