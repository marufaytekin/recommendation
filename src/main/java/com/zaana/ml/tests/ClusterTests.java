package com.zaana.ml.tests;

import com.zaana.ml.DataParser;
import com.zaana.ml.similarity.Cosine;

/**
 * Created by maytekin on 27.05.2015.
 */
public class ClusterTests extends AbstractTests{

    /*String[] names = new String[] { "O1", "O2", "O3", "O4", "O5", "O6" };
    double[][] distances = new double[][] {
            { 0, 1, 9, 7, 11, 14 },
            { 1, 0, 4, 3, 8, 10 },
            { 9, 4, 0, 9, 2, 8 },
            { 7, 3, 9, 0, 6, 13 },
            { 11, 8, 2, 6, 0, 10 },
            { 14, 10, 8, 13, 10, 0 }};

    ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
    Cluster cluster = alg.performClustering(distances, names,
            new AverageLinkageStrategy());
    Alternatively, you can pass a pdist-like matrix containing one row:

    String[] names = new String[] { "O1", "O2", "O3", "O4", "O5", "O6" };
    double[][] pdist = new double[][] {
            {1, 9, 7, 11 ,14 ,4 ,3 ,8 ,10 ,9 ,2 ,8 ,6 ,13 ,10}
    };
    ClusteringAlgorithm alg = new PDistClusteringAlgorithm();
    Cluster cluster = alg.performClustering(pdist, names, new AverageLinkageStrategy());*/
    public static void runClusterTests(String dataFileBase, String seperator, int y) {
        preprocessDataForValidation(dataFileBase, 1, "val", seperator);
        Cosine.createDistanceMatrix(userRateMap, y);
    }
}
