package com.zaana.ml.recomm.lsh;

/**
 * Created by maytekin on 03.02.2016.
 */

import com.zaana.ml.LSH;
import com.zaana.ml.Vector;

import java.util.*;
import java.util.List;


/**
 * Created by maytekin on 28.01.2016.
 */
public class UBLSHRandomRecommender extends AbstractLSHRecommender {

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
    public List<String> getCandidateItemList(HashMap<String, HashMap<String, Integer>> userRateMap, HashMap<String, HashSet<String>> userRateSet, String userId, Set<String> ratedItemSet) {
        return null;
    }


    @Override
    public Set<String> recommendItems(
            String userId, List<String> candidateList, int topN)
    {
        return null;
    }

    @Override
    public Double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            String targetUserId,
            String movieId,
            Set <String> intersectionOfCandidateRatedUserSets,
            List<String> candidateSetList)
    {
        double weightedRatingsTotal = 0.0;
        Integer rating;
        int size = candidateSetList.size();
        int idx;
        int cnt = 0;
        for (int i = 0; cnt < 20 && i < size; i++) {
            idx = (int) Math.floor(Math.random()*size);
            String userId = candidateSetList.get(idx);
            rating = userRateMap.get(userId).get(movieId);
            if (rating != null){
                weightedRatingsTotal += rating;
                cnt++;
            }
        }
        candidateItemListSize = candidateSetList.size();
        if (cnt == 0) return null;
        else return weightedRatingsTotal/cnt;
    }

}
