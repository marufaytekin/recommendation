package com.zaana.ml.prediction;

import java.util.*;
import java.util.Map.Entry;

public class Prediction
{

    /**
     * This method ..
     * 
     * @param userRateMap
     * @param kNNList
     * @param movieId
     * @return
     */
    public static double calculateUserBasedPredicitonRate(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            LinkedHashMap<String, Double> kNNList, String movieId)
    {

        Iterator<Entry<String, Double>> entry = kNNList.entrySet()
                .iterator();
        double itemRate = 0.0D;
        double wuvRviTotal = 0.0D;
        double wuvTotal = 0.0D;
        while (entry.hasNext()) {
            Entry<String, Double> userSimPair = entry.next();
            String rUserId = userSimPair.getKey();
            Double wuv = userSimPair.getValue();
            Integer rvi = userRateMap.get(rUserId).get(movieId);
            wuvRviTotal += wuv.doubleValue() * rvi.intValue();
            wuvTotal += Math.abs(wuv.doubleValue());
        }
        if (wuvTotal != 0.0D) {
            itemRate = wuvRviTotal / wuvTotal;
        }

        return itemRate;

    }

    /**
     * for each entry in nearest neighbor list (k)
     *     calculate weighted rating wuv, weighted by similarity s
     *     add absolute value of weighted rating to a running total wuvTotal
     * compute the average rating by wuvTotal / number of nearest neighbor
     * return the average rating
     * 
     * @param itemRateMap
     * @param kNNList
     * @param testUserId
     * @return
     */
    public static double calculateItemBasedPredicitonRate(
            final HashMap<String, HashMap<String, Integer>> itemRateMap,
            final LinkedHashMap<String, Double> kNNList,
            final String testUserId)
    {
        Iterator<Entry<String, Double>> entry = kNNList.entrySet().iterator();
        double itemRate = 0.0D;
        double wijRujTotal = 0.0D;
        double wuvTotal = 0.0D;
        while (entry.hasNext()) {
            Entry<String, Double> nearestNeighbor = entry.next();
            String rMovieId = nearestNeighbor.getKey();
            Double wuv = nearestNeighbor.getValue();
            wijRujTotal += wuv * itemRateMap.get(rMovieId).get(testUserId);
            wuvTotal += Math.abs(wuv);
        }
        if (wuvTotal != 0.0D) {
            itemRate = wijRujTotal / wuvTotal;
        }
        return itemRate;
    }

    /**
     * Calculates LSH prediction */
    public static double calculateLSHBasedPredicitonRate(HashMap<String, HashMap<String, Integer>> userRateMap,
                                                         Set<String> intersectionOfCandidateRatedUserSets,
                                                         List<String> candidateSetList, String movieId)
    {
        Iterator<String> entry = intersectionOfCandidateRatedUserSets.iterator();
        double weightedRatingsTotal = 0;
        double weightsTotal = 0;
        while (entry.hasNext()) {
            String candidateUser = entry.next();
            Integer fraquency = Collections.frequency(candidateSetList, candidateUser);
            Integer rating = userRateMap.get(candidateUser).get(movieId);
            weightedRatingsTotal += rating * fraquency;
            weightsTotal += fraquency;
        }
        if (weightsTotal != 0)
            return weightedRatingsTotal / weightsTotal;
        else
            return 0;
    }


}
