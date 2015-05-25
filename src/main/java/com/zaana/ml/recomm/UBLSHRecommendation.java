package com.zaana.ml.recomm;

import com.zaana.ml.LSH;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by maruf on 20/02/15.
 */
public final class UBLSHRecommendation extends LSHRecommendation{

    public static List<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            Set<String> itemSet, String targetUserId, int topN, int kNN, int y)
    {
        HashMap<String, Integer> userRateList = userRateMap.get(targetUserId);
        Set<String> candidateUserSet = LSH.getCandidateSet(hashTables, vmap, targetUserId,
                userRateList);
        candidateSetSize = candidateUserSet.size();
        List<String> userBasedTopNRecom = null;
        if (!candidateUserSet.isEmpty()) {
            userBasedTopNRecom = UBRecommendation.recommendItems(
                    userRateMap, itemSet, targetUserId, candidateUserSet, topN, kNN, y);
        }

        return userBasedTopNRecom;

    }
}
