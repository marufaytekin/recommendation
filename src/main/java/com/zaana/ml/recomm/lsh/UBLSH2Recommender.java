package com.zaana.ml.recomm.lsh;

import com.zaana.ml.lsh.LSH;
import com.zaana.ml.utils.Vector;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import net.openhft.koloboke.collect.set.hash.HashObjSets;

import java.util.*;

/**
 * Created by maytekin on 28.01.2016.
 */
public class UBLSH2Recommender extends AbstractLSHRecommender {

    @Override
    public void buildModel(HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
                           HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
                           int numOfBands, int numOfHashFunctions) {
        Set<String> itemSet = itemRateMap.keySet();
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap =
                Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
        hashTables = LSH.buildModel(userRateMap, vmap, numOfBands);
        hashKeyLookupTable = LSH.getHashKeyLookupTable();
    }

    public List<String> getCandidateItemList(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<Object, Object> userRateSet,
            String userId,
            HashObjSet<String> ratedItemSet)
    {
        HashObjSet<String> userCandidateSet =
                LSH.getCandidateSetFromHashTables(hashTables, userId, hashKeyLookupTable);
        HashObjSet<String> neighborsRatingSet;
        List<String> ratedItemList = new ArrayList<>();
        for (String neighborId : userCandidateSet) {
            neighborsRatingSet = (HashObjSet<String>) userRateSet.get(neighborId);
            neighborsRatingSet.removeAll(ratedItemSet);
            ratedItemList.addAll(neighborsRatingSet);
        }
        //Collections.shuffle(ratedItemList);
        return ratedItemList;
    }

    @Override
    public List<String> recommendItems(
            String userId, List<String> candidateList, int topN)
    {
        List<String> recList = new ArrayList<>();
        int size = candidateList.size();
        int idx;
        for (int i = size; i > 0 && recList.size() < topN; i--) {
            idx = (int) Math.floor(Math.random()*size);
            recList.add(candidateList.get(idx));
        }
        return recList;
    }

    @Override
    public Double calculatePrediction(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            String targetUserId,
            String movieId,
            HashObjSet<String> intersectionOfCandidateRatedUserSets,
            List<String> candidateSetList)
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
        if (kNNList.isEmpty()) return null; //2.5;
        HashObjObjMap<String, Integer> usersRated = itemRateMap.get(movieId);
        for (String candidateUser : kNNList) {
            rating = usersRated.get(candidateUser);
            weightedRatingsTotal += rating;
        }
        //candidateItemListSize = candidateSetList.size();
        //uniqueCandidateItemListSize = candidateSet.size();
        return weightedRatingsTotal/kNNList.size();
    }

}
