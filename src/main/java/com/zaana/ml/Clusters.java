package com.zaana.ml;

import clustering.AverageLinkageStrategy;
import clustering.Cluster;
import clustering.ClusteringAlgorithm;
import clustering.PDistClusteringAlgorithm;
import clustering.visualization.DendrogramPanel;
import com.zaana.ml.similarity.Cosine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maruf on 29/05/15.
 */
public class Clusters {

    public static List<String> getClusterNeighborsWithName(Cluster clusters, String name, int depth) {
        List <Cluster> leafList = clusters.getLeafs();
        Cluster targetNode = getLeafWithName(leafList, name);
        Cluster parent = getParentClusterWithDepth(targetNode, depth);
        List<String> neighbors = parent.getLeafIds();
        int idx = neighbors.indexOf(name);
        neighbors.remove(idx);

        return neighbors;
    }

    public static Cluster buildCluster(HashMap<String, HashMap<String, Integer>> userRateMap, int y) {

        List<String> names = new ArrayList<>(userRateMap.keySet());
        String[] clusterNames = new String[names.size()];
        for (int i = 0; i < names.size(); i++) {
            clusterNames[i] = names.get(i);
        }
        double[][] distanceMatrix = Cosine.createDistanceMatrix(userRateMap, y);
        ClusteringAlgorithm alg = new PDistClusteringAlgorithm();
        Cluster clusters = alg.performClustering(
                distanceMatrix, clusterNames, new AverageLinkageStrategy());

        return clusters;

    }

    private static Cluster getParentClusterWithDepth(Cluster targetNode, int depth) {
        for (int i=0; i<depth; i++) {
            if (targetNode.getParent() == null) return targetNode;
            targetNode = targetNode.getParent();
        }
        return targetNode;
    }

    private static Cluster getLeafWithName(List<Cluster> leafs, String name) {
        for (Cluster leaf: leafs) {
            if (leaf.getName().equals(name)) return leaf;
        }
        return null;
    }

    private static void drawCluster(Cluster cluster) {
        DendrogramPanel dp = new DendrogramPanel();
        dp.setModel(cluster);
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame();
        frame.setTitle("Clusters");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(dp, BorderLayout.CENTER);
        frame.getContentPane().setPreferredSize(new Dimension(2048, 1024));
        frame.pack();
        frame.setVisible(true);
    }
}
