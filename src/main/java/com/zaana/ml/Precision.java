package com.zaana.ml;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maytekin on 02.12.2014.
 */
public abstract class Precision {

    static Logger LOG = Logger.getLogger(Precision.class);

    protected Precision() {}

    protected static Double calculatePrecision(
            final Set<String> relevant,
            final Set<String> retreived, int topN)
    {
        try {
            Set<String> intersec = new HashSet<>(relevant);
            intersec.retainAll(retreived);
            double precision = (double) intersec.size() / topN;
            return precision;
        } catch (NullPointerException e) {
            LOG.debug(e.getStackTrace());
            return 0.0;
        }
    }

    protected static Double getPrecision(
            List<String> topNRecommendation,
            Map.Entry<String, HashMap<String, Integer>> entry, int topN)
    {
        if (topNRecommendation.size() == 0) return 0.0;
        Set<String> retreived = new HashSet<>(topNRecommendation);
        Set<String> relevant = entry.getValue().keySet();
        return calculatePrecision(relevant,
                retreived, topN);

    }

    protected static double getAveragePrecision(
            HashMap<String, Set<String>> relevantList,
            HashMap<String, Set<String>> retreivedList)
    {
        double totalPrecision = 0.0D;
        Iterator<Map.Entry<String, Set<String>>> iter = relevantList.entrySet()
                .iterator();
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
            precision = calculatePrecision(relevant, retreived, size);
            if (precision != null) {
                totalPrecision += precision;
                size++;
            }
        }

        return totalPrecision / size;
    }

}
