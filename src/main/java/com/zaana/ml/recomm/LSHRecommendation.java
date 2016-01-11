package com.zaana.ml.recomm;

import com.zaana.ml.FreqPriorityQueue;
import com.zaana.ml.LSH;
import com.zaana.ml.SortHashMap;
import org.apache.commons.collections.*;
import org.apache.commons.collections.PriorityQueue;

import java.util.*;

/**
 * Created by maruf on 09/05/15.
 */
public class LSHRecommendation extends AbstractRecommendation {

    public static Set<String> recommendItems(
            HashMap<Integer, HashMap<String, Set<String>>> itemHashTables,
            HashMap<String, Integer> ratingsSet,
            HashMap<String, String> itemHashKeyTable,
            int topN)
    {
        Set<String>topLikedItems = getTopLikedItems(ratingsSet, 10);
        List<String> candidateList = new ArrayList<>();
        for (String testItemId : topLikedItems) {
            Set<String> candidateSet = LSH.getCandidateItemSetFromHashTable(itemHashTables, ratingsSet, testItemId, itemHashKeyTable);
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

    public static Set<String> recommendFrequentItems(
            HashMap<Integer, HashMap<String, Set<String>>> itemHashTables,
            HashMap<String, Integer> ratingsSet,
            HashMap<String, String> itemHashKeyTable,
            int topN)
    {
        Set<String>topLikedItems = getTopLikedItems(ratingsSet, 10);
        List<String> candidateList = new ArrayList<>();
        for (String ratedItemId : topLikedItems) {
            Set<String> candidateSet = LSH.getCandidateItemSetFromHashTable(itemHashTables, ratingsSet, ratedItemId, itemHashKeyTable);
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

    private static Set<String> getTopLikedItems(HashMap<String, Integer> ratingsSet, int n)
    {
        LinkedHashMap<String, Integer> sortedRatings = SortHashMap.sortByValues(ratingsSet);
        Set<String>topLikedItems = new HashSet<>();
        int i = 0;
        Iterator<Map.Entry<String, Integer>> iter = sortedRatings.entrySet().iterator();
        while (iter.hasNext() && i < n){
            Map.Entry<String, Integer> entry = iter.next();
            topLikedItems.add(entry.getKey());
            i ++;
        }
        return topLikedItems;
    }

}
