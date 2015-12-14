package com.zaana.ml;

import java.util.*;

/**
 * Created by maytekin on 14.12.2015.
 */
public final class FreqPriorityQueue {

    /**
     * Returns most frequent items at the front of the queue.
     *
     * @param candidateList
     * @return
     */
    public static Queue<AbstractMap.SimpleEntry<String, Integer>> buildFrequencyBasedPriorityQueue(
            List<String> candidateList) {

        Comparator<Map.Entry<String, Integer>> comparator = new Comparator<Map.Entry <String, Integer>>() {
            public int compare(Map.Entry <String, Integer> o1, Map.Entry<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        };
        Queue<AbstractMap.SimpleEntry<String, Integer>> q = new PriorityQueue<>(comparator);
        Set<String> candidateSet = new HashSet<>(candidateList);
        for (String itemId : candidateSet) {
            Integer frequency = Collections.frequency(candidateList, itemId);
            q.add(new HashMap.SimpleEntry<>(itemId, frequency));
        }
        return q;

    }

}
