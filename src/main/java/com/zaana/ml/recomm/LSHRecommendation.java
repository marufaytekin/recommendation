package com.zaana.ml.recomm;

import com.zaana.ml.LSH;
import com.zaana.ml.SortHashMap;

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
        Set<String>topLikedItems = getTopLikedItems(ratingsSet, 5);
        Iterator<String> iter = topLikedItems.iterator();
        List<String> candidateList = new ArrayList<>();
        while (iter.hasNext()) {
            String testItemId = iter.next();
            Set<String> candidateSet = LSH.getCandidateSetItemTable(itemHashTables, ratingsSet, testItemId, itemHashKeyTable);
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
        Set<String>topLikedItems = getTopLikedItems(ratingsSet, 5);
        Iterator<String> iter = topLikedItems.iterator();
        List<String> candidateList = new ArrayList<>();
        while (iter.hasNext()) {
            String testItemId = iter.next();
            Set<String> candidateSet = LSH.getCandidateSetItemTable(itemHashTables, ratingsSet, testItemId, itemHashKeyTable);
            candidateList.addAll(candidateSet);
        }
        Iterator<String> iter2 = new HashSet<>(candidateList).iterator();

        Comparator comparator = new Comparator<Map.Entry <String, Integer>>() {
            public int compare(Map.Entry <String, Integer> o1, Map.Entry<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        };
        Queue q = new PriorityQueue(topN, comparator);
        while (iter2.hasNext()) {
            String itemId = iter2.next();
            Integer frequency = Collections.frequency(candidateList, itemId);
            q.add(new HashMap.SimpleEntry<>(itemId, frequency));
        }

        Set<String> recSet = new HashSet<>();
        for (int i=0; i < q.size() && recSet.size() < topN; i++) {
            try {
                HashMap.SimpleEntry<String, Integer> entry = (HashMap.SimpleEntry<String, Integer>) q.remove();
                recSet.add(entry.getKey());
            }
            catch (NoSuchElementException e) { }
        }

        return recSet;

    }

    private static Set<String> getTopLikedItems(HashMap<String, Integer> ratingsSet, int n)
    {
        LinkedHashMap<String, Integer> sortedRatings = SortHashMap.sortByValues(ratingsSet);
        Set<String>topLikedItems = new HashSet<>();
        Iterator<Map.Entry<String, Integer>> iter = sortedRatings.entrySet().iterator();
        int i = 0;
        while (iter.hasNext() && i < n){
            Map.Entry<String, Integer> entry = iter.next();
            topLikedItems.add(entry.getKey());
            i ++;
        }
        return topLikedItems;
    }

}
