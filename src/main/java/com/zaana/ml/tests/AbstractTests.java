package com.zaana.ml.tests;

import com.zaana.ml.DataParser;
import com.zaana.ml.Main;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by maytekin on 06.05.2015.
 */
public abstract class AbstractTests {

    static HashMap<String, HashMap<String, Integer>> userRateMap;
    static HashMap<String, HashMap<String, Integer>> itemRateMap;
    static HashMap<String, HashMap<String, Integer>> testDataMap;
    static Set<String> itemSet;
    static Set<String> userSet;
    static Logger LOG = Logger.getLogger(AbstractTests.class);
    static Logger LOG2 = Logger.getLogger("RESULTS_LOGGER");

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
    }

}
