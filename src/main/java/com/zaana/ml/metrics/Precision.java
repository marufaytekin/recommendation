package com.zaana.ml.metrics;

import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maytekin on 02.12.2014.
 */
public abstract class Precision {

    static Logger LOG = Logger.getLogger(Precision.class);

    protected Precision() {}

    public static Double calculatePrecision(
            final Set<String> relevant,
            final Set<String> topNRecommendation)
    {
        if (topNRecommendation.size() == 0) return 0.0;
        try {
            Set<String> intersec = new HashSet<>(topNRecommendation);
            intersec.retainAll(relevant);
            double precision = (double) intersec.size() / (double) topNRecommendation.size();
            return precision;
        } catch (NullPointerException e) {
            LOG.debug(e.getStackTrace());
            return 0.0;
        }
    }

    public static Double calculateMeanAveragePrecision(
            final Set<String> relevantList,
            final List<String> topNRecommendation)
    {
        if (topNRecommendation.size() == 0) return 0.0;
        int N = topNRecommendation.size();
        int relevant = relevantList.size();

        try {
            int hits = 0;
            double ap = 0;
            int i = 0;
            for (String itemId : topNRecommendation) {
                if (relevantList.contains(itemId)){
                    hits++;
                    ap += (double) hits / (i+1);
                }
                i++;
            }
            ap = ap / relevant;
            return(ap);
        } catch (NullPointerException e) {
            LOG.debug(e.getStackTrace());
            return 0.0;
        }
    }


    public static Double getPrecision(
            Set<String> topNRecommendation,
            Map.Entry<String, HashObjObjMap<String, Integer>> entry)
    {
        if (topNRecommendation.size() == 0) return 0.0;
        Set<String> relevant = entry.getValue().keySet();
        return calculatePrecision(relevant, topNRecommendation);

    }

    protected static double getAveragePrecision(
            HashMap<String, Set<String>> relevantList,
            HashMap<String, Set<String>> retreivedList)
    {
        double totalPrecision = 0.0D;
        Iterator<Map.Entry<String, Set<String>>> iter = relevantList.entrySet().iterator();
        int size = 0;
        Double precision;
        while (iter.hasNext()) {
            Map.Entry<String, Set<String>> entry = iter.next();
            String key = entry.getKey();
            Set<String> relevant = entry.getValue();
            Set<String> retreived = retreivedList.get(key);
            LOG.debug(entry.toString());
            LOG.debug(retreived);
            LOG.debug(relevant);
            precision = calculatePrecision(relevant, retreived);
            if (precision != null) {
                totalPrecision += precision;
                size++;
            }
        }

        return totalPrecision / size;
    }

}
