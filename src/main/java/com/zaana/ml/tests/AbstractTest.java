package com.zaana.ml.tests;

import com.zaana.ml.*;
import com.zaana.ml.Vector;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maytekin on 06.05.2015.
 */
public abstract class AbstractTest {

    static HashMap<String, HashMap<String, Integer>> userRateMap;
    static HashMap<String, HashMap<String, Integer>> itemRateMap;
    static HashMap<String, HashMap<String, Integer>> testDataMap;
    static List<HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>>> vmaps;
    static List<HashMap<Integer, HashMap<String, Set<String>>>> hashTables;
    static Set<String> itemSet;
    static Set<String> userSet;
    static Logger LOG = Logger.getLogger(AbstractTest.class);
    static Logger LOG2 = Logger.getLogger("RESULTS_LOGGER");
    static HashMap<String, String> hashKeyLookupTable;

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
        DataParser.removeDuplicateData(userRateMap, itemRateMap, testDataMap);
        //HashMap<String, PriorityQueue<Map.Entry<String, Double>>> model =
        //        ModelBuild.readModelFromFile(baseUrl+num+".model.ub");
        //HashMap<String, PriorityQueue<Map.Entry<String, Double>>> model2 =
        //        ModelBuild.readModelFromFile(baseUrl+num+".model.ib");

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
        //DataParser.removeDuplicateData(userRateMap, itemRateMap, testDataMap);

    }

    public static void prepareHashTables(String baseUrl, double smoothRun, String seperator, int l, int k) {
        vmaps = new LinkedList<>();
        hashTables = new LinkedList<>();
        hashKeyLookupTable = new HashMap<>();
        for (int j = 0; j < smoothRun; j++) {
            preprocessDataForRecommendation(baseUrl, (j + 1), seperator);
            Set<String> itemSet = itemRateMap.keySet();
            Set<String> userSet = userRateMap.keySet();
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap = Vector.generateHashFunctions(-5, 5, l, k, itemSet);
            HashMap<Integer, HashMap<String, Set<String>>> tables = LSH.buildIndexTables(userRateMap, vmap, l);
            vmaps.add(vmap);
            hashTables.add(tables);

        }
        LOG.info("Hash Tables created...");
    }

    public static void buildAndWriteModel(String baseUrl, double smoothRun, String seperator, int y) {
        for (int j = 0; j < smoothRun; j++) {
            String trainDataFilePath = baseUrl + (j+1);
            preprocessDataForValidation(baseUrl, (j+1), "test", seperator);
            userRateMap = DataParser.getUserRateMap();
            itemRateMap = DataParser.getItemRateMap();
            testDataMap = DataParser.getTestDataMap();
            //ModelBuild.createAndWriteModel(userRateMap, trainDataFilePath + ".model.ub", y);
            ModelBuild.createAndWriteModel(userRateMap, trainDataFilePath + ".model.ib", y, 30);
        }
    }

}
