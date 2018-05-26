package com.zaana.ml.recomm.lsh;

import com.zaana.ml.lsh.LSH;
import com.zaana.ml.utils.Vector;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import net.openhft.koloboke.collect.set.hash.HashObjSets;

import java.util.*;

/**
 * Created by maruf on 09/05/15.
 */
public class IBLSH2Recommender extends AbstractLSHRecommender {

    public IBLSH2Recommender() {
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
        //ratedItemSet = Common.sortByValueAndGetTopNItems(userRateMap.get(userId), 20);
        //ratedItemSet = (HashObjSet<String>) userRateSet.get(userId);
        List<String> candidateList = new ArrayList<>();
        for (String testItemId : ratedItemSet) {
            HashObjSet<String> candidateSet = LSH.getCandidateItemSetForTopNRecommendation
                    (hashTables, ratedItemSet, testItemId, hashKeyLookupTable);
            candidateList.addAll(candidateSet);
        }
        //Collections.shuffle(candidateList);
        return candidateList;
    }

    @Override
    public List<String> recommendItems(String userId, List<String> candidateList, int topN) {
        List<String> recList = new ArrayList<>();
        int size = candidateList.size();
        for (int i = size; i > 0 && recList.size() < topN; i--) {
            int idx = (int) Math.floor(Math.random()*size);
            recList.add(candidateList.get(idx));
        }
        return recList;
    }

    @Override
    public Double calculatePrediction(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            String targetUserId, String itemId,
            HashObjSet<String> intersectionOfCandidateItemSet,
            List<String> candidateSetList) {
        double weightedRatingsTotal = 0;
        int size = candidateSetList.size();
        int idx;
        double rating;
        List <String> kNNList = new ArrayList<>();
        for (int i = size; i > 0 && kNNList.size() <= 20; i--) {
            idx = (int) Math.floor(Math.random()*size);
            String candidateUser = candidateSetList.get(idx);
            if (intersectionOfCandidateItemSet.contains(candidateUser)) {
                kNNList.add(candidateUser);
            }
        }
        if (kNNList.isEmpty()) return null; //2.5;
        HashObjObjMap<String, Integer> userRatings = userRateMap.get(targetUserId);
        for (String candidateItem : kNNList) {
            rating = userRatings.get(candidateItem);
            weightedRatingsTotal += rating;
        }
        return weightedRatingsTotal / kNNList.size();
    }


}
