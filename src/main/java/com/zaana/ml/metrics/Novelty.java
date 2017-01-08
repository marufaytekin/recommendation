package com.zaana.ml.metrics;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public final class Novelty
{

    /**
     * u: number of users
     * k_i : the number of users who have collected/rated item i
     *
     * for each item i in top N recommendation list:
     *     calculate its self information I_i = log2(u / k_i)  and add the result to I_total
     * compute mean self information (top-L surprisal/novelty) as I(L) = I_total / N
     *
     *
     * @param topNReccomList
     * @param userSet
     * @param itemSetCount
     * @return mean self information: I(L)
     */
    public static double novelty(Set<String> topNReccomList, Set<String> userSet, HashMap<String, Integer> itemSetCount)
    {
        if (topNReccomList.isEmpty()) return 0.0;
        int u = userSet.size();
        int N = topNReccomList.size();
        Iterator<String> iter = topNReccomList.iterator();
        BigDecimal I_total = new BigDecimal(0);
        while(iter.hasNext()) {
            String itemId = iter.next();
            Integer k_i = itemSetCount.get(itemId);
            BigDecimal I_i = new BigDecimal(Math.log(u/k_i) / Math.log(2));
            I_total = I_total.add(I_i);
        }
        
        return I_total.doubleValue()/N;
    }
    
    
}
