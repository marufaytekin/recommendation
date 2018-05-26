package com.zaana.ml.recomm.lsh;

import com.zaana.ml.lsh.LSH;
import com.zaana.ml.utils.Vector;
import com.zaana.ml.utils.Common;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;

import java.util.*;

/**
 * This recommender implements Karypis' user-based top-N recommendation with LSH.
 * Evaluation of Item-Based Top-N Recommendation Algorithms paper:
 * http://www.dtic.mil/dtic/tr/fulltext/u2/a439546.pdf
 */
public class UBLSH1Recommender extends AbstractLSHRecommender {

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

    @Override
    public List<String> getCandidateItemList(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<Object, Object> userRateSet,
            String userId,
            HashObjSet<String> ratedItemSet) {
        //Set<String> userRatingsSet = userRateSet.get(userId);
        HashObjSet <String> userCandidateSet =
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
        return Common.getMostFrequentTopNElementList(candidateList, topN);

    }

    @Override
    public Double calculatePrediction(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            String targetUserId,
            String movieId,
            HashObjSet<String> intersectionOfCandidateRatedUserSets,
            List<String> candidateSetList) {

        double weightedRatingsTotal = 0;
        double weightsTotal = 0;
        Integer frequency;
        Integer rating;
        HashMap <String, Integer> frequencyMap =
                Common.getCandidateFrequentNElementsMap(
                        candidateSetList, intersectionOfCandidateRatedUserSets, 20);
        //candidateItemListSize = candidateSetList.size();
        if (frequencyMap.isEmpty()) return null; //2.5;
        HashObjObjMap<String, Integer> usersRated = itemRateMap.get(movieId);
        for (String candidateUser : frequencyMap.keySet()) {
            frequency = frequencyMap.get(candidateUser);
            rating = usersRated.get(candidateUser);
            weightedRatingsTotal += rating * frequency;
            weightsTotal += frequency;
        }
        //uniqueCandidateItemListSize = candidateSet.size();
        return weightedRatingsTotal / weightsTotal;

    }


}
