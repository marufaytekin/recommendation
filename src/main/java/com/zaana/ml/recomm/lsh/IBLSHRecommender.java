package com.zaana.ml.recomm.lsh;

import com.zaana.ml.*;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maruf on 26/01/16.
 */
public class IBLSHRecommender extends AbstractLSHReccommender {

    public IBLSHRecommender() {
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
        hashTables = LSH.buildModel(itemRateMap, vmap, numOfBands);
        hashKeyLookupTable = LSH.getHashKeyLookupTable();
    }

    /**
     * IBRecommender with LSH based on frequency of items in the buckets.
     * Instead of merged similarity value we use frequency of items in candidate list.
     * @param userRateMap
     * @param userId
     * @param topN    @return    */
    @Override
    public Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            String userId, int topN)
    {
        HashMap<String, Integer> ratingsSet = userRateMap.get(userId);
        Set<String> ratedItemSet = userRateMap.get(userId).keySet();
        List<String> candidateList = new ArrayList<>();
        Set<String> uniqueueItemsSet = new HashSet<>();
        for (String ratedItemId : ratedItemSet) {
            Set<String> candidateSet = LSH.getCandidateItemSetFromHashTable
                    (hashTables, ratingsSet, ratedItemId, hashKeyLookupTable);
            candidateList.addAll(candidateSet);
            //uniqueueItemsSet.addAll(candidateSet);
        }
        candidateItemListSize = candidateList.size();
        //uniqueCandidateItemListSize = uniqueueItemsSet.size();
        Queue<AbstractMap.SimpleEntry<String, Integer>> q =
                Common.buildFrequencyBasedPriorityQueue(candidateList);
        Set<String> recSet = new HashSet<>();
        for (int i = 0; i < q.size() && recSet.size() < topN; i++)
            try {
                HashMap.SimpleEntry<String, Integer> entry = q.remove();
                recSet.add(entry.getKey());
            } catch (NoSuchElementException ignored) {}

        return recSet;

    }


}
