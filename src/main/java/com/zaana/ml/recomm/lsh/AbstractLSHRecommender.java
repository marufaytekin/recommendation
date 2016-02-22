package com.zaana.ml.recomm.lsh;

import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;

import java.util.*;

/**
 * Created by maruf on 07/05/15.
 */
public abstract class AbstractLSHRecommender {

    HashObjObjMap<Object, Object> hashTables;
    HashObjObjMap<Object, Object> hashKeyLookupTable;
    int uniqueCandidateItemListSize;

    public HashObjObjMap<Object, Object> getHashTables() {
        return hashTables;
    }

    public HashObjObjMap<Object, Object> getHashKeyLookupTable() {
        return hashKeyLookupTable;
    }

    public int getCandidateItemListSize() {
        return candidateItemListSize;
    }

    public int getUniqueCandidateItemListSize() {
        return uniqueCandidateItemListSize;
    }

    int candidateItemListSize;

    public abstract void buildModel(HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
                                    HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
                                    int numOfBands, int numOfHashFunctions);

    public abstract Set<String> recommendItems(
            String userId, List<String> candidateList, int topN);

    public abstract List<String> getCandidateItemList(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<Object, Object> userRateSet,
            String userId,
            HashObjSet<String> ratedItemSet);

    public abstract Double calculatePrediction(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap,
            String targetUserId,
            String movieId,
            HashObjSet<String> intersectionOfCandidateRatedUserSets,
            List<String> candidateSetList);

}
