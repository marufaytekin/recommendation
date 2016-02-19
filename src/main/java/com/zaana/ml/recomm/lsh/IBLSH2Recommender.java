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
        hashTables = LSH.buildModel(itemRateMap, vmap, numOfBands);
        hashKeyLookupTable = LSH.getHashKeyLookupTable();
    }

    @Override
    public List<String> getCandidateItemList(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashSet<String>> userRateSet,
            String userId,
            Set<String> ratedItemSet) {
        ratedItemSet = Common.sortByValueAndGetTopNItems(userRateMap.get(userId), 20);
        //Set<String> ratedItemSet = userRateSet.get(userId);
        List<String> candidateList = new ArrayList<>();
        for (String testItemId : ratedItemSet) {
            Set<String> candidateSet = LSH.getCandidateItemSetForTopNRecommendation
                    (hashTables, ratedItemSet, testItemId, hashKeyLookupTable);
            candidateList.addAll(candidateSet);
        }
        //Collections.shuffle(candidateList);
        return candidateList;
    }

    @Override
    public Set<String> recommendItems(
            String userId, List<String> candidateList, int topN)
    {
        //HashMap<String, Integer> ratingsSet = userRateMap.get(userId);
        //Set<String> userRatingSet = ratingsSet.keySet(); // use all items rated by user.
        //candidateItemListSize = candidateList.size();
        //uniqueCandidateItemListSize = uniqueueItemsSet.size();
        Set<String> recSet = new HashSet<>();
        int size = candidateList.size();
        for (int i = size; i > 0 && recSet.size() < topN; i--) {
            int idx = (int) Math.floor(Math.random()*size);
            recSet.add(candidateList.get(idx));
        }

        return recSet;
    }

    @Override
    public Double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            String targetUserId, String itemId,
            Set <String> intersectionOfCandidateItemSet,
            List<String> candidateSetList) {

        double rating;
        double weightedRatingsTotal = 0;
        int size = candidateSetList.size();
        int idx;
        List <String> kNNList = new ArrayList<>();
        for (int i = size; i > 0 && kNNList.size() <= 20; i--) {
            idx = (int) Math.floor(Math.random()*size);
            String candidateUser = candidateSetList.get(idx);
            if (intersectionOfCandidateItemSet.contains(candidateUser)) {
                kNNList.add(candidateUser);
            }
        }
        //candidateItemListSize = candidateSetList.size();
        if (kNNList.isEmpty()) return null;
        for (String candidateItem : kNNList) {
            rating = userRateMap.get(targetUserId).get(candidateItem);
            weightedRatingsTotal += rating;
        }
        //uniqueCandidateItemListSize = intersectionOfCandidateItemSet.size();
        return weightedRatingsTotal / kNNList.size();
    }


}
