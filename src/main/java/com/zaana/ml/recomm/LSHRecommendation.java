package com.zaana.ml.recomm;

/**
 * Created by maruf on 09/05/15.
 */
public class LSHRecommendation extends AbstractRecommendation {
    static int candidateSetSize;

    public static int getCandidateSetSize() {
        return candidateSetSize;
    }
}
