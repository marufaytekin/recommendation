package com.zaana.ml.recomm.lsh;

import com.zaana.ml.LSH;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maytekin on 28.01.2016.
 */
public class UBLSH2Recommender extends AbstractLSHRecommender {

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

    public List<String> getCandidateItemList(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashSet<String>> userRateSet,
            String userId,
            Set<String> ratedItemSet)
    {
        //Set<String> userRatingList = userRateSet.get(userId);
        Set<String> userCandidateSet =
                LSH.getCandidateSetFromHashTables(hashTables, userId, hashKeyLookupTable);
        Set<String> neighborsRatingSet;
        List<String> ratedItemList = new ArrayList<>();
        for (String neighborId : userCandidateSet) {
            neighborsRatingSet = userRateSet.get(neighborId);
            neighborsRatingSet.removeAll(ratedItemSet);
            ratedItemList.addAll(neighborsRatingSet);
        }
        //Collections.shuffle(ratedItemList);
        return ratedItemList;
    }

    @Override
    public Set<String> recommendItems(
            String userId, List<String> candidateList, int topN)
    {
        Set<String> recSet = new HashSet<>();
        int size = candidateList.size();
        int idx;
        for (int i = size; i > 0 && recSet.size() < topN; i--) {
            idx = (int) Math.floor(Math.random()*size);
            recSet.add(candidateList.get(idx));
        }
        return recSet;
    }

    @Override
    public Double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            String targetUserId,
            String movieId, Set <String> intersectionOfCandidateRatedUserSets, List<String> candidateSetList)
    {
        double weightedRatingsTotal = 0.0;
        Integer rating;
        int size = candidateSetList.size();
        int idx;
        List <String> kNNList = new ArrayList<>();
        for (int i = candidateSetList.size(); i > 0 && kNNList.size() <= 20; i--) {
            idx = (int) Math.floor(Math.random()*size);
            String candidateUser = candidateSetList.get(idx);
            if (intersectionOfCandidateRatedUserSets.contains(candidateUser)) {
                kNNList.add(candidateUser);
            }
        }
        if (kNNList.isEmpty()) return null;
        for (String candidateUser : kNNList) {
            rating = userRateMap.get(candidateUser).get(movieId);
            weightedRatingsTotal += rating;
        }
        //candidateItemListSize = candidateSetList.size();
        //uniqueCandidateItemListSize = candidateSet.size();
        return weightedRatingsTotal/kNNList.size();
    }

}
