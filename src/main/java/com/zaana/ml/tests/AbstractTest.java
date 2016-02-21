package com.zaana.ml.tests;

import com.zaana.ml.*;
import com.zaana.ml.Vector;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maytekin on 06.05.2015.
 */
public abstract class AbstractTest {

    static HashMap<String, HashMap<String, Integer>> userRateMap;
    static HashMap<String, HashMap<String, Integer>> itemRateMap;
    static HashObjObjMap<Object, Object> userRateSet;
    static HashObjObjMap<Object, Object> itemRateSet;
    static HashObjObjMap<Object, Object> testRateSet;
    static HashMap<String, HashMap<String, Integer>> testDataMap;
    static List<HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>>> vmaps;
    static List<HashMap<Integer, HashMap<String, Set<String>>>> hashTables;
    static Set<String> itemSet;
    static Set<String> userSet;
    static Logger LOG = Logger.getLogger(AbstractTest.class);
    static Logger LOG2 = Logger.getLogger("RESULTS_LOGGER");
    static HashObjObjMap<Object, Object> hashKeyLookupTable;


    public static void preprocessDataForValidation(
            String baseUrl, int num, String type, String seperator)
    {
        String trainDataFilePath = baseUrl + num + ".base";
        String testDataFilePath = baseUrl + num + "." + type;
        DataParser.readTrainingDataFile(trainDataFilePath, seperator);
        DataParser.readTestDataFile(testDataFilePath, seperator);
        userRateMap = DataParser.getUserRateMap();
        itemRateMap = DataParser.getItemRateMap();
        testDataMap = DataParser.getTestDataMap();
        //DataParser.removeDuplicateData(userRateMap, itemRateMap, testDataMap);
    }


    public static void preprocessDataForRecommendation(
            String baseUrl, int num, String seperator)
    {
        String trainDataFilePath = baseUrl + num + ".recomm.base";
        String testDataFilePath = baseUrl + num + ".recomm.test";
        DataParser.readTrainingDataFile(trainDataFilePath, seperator);
        DataParser.readTestDataFile(testDataFilePath, seperator);
        userRateMap = DataParser.getUserRateMap();
        itemRateMap = DataParser.getItemRateMap();
        testDataMap = DataParser.getTestDataMap();
        userRateSet = DataParser.getUserRateSet();
        itemRateSet = DataParser.getItemRateSet();
        testRateSet = DataParser.getTestRateSet();
        //DataParser.removeDuplicateData(userRateMap, itemRateMap, testDataMap);

    }

}
