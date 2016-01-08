package com.zaana.ml;

import org.apache.log4j.Logger;

/**
 * Created by maytekin on 08.01.2016.
 */
public class PrecisionRecall {
    static Logger LOG = Logger.getLogger(UBLSHPrecisionRecall.class);
    static double candidate_size;
    static double precision;
    static double recall;

    public static double getPrecision() {
        return precision;
    }

    public static double getRecall() {
        return recall;
    }

    public static double getCandidate_size () {
        return candidate_size;
    }

}
