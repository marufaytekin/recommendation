package com.zaana.ml.recomm;

import com.zaana.ml.Common;
import com.zaana.ml.FreqPriorityQueue;
import com.zaana.ml.LSH;

import java.util.*;

/**
 * Created by maruf on 26/01/16.
 */
public class LSHFreqRecommender implements LSHReccommenderInterface {

    public LSHFreqRecommender() {
        super();
    }

    public Set<String> recommendItems(
            HashMap<Integer, HashMap<String, Set<String>>> itemHashTables,
            HashMap<String, Integer> ratingsSet,
            HashMap<String, String> itemHashKeyTable,
            int topN)
    {
        Set<String>topLikedItems = Common.sortByValueAndGetTopNItems(ratingsSet, 10);
        List<String> candidateList = new ArrayList<>();
        for (String ratedItemId : topLikedItems) {
            Set<String> candidateSet = LSH.getCandidateItemSetFromHashTable
                    (itemHashTables, ratingsSet, ratedItemId, itemHashKeyTable);
            candidateList.addAll(candidateSet);
        }
        Queue<AbstractMap.SimpleEntry<String, Integer>> q =
                FreqPriorityQueue.buildFrequencyBasedPriorityQueue(candidateList);
        Set<String> recSet = new HashSet<>();
        for (int i=0; i < q.size() && recSet.size() < topN; i++)
            try {
                HashMap.SimpleEntry<String, Integer> entry = q.remove();
                recSet.add(entry.getKey());
            } catch (NoSuchElementException ignored) {}

        return recSet;

    }


}
