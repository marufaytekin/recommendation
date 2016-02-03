package com.zaana.ml;

import com.google.common.collect.MinMaxPriorityQueue;
import org.apache.log4j.Logger;

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
        Set<String> recommList = new HashSet<>();
        int i = 0;
        Iterator<Entry<String, Double>> iter = sorted.entrySet().iterator();
        while (iter.hasNext() && i < topN) {
            Entry<String, Double> entry = iter.next();
            recommList.add(entry.getKey());
            i++;
        }
    
        return recommList;
    
    }

    /**
     * Calculates frequency of uniqueue items in a list.
     * @param candidateList
     * @return HashMap of <item, frequency>
     */
    public static HashMap<String, Integer> getFrequencyMap(
            List<String> candidateList) {
        HashMap <String,Integer> frequencyMap = new HashMap<>();
        for (String element: candidateList) {
            if(frequencyMap.containsKey(element)) {
                frequencyMap.put(element, frequencyMap.get(element)+1);
            }
            else{ frequencyMap.put(element, 1); }
        }
        return frequencyMap;
    }


    /**
     * Returns most frequent n elements at the front of the queue.
     *
     * @param candidateList
     * @param n
     * @return
     */
    public static Set<String> getMostFrequentTopNElements(
            List<String> candidateList, int n) {

        Comparator<Entry<String, Integer>> comparator = new Comparator<Entry <String, Integer>>() {
            public int compare(Entry <String, Integer> o1, Entry<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        };
        HashMap <String,Integer> frequencyMap = new HashMap<>();
        for (String element: candidateList) {
            if(frequencyMap.containsKey(element)) {
                frequencyMap.put(element, frequencyMap.get(element)+1);
            }
            else{ frequencyMap.put(element, 1); }
        }

        MinMaxPriorityQueue<Map.Entry<String, Integer>> q = MinMaxPriorityQueue
                .orderedBy(comparator)
                .maximumSize(n)
                .create();
        for ( Map.Entry<String, Integer> elementId : frequencyMap.entrySet()) q.offer(elementId);

        Set<String> recSet = new HashSet<>();
        for (;0 < q.size() && recSet.size() < n;)
            try {
                HashMap.Entry<String, Integer> entry = q.remove();
                recSet.add(entry.getKey());
            } catch (NoSuchElementException ignored) {}

        return recSet;

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
