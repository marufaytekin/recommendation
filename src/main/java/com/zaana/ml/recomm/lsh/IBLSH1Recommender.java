package com.zaana.ml.recomm.lsh;

import com.zaana.ml.lsh.LSH;
import com.zaana.ml.tools.Vector;
import com.zaana.ml.tools.Common;
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
    public void buildModel(HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
                           HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
                           int numOfBands, int numOfHashFunctions)
    {
        Set<String> userSet = userRateMap.keySet();
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap =
                Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
        hashTables = LSH.buildModel(itemRateMap, vmap, numOfBands);
        hashKeyLookupTable = LSH.getHashKeyLookupTable();

    }

    @Override
    public List<String> getCandidateItemList(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<Object, Object> userRateSet,
            String userId,
            HashObjSet<String> ratedItemSet) {
        //ratedItemSet = userRateSet.get(userId);
        List<String> candidateList = new ArrayList<>();
        for (String ratedItemId : ratedItemSet) {
            HashObjSet<String> candidateSet = LSH.getCandidateItemSetForTopNRecommendation
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
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            String targetUserId,
            String itemId,
            HashObjSet<String> intersectItemsCandidateSet,
            List<String> candidateSetList) {

        int frequency;
        double rating;
        double weightedRatingsTotal = 0.0;
        int weightsTotal = 0;
        HashMap <String, Integer> frequencyMap =
                Common.getCandidateFrequentNElementsMap(candidateSetList, intersectItemsCandidateSet, 20);
        if (frequencyMap.isEmpty()) return null; //2.5;
        HashObjObjMap<String, Integer> userRatings = userRateMap.get(targetUserId);
        for (String item : frequencyMap.keySet()) {
            frequency = frequencyMap.get(item);
            rating = userRatings.get(item);
            weightedRatingsTotal += rating * frequency;
            weightsTotal += frequency;
        }

        return weightedRatingsTotal / weightsTotal;

    }

}
