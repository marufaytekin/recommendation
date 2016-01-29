package com.zaana.ml.recomm.lsh;

import java.util.*;

/**
 * Created by maruf on 07/05/15.
 */
public abstract class AbstractLSHReccommender {

    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
    HashMap<String, String> hashKeyLookupTable;
    int uniqueCandidateItemListSize;

    public int getCandidateItemListSize() {
        return candidateItemListSize;
    }

    public int getUniqueCandidateItemListSize() {
        return uniqueCandidateItemListSize;
    }

    int candidateItemListSize;

    public abstract void buildModel(HashMap<String, HashMap<String, Integer>> userRateMap,
                                    HashMap<String, HashMap<String, Integer>> itemRateMap,
                                    int numOfBands, int numOfHashFunctions);
    public abstract Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            String userId, int topN);
}
