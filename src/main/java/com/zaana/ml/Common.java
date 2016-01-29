package com.zaana.ml;

import com.google.common.collect.MinMaxPriorityQueue;
import org.apache.log4j.Logger;
import org.apache.log4j.pattern.IntegerPatternConverter;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public final class Common
{

    static Logger LOG = Logger.getLogger(Common.class);

    private Common() {

    }

    /**
     * This method returns the list of items that have not been rated by the target user
     * 
     * @param userRateMap
     * @param itemSet
     * @param targetUserId
     * @return The list of items that have not been rated by the target user
     */
    public static Set<String> getUserNonRatedItemList(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            Set<String> itemSet, String targetUserId)
    {

        try {
            Set<String> ratedItems = userRateMap.get(targetUserId).keySet();
            Set<String> diff = new HashSet<>(itemSet);
            diff.removeAll(ratedItems);
            return diff;
        } catch (NullPointerException e) {
            throw e;
        }
    }

    public static LinkedHashMap<String, Double> getkNNList(
            LinkedHashMap<String, Double> targetUserSimilarityList,
            HashMap<String, HashMap<String, Integer>> userRateMap,
            String movieId, int kNN)
    {

        Iterator<Entry<String, Double>> iter = targetUserSimilarityList
                .entrySet().iterator();
        LinkedHashMap<String, Double> kNNList = new LinkedHashMap<String, Double>();
        // K:userId, V: similarity
        int numNN = 0;

        while (numNN < kNN && iter.hasNext()) {
            Entry<String, Double> nearestNeighbor = iter.next();
            Integer movieRating = userRateMap.get(nearestNeighbor.getKey())
                    .get(movieId);
            if (movieRating != null) {
                kNNList.put(nearestNeighbor.getKey(),
                        nearestNeighbor.getValue());
                numNN++;
            }
        }
        return kNNList;
    }

    public static Set<String> getTopN(
            LinkedHashMap<String, Double> list, int topN)
    {
    
        LinkedHashMap<String, Double> sorted = SortHashMap.sortByValues(list);
        Iterator<Entry<String, Double>> iter = sorted.entrySet().iterator();
        Set<String> recommList = new HashSet<>();
        int i = 0;
        while (iter.hasNext() && i < topN) {
            Entry<String, Double> entry = iter.next();
            recommList.add(entry.getKey());
            i++;
        }
    
        return recommList;
    
    }

    /**
     * Returns most frequent items at the front of the queue.
     *
     * @param candidateList
     * @return
     */
    public static Queue<AbstractMap.SimpleEntry<String, Integer>> buildFrequencyBasedPriorityQueue(
            List<String> candidateList) {

        Comparator<Entry<String, Integer>> comparator = new Comparator<Entry <String, Integer>>() {
            public int compare(Entry <String, Integer> o1, Entry<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        };
        HashMap <String,Integer> frequencyMap = new HashMap<>();
        for (String a: candidateList) {
            if(frequencyMap.containsKey(a)) {
                frequencyMap.put(a, frequencyMap.get(a)+1);
            }
            else{ frequencyMap.put(a, 1); }
        }
        Queue<AbstractMap.SimpleEntry<String, Integer>> q = new PriorityQueue<>(comparator);
        //Set<String> candidateSet = new HashSet<>(candidateList);
        //for (String itemId : candidateSet) {
        //Integer frequency = Collections.frequency(candidateList, itemId);
//q.add(new HashMap.SimpleEntry<>(itemId, frequency));
        for ( Map.Entry<String, Integer> itemId : frequencyMap) q.add((HashMap.SimpleEntry) itemId);
        return q;

    }

    static class CustomComparatorInt implements Comparator<Map.Entry <String, Integer>>, Serializable {
        public int compare(Map.Entry <String, Integer> o1, Map.Entry<String, Integer> o2) {
            return Double.compare(o2.getValue(), o1.getValue());
        }
    }
    public static Set<String> sortByValueAndGetTopNItems(HashMap<String, Integer> ratingsSet, int n)
    {
        MinMaxPriorityQueue<Entry<String, Integer>> topNReccQueue =
                MinMaxPriorityQueue.orderedBy(new CustomComparatorInt()).maximumSize(n).create();
        for (Map.Entry<String, Integer> entry : ratingsSet.entrySet()) {
            topNReccQueue.offer(entry);
        }

        Set<String> topNSet = new HashSet<>();

        while (!topNReccQueue.isEmpty()) {
            try {
                topNSet.add(topNReccQueue.poll().getKey());
            } catch (NoSuchElementException e) {
                //none
            }
        }
        return topNSet;
    }


    static class CustomComparatorDouble implements Comparator<Map.Entry <String, Double>>, Serializable
    {
        public int compare(Map.Entry <String, Double> o1, Map.Entry<String, Double> o2) {
            return Double.compare(o2.getValue(), o1.getValue());
        }
    }

    public static Set<String> sortByValueAndGetTopN(HashMap<String, Double> simList, int topN)
    {
        MinMaxPriorityQueue<Entry<String, Double>> topNReccQueue =
                MinMaxPriorityQueue.orderedBy(new CustomComparatorDouble()).maximumSize(topN).create();
        for (Map.Entry<String, Double> entry : simList.entrySet()) {
            topNReccQueue.offer(entry);
        }
        Set<String> topNSet = new HashSet<>();
        while (!topNReccQueue.isEmpty()) {
            try {
                topNSet.add(topNReccQueue.poll().getKey());
            } catch (NoSuchElementException e) {
                //none
            }
        }
        return topNSet;
    }

}
