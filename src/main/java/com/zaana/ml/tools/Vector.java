package com.zaana.ml.tools;

import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public final class Vector
{

    public static HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> generateHashFunctions(
            final int bound1, final int bound2, final int l, final int k,
            Set<String> itemSet)
    {
        // generate l number of hash maps 
        // contain k number of hash functions

        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> map = new HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>>();
        for (int j = 0; j < l; j++) {
            HashMap<Integer, HashMap<String, Integer>> vectors = new HashMap<Integer, HashMap<String, Integer>>();
            for (int i = 0; i < k; i++) {
                HashMap<String, Integer> V = getRandomVector(bound1, bound2,
                        itemSet);
                vectors.put(i, V);
            }
            map.put(j, vectors);
        }

        return map;

    }

    public static HashMap<String, Integer> getRandomVector(final int bound1,
            final int bound2, Set<String> itemSet)
    {
        HashMap<String, Integer> V = new HashMap<String, Integer>();
        int value;
        for (String key : itemSet) {
            value = (int) (Math.random() * (bound2 - bound1) + bound1);
            V.put(key, value);
        }

        return V;
    }

    public static int calculateDotProduct(final HashObjObjMap<String, Integer> V1,
            final HashMap<String, Integer> V2)
    {

        Iterator<Entry<String, Integer>> iter = V1.entrySet().iterator();
        int product = 0;
        while (iter.hasNext()) {
            Entry<String, Integer> entry = iter.next();
            String key1 = entry.getKey();
            Integer value1 = entry.getValue();
            Integer value2 = V2.get(key1);
            product += value1 * value2;
        }
        return product;
    }

}
