package com.zaana.ml.prediction;

import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by maruf on 29/02/16.
 */
public class RandomPrediction extends AbstractPredictionTests {

    public static double runRandomPredictionOnTestData(
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap)
    {
        outputList = new LinkedList<>();
        targetList = new LinkedList<>();
        LOG.info("Running UserBasedNN test...");
        final long startTime = System.currentTimeMillis();
        for (Map.Entry<String, HashObjObjMap<String, Integer>> testDataEntry : testDataMap.entrySet()) {
            String userId = testDataEntry.getKey();
            HashObjObjMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null || userRateList.isEmpty()) {
                continue;
            }
            predictRandomRatingsForTestUsers
                    (testDataEntry, outputList, targetList);
        }

        final long endTime = System.currentTimeMillis();
        double avgRunTime = (double)(endTime - startTime)/outputList.size();
        LOG.info( "UserBasedNN Running time: " + avgRunTime);

        return avgRunTime;

    }

    private static void predictRandomRatingsForTestUsers(
            Map.Entry<String, HashObjObjMap<String, Integer>> testDataEntry,
            LinkedList<Double> outputList,
            LinkedList<Integer> targetList)
    {
        HashObjObjMap<String, Integer> movieRatePair = testDataEntry.getValue();
        for (Map.Entry<String, Integer> entry : movieRatePair.entrySet()) {
            try {
                double prediction = Math.random() * 4 + 1;
                outputList.add(prediction);
                targetList.add(entry.getValue());
            } catch (NullPointerException e) {
                // do nothing
            }
        }
    }
}
