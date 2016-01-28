package com.zaana.ml.recomm;

import com.zaana.ml.Common;
import com.zaana.ml.LSH;

import java.util.*;

/**
 * Created by maruf on 09/05/15.
 */
public class LSHRecommender implements LSHReccommenderInterface {

    public LSHRecommender() {
        super();
    }

    public Set<String> recommendItems(
            HashMap<Integer, HashMap<String, Set<String>>> itemHashTables,
            HashMap<String, Integer> ratingsSet,
            HashMap<String, String> itemHashKeyTable,
            int topN)
    {
        Set<String>topLikedItems = Common.sortByValueAndGetTopNItems(ratingsSet, 20);
        List<String> candidateList = new ArrayList<>();
        for (String testItemId : topLikedItems) {
            Set<String> candidateSet = LSH.getCandidateItemSetFromHashTable
                    (itemHashTables, ratingsSet, testItemId, itemHashKeyTable);
            candidateList.addAll(candidateSet);
        }
        Set<String> recSet = new HashSet<>();
        int size = candidateList.size();
        for (int i = candidateList.size(); i >= 0 && recSet.size() < topN; i--) {
            int idx = (int) Math.floor(Math.random()*size);
            recSet.add(candidateList.get(idx));
        }

        return recSet;
    }


}
