package com.zaana.ml.recomm;

import com.zaana.ml.SortHashMap;

import java.util.*;

/**
 * Created by maruf on 09/05/15.
 */
public class LSHRecommendation extends AbstractRecommendation {

    public static List<String> recommendItems(
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            HashMap<Integer, HashMap<String, Set<String>>> itemHashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            HashMap<String, Integer> ratingsSet, String targetUserId, int topN, int kNN, int y)
    {
        LinkedHashMap<String, Integer> sortedRatings = SortHashMap.sortByValues(ratingsSet);
        Set<String>topLikedItems = new HashSet<>();
        Iterator<Map.Entry<String, Integer>> iter = sortedRatings.entrySet().iterator();
        int i = 0;
        while (iter.hasNext() || i < kNN){
            Map.Entry<String, Integer> entry = iter.next();
            topLikedItems.add(entry.getKey());
        }


    }
}
