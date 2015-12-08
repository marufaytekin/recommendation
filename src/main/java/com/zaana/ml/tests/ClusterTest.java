package com.zaana.ml.tests;

import clustering.Cluster;
import com.zaana.ml.Clusters;
import com.zaana.ml.MAE;
import com.zaana.ml.prediction.ClusterPredictionTest;

import java.util.ArrayList;

import static com.zaana.ml.Clusters.buildCluster;
import static com.zaana.ml.Clusters.drawCluster;

/**
 * Created by maytekin on 27.05.2015.
 */
public class ClusterTest extends AbstractTest {

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


    public static void runClusterTests1(String dataFileBase, String seperator, int kNN, int y) {
        preprocessDataForValidation(dataFileBase, 1, "val", seperator);
        Cluster userClusters = buildCluster(userRateMap, y);
        drawCluster(userClusters);
        //ClusterPrediction.runClusterPredictionOnTestData(userClusters, userRateMap, itemRateMap, testDataMap, 7, kNN, y);
        drawCluster(Clusters.getUsersClusterWithDepth(userClusters, "306", 5));


    }


    /**
     * Runs LSH prediction tests against k nearest neighbor parameter.
     * To determine the best k parameter for pre configured LSH model.
     * Number of hash functions and tables are set.
     */
    public static void runClusterPredictionTests(
            String dataFileBase, String seperator,
            int numOfRun, double smoothRun, int kNN, int y) {
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> runTimeList = new ArrayList<>();
        ArrayList<Double> candidateSetList = new ArrayList<>();
        for (int i = 0; i < numOfRun; i++) {
            double totalMae = 0.0;
            double runTimeTotal = 0;
            double totalNbrSize = 0.0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForValidation(dataFileBase, (j + 1), "test", seperator);
                Cluster userClusters = buildCluster(userRateMap, y);
                runTimeTotal += ClusterPredictionTest.runClusterPredictionOnTestData(
                        userClusters, userRateMap, itemRateMap, testDataMap, (i + 1), kNN, y);
                totalMae += MAE.calculateMAE(
                        ClusterPredictionTest.getOutputList(),
                        ClusterPredictionTest.getTargetList());
                totalNbrSize += ClusterPredictionTest.getAvg_candidate_set_size();

            }
            maeList.add(totalMae / smoothRun);
            runTimeList.add(runTimeTotal / smoothRun);
            candidateSetList.add(totalNbrSize / smoothRun);
        }
        LOG2.info("# ========================================================");
        LOG2.info("# Cluster test case  - Prediction");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info("MaeList = " + maeList.toString());
        LOG2.info("Runtime = " + runTimeList.toString());
        LOG2.info("AvgdNbrSize = " + candidateSetList.toString());
    }


}
