package com.zaana.ml.metrics;

import com.zaana.ml.similarity.Cosine;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.*;

public final class Diversity
{
    private Diversity() {}

    /**
     * Intra List Dissimilarity:
     * for each item i in top N recommendation list
     *     for every other item j in top N recommendation list
     *         calculate distance d (1- similarity s) between i and j   
     *  add d to running total dist_total
     *  compute the average distance = dist_total / N
     * @param topNRecommendedItems
     * @param ratingMap
     * @param y
     * @return diversity
     */
    public static double intraListDissimilarity(
            Set<String> topNRecommendedItems,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> ratingMap,
            int y)
    {

        Iterator<String> iter = topNRecommendedItems.iterator();
        List<String> subList = new ArrayList<>(topNRecommendedItems);
        Double intraListDistTotal = 0.0D;
        int cnt = 0;
        while (iter.hasNext()) {
            String itemId = iter.next();
            subList.remove(itemId);
            Iterator<String> iterSub = subList.iterator();
            while (iterSub.hasNext()) {
                String subItemId = iterSub.next();
                double sim = Cosine.getCosineSimilarity(itemId, subItemId, ratingMap, y);
                double distance = 1 - sim;
                intraListDistTotal += distance;
                cnt++;
            }
        }

        double diversity = intraListDistTotal/cnt;

        if (Double.isNaN(diversity)) {
            return 0;
        }
        
        return diversity;

    }
    

}
