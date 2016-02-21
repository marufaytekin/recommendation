package com.zaana.ml.recomm.lsh;

import com.zaana.ml.*;
import com.zaana.ml.Vector;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;

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
        hashTables = LSH2.buildModel(itemRateMap, vmap, numOfBands);
        hashKeyLookupTable = LSH2.getHashKeyLookupTable();

    }

    @Override
    public List<String> getCandidateItemList(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashObjObjMap<Object, Object> userRateSet,
            String userId,
            HashObjSet<String> ratedItemSet) {
        //ratedItemSet = userRateSet.get(userId);
        List<String> candidateList = new ArrayList<>();
        for (String ratedItemId : ratedItemSet) {
            HashObjSet<String> candidateSet = LSH2.getCandidateItemSetForTopNRecommendation
                    (hashTables, ratedItemSet, ratedItemId, hashKeyLookupTable);
            candidateList.addAll(candidateSet);
        }
        //Collections.shuffle(candidateList);
        return candidateList;
    }

    /**
     * IBRecommender with LSH based on frequency of items in the buckets.
     * Instead of merged similarity value we use frequency of items in candidate list.
     * @param userId
     * @param candidateList
     * @param topN    @return      */
    @Override
    public HashObjSet<String> recommendItems(
            String userId, List<String> candidateList, int topN)
    {

        //candidateItemListSize = candidateList.size();
        //uniqueCandidateItemListSize = uniqueueItemsSet.size();

        return Common.getMostFrequentTopNElementSet(candidateList, topN);

    }

    @Override
    public Double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            String targetUserId,
            String itemId,
            Set <String> intersectItemsCandidateSet,
            List<String> candidateSetList) {

        int frequency;
        double rating;
        double weightedRatingsTotal = 0.0;
        int weightsTotal = 0;
        HashMap <String, Integer> frequencyMap =
                Common.getCandidateFrequentNElementsMap(candidateSetList, intersectItemsCandidateSet, 20);
        if (frequencyMap.isEmpty()) return null;
        for (String item : frequencyMap.keySet()) {
            frequency = frequencyMap.get(item);
            rating = userRateMap.get(targetUserId).get(item);
            weightedRatingsTotal += rating * frequency;
            weightsTotal += frequency;
        }

        return weightedRatingsTotal / weightsTotal;

    }

}
