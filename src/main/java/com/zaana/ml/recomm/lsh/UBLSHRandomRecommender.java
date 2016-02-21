package com.zaana.ml.recomm.lsh;

/**
 * Created by maytekin on 03.02.2016.
 */

import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;

import java.util.*;
import java.util.List;


/**
 * Created by maytekin on 28.01.2016.
 */
public class UBLSHRandomRecommender extends AbstractLSHRecommender {

    @Override
    public void buildModel(HashMap<String, HashMap<String, Integer>> userRateMap,
                           HashMap<String, HashMap<String, Integer>> itemRateMap,
                           int numOfBands, int numOfHashFunctions) { }

    @Override
    public List<String> getCandidateItemList(HashMap<String, HashMap<String, Integer>> userRateMap, HashObjObjMap<Object, Object> userRateSet, String userId, HashObjSet<String> ratedItemSet) {
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
