package com.zaana.ml.metrics;

import com.zaana.ml.similarity.Cosine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public final class Serendipity
{
    private Serendipity() {}

    /**
     * Serendipity
     * 
     * Hu: Set of items user u already seen (rated)
     * 
     * for each item h that user u already seen (rated)
     *     for each item i in top N recommendation list that recommended to a user u
     *         calculate the distance (1-similarity s) d between i and h
     *         add d to running total dis_total
     * compute serendipity = dis_total / (|Hu|*N)
     * 
     * @param userRateMap
     * @param topNRecom
     * @param userId
     * @return serendipity
     */
    public static Double serendipity(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> itemRateMap,
            Set<String> topNRecom,
            String userId)
    {
        if (topNRecom.isEmpty()) return 0.0;
        Set <String> H_u = userRateMap.get(userId).keySet();
        
        if (!H_u.isEmpty()) {
            Iterator <String> iter = H_u.iterator();
            double dist_total = 0;
            while(iter.hasNext()){
                String h = iter.next();
                Iterator <String> iterTopN = topNRecom.iterator();
                while (iterTopN.hasNext()){
                    String i = iterTopN.next();
                    double s = Cosine.getCosineSimilarity(h, i, itemRateMap, 1);
                    double distance = 1-s; 
                    dist_total += distance;
                }
            }
    
            return dist_total / (H_u.size() * topNRecom.size());
        }
        else return null;
    }

}
