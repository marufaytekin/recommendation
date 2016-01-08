package com.zaana.ml.metrics;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maytekin on 02.12.2014.
 */
public final class Recall {

    static Logger LOG = Logger.getLogger(Precision.class);

    private Recall() {}

    public static Double calculateRecall(final Set<String> relevant,
                                         final Set<String> topNRecommendation)
    {
        try {
            Set<String> intersec = new HashSet<>(topNRecommendation);
            intersec.retainAll(relevant);
            double recall = (double) intersec.size() / (double) relevant.size();
            LOG.debug("recall: " + recall);
            return recall;
        } catch (NullPointerException e) {
            LOG.debug(e.getStackTrace());
            return 0.0;
        }
    }
    public static Double getRecall(
            Set<String> topNRecommendation,
            Map.Entry<String, HashMap<String, Integer>> entry)
    {
        //Set<String> retreived = new HashSet<>(topNRecommendation);
        Set<String> relevant = entry.getValue().keySet();
        return calculateRecall(relevant, topNRecommendation);

    }

    public static double getAverageRecall(
            HashMap<String, Set<String>> relevantList,
            HashMap<String, Set<String>> retreivedList)
    {
        double totalRecall = 0.0D;
        Iterator<Map.Entry<String, Set<String>>> iter = relevantList.entrySet()
                .iterator();
        int size = 0;
        Double recall;
        while (iter.hasNext()) {
            Map.Entry<String, Set<String>> entry = iter.next();
            String key = entry.getKey();
            Set<String> relevant = entry.getValue();
            Set<String> retreived = retreivedList.get(key);
            recall = calculateRecall(relevant, retreived);
            if (recall != null) {
                totalRecall += recall;
                size++;
            }
        }

        return totalRecall / size;
    }

}
