package com.zaana.ml.tools;

import com.google.common.collect.MinMaxPriorityQueue;
import net.openhft.koloboke.collect.map.hash.HashObjIntMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import net.openhft.koloboke.collect.set.hash.HashObjSets;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

public final class Common
{

    static Logger LOG = Logger.getLogger(Common.class);

    private Common() {

    }

    public static LinkedHashMap<String, Double> getkNNList(
            LinkedHashMap<String, Double> targetUserSimilarityList,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            String movieId, int kNN)
    {
        LinkedHashMap<String, Double> kNNList = new LinkedHashMap<>();
        // K:userId, V: similarity
        int numNN = 0;
        Iterator<Entry<String, Double>> iter = targetUserSimilarityList.entrySet().iterator();
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

    public static Set<String> getTopN(LinkedHashMap<String, Double> list, int topN) {
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
    public static HashObjObjMap<String, Integer> getFrequencyMap(
            List<String> candidateList) {
        //HashMap <String,Integer> frequencyMap = new HashMap<>();
        HashObjObjMap<String, Integer> frequencyMap = HashObjObjMaps.getDefaultFactory().newMutableMap();
        for (String element: candidateList) {
            if(frequencyMap.containsKey(element)) {
                Integer frequency = frequencyMap.get(element);
                frequencyMap.put(element, frequency + 1);
            }
            else{ frequencyMap.put(element, 1); }
        }
        return frequencyMap;
    }

    public static HashObjObjMap<String, Integer> getFrequentTopNElementsMap(
            List<String> candidateList, int n) {

        Comparator<Entry<String, Integer>> comparator = new Comparator<Entry <String, Integer>>() {
            public int compare(Entry <String, Integer> o1, Entry<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        };

        HashObjObjMap <String,Integer> frequencyMap = getFrequencyMap(candidateList);

        MinMaxPriorityQueue<Map.Entry<String, Integer>> q = MinMaxPriorityQueue
                .orderedBy(comparator)
                .maximumSize(n)
                .create();
        for ( Map.Entry<String, Integer> elementId : frequencyMap.entrySet()) q.offer(elementId);
        HashObjObjMap<String, Integer> frequencyMapList = HashObjObjMaps.getDefaultFactory().newMutableMap();
        for (;0 < q.size() && frequencyMapList.size() < n;)
            try {
                HashObjObjMap.Entry<String, Integer> entry = q.remove();
                frequencyMapList.put(entry.getKey(), entry.getValue());
            } catch (NoSuchElementException ignored) {}
        return frequencyMapList;
    }

    public static HashMap<String, Integer> getCandidateFrequentNElementsMap(
            List<String> candidateList, Set <String> intersectionOfCandidateRatedUserSets, int n) {

        Comparator<Entry<String, Integer>> comparator = new Comparator<Entry <String, Integer>>() {
            public int compare(Entry <String, Integer> o1, Entry<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        };

        HashObjObjMap<String, Integer> frequencyMap = getFrequencyMap(candidateList);

        MinMaxPriorityQueue<Map.Entry<String, Integer>> q = MinMaxPriorityQueue
                .orderedBy(comparator)
                .maximumSize(n)
                .create();

        for ( Entry<String, Integer> elementId : frequencyMap.entrySet()) {
            if (intersectionOfCandidateRatedUserSets.contains(elementId.getKey())) {
                q.offer(elementId);
            }
        }

        HashMap <String, Integer> topNFrequencyMap = new HashMap<>();
        for (;0 < q.size() && topNFrequencyMap.size() < n;)
            try {
                HashObjIntMap.Entry<String, Integer> entry = q.remove();
                topNFrequencyMap.put(entry.getKey(), entry.getValue());
            } catch (NoSuchElementException ignored) {}

        return topNFrequencyMap;

    }


    /**
     * Returns most frequent n elements at the front of the queue.
     *
     * @param candidateList
     * @param n
     * @return
     */
    public static HashObjSet<String> getMostFrequentTopNElementSet(List<String> candidateList, int n) {
        HashObjObjMap<String, Integer> s = getFrequentTopNElementsMap(candidateList, n);
        return s.keySet();
    }


    public static HashObjSet<String> sortByValueAndGetTopNItems(HashObjObjMap<String, Integer> ratingsSet, int n) {

        Comparator<Entry<String, Integer>> comparator = new Comparator<Entry <String, Integer>>() {
            public int compare(Entry <String, Integer> o1, Entry<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        };

        MinMaxPriorityQueue<Entry<String, Integer>> topNReccQueue = MinMaxPriorityQueue
                .orderedBy(comparator)
                .maximumSize(n)
                .create();

        for (Map.Entry<String, Integer> entry : ratingsSet.entrySet()) {
            topNReccQueue.offer(entry);
        }

        HashObjSet<String> topNSet = HashObjSets.getDefaultFactory().newMutableSet();

        while (!topNReccQueue.isEmpty()) {
            try {
                topNSet.add(topNReccQueue.poll().getKey());
            } catch (NoSuchElementException e) {
                //none
            }
        }
        return topNSet;
    }


    public static Set<String> sortByValueAndGetTopN(HashMap<String, Double> simList, int topN) {
        Comparator<Entry <String, Double>> comparator = new Comparator<Entry <String, Double>>() {
            public int compare(Map.Entry <String, Double> o1, Map.Entry<String, Double> o2) {
                return Double.compare(o2.getValue(), o1.getValue());
            }
        };
        MinMaxPriorityQueue<Entry<String, Double>> topNReccQueue = MinMaxPriorityQueue
                .orderedBy(comparator)
                .maximumSize(topN)
                .create();
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
