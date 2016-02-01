package com.zaana.ml.prediction;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by maruf on 31/01/16.
 */
abstract class LSHPrediction {

    public abstract double calculatePrediction(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            List<String> candidateSetList,
            Set<String> intersectionOfCandidateRatedUserSets,
            String movieId);

}
