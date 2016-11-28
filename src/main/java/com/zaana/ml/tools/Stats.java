package com.zaana.ml.tools;

import org.apache.commons.math3.stat.StatUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Stats
{

    public static HashMap<String, HashMap<String, Double>> calculateUserMeanVarianceMap(
            final HashMap<String, HashMap<String, Integer>> ratingMap)
    {

        HashMap<String, HashMap<String, Double>> statsMap = new HashMap<String, HashMap<String, Double>>();

        Iterator<Entry<String, HashMap<String, Integer>>> iter = ratingMap
                .entrySet().iterator();

        while (iter.hasNext()) {
            Entry<String, HashMap<String, Integer>> entry = iter.next();
            String userId = entry.getKey();
            Collection<Integer> values = entry.getValue().values();
            double[] ratings = toDoubleArray(values);
            HashMap<String, Double> value = new HashMap<String, Double>();
            value.put("mean", StatUtils.mean(ratings));
            value.put("variance", StatUtils.variance(ratings));
            statsMap.put(userId.toString(), value);
        }

        return statsMap;

    }

    public static double getVectorMean(HashMap<String, Integer> V)
    {

        Collection<Integer> values = V.values();
        double[] ratings = toDoubleArray(values);

        return StatUtils.mean(ratings);
    }

    public static double getVectorVariance(HashMap<String, Integer> V)
    {

        Collection<Integer> values = V.values();
        double[] ratings = toDoubleArray(values);

        return StatUtils.variance(ratings);
    }

    private static double[] toDoubleArray(Collection<Integer> collection)
    {
        double[] array = new double[collection.size()];
        int i = 0;
        for (int rating : collection) {
            array[i++] = rating;
        }
        return array;
    }

}
