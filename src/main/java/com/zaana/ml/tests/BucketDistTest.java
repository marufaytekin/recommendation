package com.zaana.ml.tests;

import com.zaana.ml.lsh.LSH;
import com.zaana.ml.tools.Vector;
import com.zaana.ml.similarity.Cosine;
import com.zaana.ml.tools.SortHashMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;

import java.util.*;

/**
 * Created by maytekin on 24.12.2015.
 */
public class BucketDistTest extends AbstractTest {

    public static void runModelBuildTest (String dataFileBase, String seperator) {
        preprocessDataForValidation(dataFileBase, 1, "test", seperator);
        LOG.info("model build started...");
        Cosine.createDistanceMatrix(userRateMap, 5);
        LOG.info("model build completed...");
    }
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
            vmap = Vector.generateHashFunctions(-5, 5, l, k, itemSet);
            HashObjObjMap<Object, Object> hashTables = LSH.buildModel(userRateMap, vmap, l);
            int size;
            int maxSize = 0;
            HashObjObjMap<String, HashObjSet> hashTable = (HashObjObjMap<String, HashObjSet>) hashTables.get(0);
            for (Map.Entry<String, HashObjSet> bucket : hashTable.entrySet()) {
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
