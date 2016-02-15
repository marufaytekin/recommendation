package com.zaana.ml.tests;

import com.zaana.ml.*;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maytekin on 24.12.2015.
 */
public class BucketDistTest extends AbstractTest {

    public static void runBucketDistTest(
            String type, String dataFileBase, String seperator, int smoothRun, int k, int l, int maxElem) {
        HashMap <Integer, Integer>histogram = new HashMap<>();
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        for (int i=0; i <= 10; i++) {
            histogram.put(i, 0); //init historgram
        }
        for (int j = 0; j < smoothRun; j++) {
            preprocessDataForValidation(dataFileBase, (j + 1), type, seperator);
            Set<String> itemSet = itemRateMap.keySet();
            Set<String> userSet = userRateMap.keySet();
            vmap = Vector.generateHashFunctions(-5, 5, l, k, itemSet);
            LSH.buildModel(userRateMap, vmap, l);
            HashMap<Integer, HashMap<String, Set<String>>> hashTable = LSH.getHashTables();
            int size;
            int maxSize = 0;
            for (Map.Entry<String, Set<String>> bucket : hashTable.get(0).entrySet()) {
                size = bucket.getValue().size();
                if (size > maxSize)
                    maxSize = size;
                Integer rate = (int)(((double) size / 533)*10);
                Integer key = Math.min(rate, 10);
                histogram.put(key, histogram.get(key) + 1);
            }
            LinkedHashMap<Integer, Integer> histo = SortHashMap.sortByKeys(histogram);
            LOG.info("Histogram = " + histo);
            LOG.info("numberOfBuckets = " + histo.keySet());
            LOG.info("numberOfItems = " + histo.values());
            LOG.info("maxSize: " + maxSize);
        }
    }


}
