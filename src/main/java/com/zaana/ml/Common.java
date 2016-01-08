package com.zaana.ml;

import org.apache.log4j.Logger;

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

}
