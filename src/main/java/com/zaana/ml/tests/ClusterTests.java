package com.zaana.ml.tests;

import clustering.*;
import clustering.visualization.DendrogramPanel;
import com.zaana.ml.similarity.Cosine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        List<String> names = new ArrayList<>(userRateMap.keySet());
        String [] clusterNames = new String[names.size()];
        for(int i = 0; i < names.size(); i++) {
            clusterNames[i] = names.get(i);
        }
        double [][] distanceMatrix = Cosine.createDistanceMatrix(userRateMap, y);
        ClusteringAlgorithm alg = new PDistClusteringAlgorithm();
        Cluster clusters = alg.performClustering(
                distanceMatrix, clusterNames, new AverageLinkageStrategy());
        //ClusterPair user = new ClusterPair();
        //Cluster userCl= user.agglomerate("765");
        //clusters.toConsole(1);
        //System.out.println(clusters.getDistanceValue());
        //System.out.println(clusters.getName());

        List <Cluster> a = clusters.getLeafs();

        Cluster targetNode = getLeafWithName(a, "306");

        Cluster parent = getParentCluster(targetNode, 7);

        System.out.println(parent.countLeafs());
        List<String> neighbors = parent.getLeafIds();
        System.out.println (neighbors.toString());
        DendrogramPanel dp = new DendrogramPanel();
        dp.setModel(parent);
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame();
        frame.setTitle("Clusters");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(dp, BorderLayout.CENTER);
        frame.getContentPane().setPreferredSize(new Dimension(2048, 1024));
        frame.pack();
        frame.setVisible(true);

    }

    private static Cluster getParentCluster(Cluster targetNode, int num) {
        for (int i=0;i<num;i++) {
            if (targetNode.getParent() == null) return targetNode;
            targetNode = targetNode.getParent();
        }
        return targetNode;
    }

    private static Cluster getLeafWithName(List<Cluster> a, String name) {
        for (Cluster cluster: a) {
            if (cluster.getName().equals(name)) return cluster;
        }
        return null;
    }

    private static List<Cluster> getSubClusters(Cluster clusters, int depth) {

        List <Cluster> a = clusters.getChildren();
        return null;
    }


}
