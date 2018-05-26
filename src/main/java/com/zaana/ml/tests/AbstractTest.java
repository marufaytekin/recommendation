package com.zaana.ml.tests;

import com.zaana.ml.utils.DataParser;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maytekin on 06.05.2015.
 */
public abstract class AbstractTest {

    static HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap;
    static HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap;
    static HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap;
    static HashObjObjMap<Object, Object> userRateSet;
    static HashObjObjMap<Object, Object> itemRateSet;
    static HashObjObjMap<Object, Object> testRateSet;
    static Set<String> itemSet;
    static Set<String> userSet;
    static HashMap<String, Integer> itemSetCount;
    static Logger LOG = Logger.getLogger(AbstractTest.class);
    static Logger LOG2 = Logger.getLogger("RESULTS_LOGGER");
    static HashObjObjMap<Object, Object> hashKeyLookupTable;


    public static void preprocessDataForValidation(String baseUrl, int cvFoldNum, String seperator)
    {
        DataParser.readTrainingDataFile(baseUrl, cvFoldNum, seperator);
        DataParser.readTestDataFile(baseUrl, cvFoldNum, seperator);
        userRateMap = DataParser.getUserRateMap();
        itemRateMap = DataParser.getItemRateMap();
        testDataMap = DataParser.getTestDataMap();
    }


    public static void preprocessDataForRecommendation(String baseUrl, int cvFoldNum, String seperator)
    {
        DataParser.readTrainingDataFile(baseUrl, cvFoldNum, seperator);
        DataParser.readTestDataFile(baseUrl, cvFoldNum, seperator);
        userRateMap = DataParser.getUserRateMap();
        itemRateMap = DataParser.getItemRateMap();
        testDataMap = DataParser.getTestDataMap();
        userRateSet = DataParser.getUserRateSet();
        itemRateSet = DataParser.getItemRateSet();
        testRateSet = DataParser.getTestRateSet();
        userSet = userRateMap.keySet();
        itemSet = itemRateMap.keySet();
        itemSetCount = getCounter(itemRateMap);
    }


    private static HashMap<String, Integer> getCounter(HashObjObjMap<String, HashObjObjMap<String, Integer>> rateMap)

    {
        HashMap<String, Integer> map = new HashMap<>();

        for (Map.Entry<String, HashObjObjMap<String, Integer>> entry  : rateMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue().size());
        }
        return map;
    }


}
