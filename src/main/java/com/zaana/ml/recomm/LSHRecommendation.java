package com.zaana.ml.recomm;

import com.zaana.ml.LSH;
import com.zaana.ml.SortHashMap;

import java.util.*;

/**
 * Created by maruf on 09/05/15.
 */
public class LSHRecommendation extends AbstractRecommendation {

    public static Set<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<Integer, HashMap<String, Set<String>>> itemHashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            HashMap<String, Integer> ratingsSet, HashMap<String, String> itemHashKeyTable, int topN)
    {
        Set<String>topLikedItems = getTopLikedItems(ratingsSet, 5);
        Iterator<String> iter = topLikedItems.iterator();
        List<String> candidateList = new ArrayList<>();
        while (iter.hasNext()) {
            String testItemId = iter.next();
            Set<String> candidateSet = LSH.getCandidateSetItemTable(itemHashTables, vmap, testItemId, itemHashKeyTable);
            candidateList.addAll(candidateSet);
        }
        Set<String> recSet = new HashSet<>();
        int size = candidateList.size();
        for (int i = candidateList.size(); i > 1 && recSet.size() < topN; i--) {
            int idx = (int) Math.floor(Math.random()*size);
            recSet.add(candidateList.get(idx));
        }

        return recSet;
    }

    private static Set<String> getTopLikedItems(HashMap<String, Integer> ratingsSet, int n) {
        LinkedHashMap<String, Integer> sortedRatings = SortHashMap.sortByValues(ratingsSet);
        Set<String>topLikedItems = new HashSet<>();
        Iterator<Map.Entry<String, Integer>> iter = sortedRatings.entrySet().iterator();
        int i = 0;
        while (iter.hasNext() && i < n){
            Map.Entry<String, Integer> entry = iter.next();
            topLikedItems.add(entry.getKey());
        }
        return topLikedItems;
    }

}
