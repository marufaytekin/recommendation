package com.zaana.ml.prediction;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by maruf on 31/01/16.
 */
public class UBLSHPredictionNew extends LSHPrediction {

    @Override
    public double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            List<String> candidateSetList,
            Set<String> intersectionOfCandidateRatedUserSets,
            String movieId)
    {
        if (intersectionOfCandidateRatedUserSets.size() == 0) return 0.0;
        double weightedRatingsTotal = 0.0;
        Integer rating;
        for (String candidateUser : intersectionOfCandidateRatedUserSets) {
            rating = userRateMap.get(candidateUser).get(movieId);
            weightedRatingsTotal += rating;
        }
        return weightedRatingsTotal / intersectionOfCandidateRatedUserSets.size();
    }

}
