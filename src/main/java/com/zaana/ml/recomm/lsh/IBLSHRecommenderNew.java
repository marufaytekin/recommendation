package com.zaana.ml.recomm.lsh;

import com.zaana.ml.*;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maruf on 09/05/15.
 */
public class IBLSHRecommenderNew extends AbstractLSHReccommender {

    public IBLSHRecommenderNew() {
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

    @Override
    public Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            String userId, int topN)
    {
        HashMap<String, Integer> ratingsSet = userRateMap.get(userId);
        //Set<String>topLikedItems = Common.sortByValueAndGetTopNItems(ratingsSet, 20);
        Set<String> userRatingSet = ratingsSet.keySet();
        Set<String> uniqueueItemsSet = new HashSet<>();
        List<String> candidateList = new ArrayList<>();
        for (String testItemId : userRatingSet) {
            Set<String> candidateSet = LSH.getCandidateItemSetFromHashTable
                    (hashTables, ratingsSet, testItemId, hashKeyLookupTable);
            candidateList.addAll(candidateSet);
            uniqueueItemsSet.addAll(candidateSet);
        }
        candidateItemListSize = candidateList.size();
        uniqueCandidateItemListSize = uniqueueItemsSet.size();
        Set<String> recSet = new HashSet<>();
        int size = candidateList.size();
        for (int i = candidateList.size(); i > 0 && recSet.size() < topN; i--) {
            int idx = (int) Math.floor(Math.random()*size);
            recSet.add(candidateList.get(idx));
        }

        return recSet;
    }


}
