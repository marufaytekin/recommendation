package com.zaana.ml.deprecated;

import com.zaana.ml.LSH;
import com.zaana.ml.deprecated.DEPUBRecommender;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by maruf on 20/02/15.
 */
public final class DEPUBLSHRecommender {

    private static int candidateSetSize;
    public static int getCandidateSetSize() {
        return candidateSetSize;
    }
    @Deprecated
    public static Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            HashMap<String, String> hashKeyLookupTable,
            Set<String> itemSet,
            String targetUserId, int topN, int kNN, int y)
    {
        //HashMap<String, Integer> userRateList = userRateMap.get(targetUserId);
        //Set<String> candidateUserSet = LSH.getCandidateSet(hashTables, vmap, targetUserId, userRateList);
        Set<String> candidateUserSet =
                LSH.getCandidateSetFromHashTables(hashTables, targetUserId, hashKeyLookupTable);
        candidateSetSize = candidateUserSet.size();
        Set<String> userBasedTopNRecom = null;
        if (!candidateUserSet.isEmpty()) {
            userBasedTopNRecom =
                    DEPUBRecommender.recommendItems(userRateMap, itemSet, targetUserId, candidateUserSet, topN, kNN, y);
        }

        return userBasedTopNRecom;

    }
}
