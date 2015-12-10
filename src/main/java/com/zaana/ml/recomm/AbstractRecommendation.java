package com.zaana.ml.recomm;

/**
 * Created by maruf on 07/05/15.
 */
public abstract class AbstractRecommendation {
    static int candidateSetSize;

    public static int getCandidateSetSize() {
        return candidateSetSize;
    }
}
