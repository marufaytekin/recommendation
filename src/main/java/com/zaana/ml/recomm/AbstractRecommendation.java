package com.zaana.ml.recomm;

import org.apache.log4j.Logger;

/**
 * Created by maruf on 07/05/15.
 */
public abstract class AbstractRecommendation {

    public static Logger LOG = Logger.getLogger(AbstractRecommendation.class);
    static int candidateSetSize;

    public static int getCandidateSetSize() {
        return candidateSetSize;
    }
}
