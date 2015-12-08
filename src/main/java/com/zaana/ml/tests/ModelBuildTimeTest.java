package com.zaana.ml.tests;

import com.zaana.ml.DataParser;
import com.zaana.ml.LSH;
import com.zaana.ml.similarity.Cosine;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maytekin on 06.05.2015.
 */
public class ModelBuildTimeTest extends AbstractTest {

    public static void runModelBuildTimeTest(
            String type, String dataFilePath, String seperator, int k, int l)
    {
        ArrayList<Long> modelBuildTimeList = new ArrayList<>();
        ArrayList<Integer> numOfUsersList = new ArrayList<>();
        ArrayList<Integer> numOfItemsList = new ArrayList<>();
        long startTime ;
        long endTime;
        long runningTime;
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        for (int i = 1; i <= 10; i++) {
            preprocessDataForModelBuildTest(dataFilePath, seperator, (100-i*10));
            numOfUsersList.add(userRateMap.keySet().size());
            numOfItemsList.add(itemRateMap.keySet().size());
            int numOfBands = l;
            if (type == "UB") {
                startTime = System.currentTimeMillis();
                Cosine.createSimilarityMatrix(userRateMap);
            } else if (type == "IB") {
                startTime = System.currentTimeMillis();
                Cosine.createSimilarityMatrix(itemRateMap);
            } else if (type == "UBLSH") {
                vmap = Vector.generateHashFunctions(-5, 5, l, k, itemSet);
                startTime = System.currentTimeMillis();
                LSH.buildIndexTables(userRateMap, vmap, numOfBands);
            } else if (type == "IBLSH") {
                vmap = Vector.generateHashFunctions(-5, 5, l, k, userSet);
                startTime = System.currentTimeMillis();
                LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
            } else {
                throw new UnsupportedOperationException("Invalid type for Model build.");
            }
            endTime = System.currentTimeMillis();
            runningTime = (endTime - startTime);
            modelBuildTimeList.add(runningTime);
            LOG.info("modelBuildTimeList: " + modelBuildTimeList);
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case : " + type + " Model Build");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath + ";");
        LOG2.info(type + "numOfUsersList = " + numOfUsersList.toString() + ";");
        LOG2.info(type + "numOfItemsList = " + numOfItemsList.toString() + ";");
        LOG2.info(type + "modelBuildTimeList = " + modelBuildTimeList.toString() + ";");
    }


    private static void preprocessDataForModelBuildTest(
            String _dataFilePath, String _seperator, int num)
    {
        final long startTime = System.currentTimeMillis();
        DataParser.processDataFile(_dataFilePath, _seperator, 0, 100);
        userRateMap = DataParser.getUserRateMap();
        itemRateMap = DataParser.getItemRateMap();
        itemSet = DataParser.getItemSet();
        userSet = DataParser.getUserSet();
        int itemDataPercentage = itemSet.size()/100;
        int userDataPercentage = userSet.size()/100;
        LOG.info("itemRateMap Size:");
        LOG.info(itemRateMap.size());
        LOG.info("userRateMap Size:");
        LOG.info(userRateMap.size());
        Iterator<Map.Entry<String, HashMap<String, Integer>>> userDataIter =
                userRateMap.entrySet().iterator();
        for (int i = 0; i < num * userDataPercentage; i++) {
            userDataIter.next();
            userDataIter.remove();
        }
        Iterator<Map.Entry<String, HashMap<String, Integer>>> itemDataIter =
                itemRateMap.entrySet().iterator();
        for (int i = 0; i < num * itemDataPercentage; i++) {
            itemDataIter.next();
            itemDataIter.remove();
        }
        LOG.info("itemRateMap Size After:");
        LOG.info(itemRateMap.size());
        LOG.info("userRateMap Size After:");
        LOG.info(userRateMap.size());
        final long endTime = System.currentTimeMillis();
        LOG.info("preprocessDataForModelBuildTest completed in " +
                (endTime - startTime) + " ms.");
    }


}
