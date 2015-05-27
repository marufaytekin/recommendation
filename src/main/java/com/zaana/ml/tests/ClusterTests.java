package com.zaana.ml.tests;

import com.zaana.ml.DataParser;
import com.zaana.ml.similarity.Cosine;

/**
 * Created by maytekin on 27.05.2015.
 */
public class ClusterTests extends AbstractTests{

    public static void runClusterTests(String dataFileBase, String seperator, int y) {
        preprocessDataForValidation(dataFileBase, 1, "val", seperator);
        Cosine.createDistanceMatrix(userRateMap,y);
    }
}
