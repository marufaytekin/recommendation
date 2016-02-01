package com.zaana.ml.prediction;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by maruf on 31/01/16.
 */
public class UBLSHPrediction extends LSHPrediction {
    @Override
    public double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            List<String> candidateSetList,
            Set<String> intersectionOfCandidateRatedUserSets,
            String movieId) {
        double weightedRatingsTotal = 0;
        double weightsTotal = 0;
        Integer frequency;
        Integer rating;
        for (String candidateUser : intersectionOfCandidateRatedUserSets) {
            frequency = Collections.frequency(candidateSetList, candidateUser);
            rating = userRateMap.get(candidateUser).get(movieId);
            weightedRatingsTotal += rating * frequency;
            weightsTotal += frequency;
        }
        if (weightsTotal != 0)
            return weightedRatingsTotal / weightsTotal;
        else
            return 0;
    }
}
