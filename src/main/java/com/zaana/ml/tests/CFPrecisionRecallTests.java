package com.zaana.ml.tests;

import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.cf.AbstractCFRecommender;

import java.util.*;

/**
 * Created by maruf on 07/05/15.
 */
public class CFPrecisionRecallTests extends AbstractTest {

    static double totalPrecision;
    static double totalRecall;
    static double totalTime;


    /**
     * Runs item-based top-N karypis method.
     */
    public static void runCFRecommendation(
            AbstractCFRecommender recommender, String dataFileBase, String separator,
            double smoothRun, int topN, int y)
    {
        LOG.info("Running runItemBasedTopNRecommendation...");
        double overAllTotalTime = 0;
        double overAllPrecision = 0;
        double overAllRecall = 0;
        for (int j = 0; j < smoothRun; j++) {
            preprocessDataForRecommendation(dataFileBase, (j + 1), separator);
            int size = testDataMap.size();
            LOG.info("Model build started...");
            recommender.buildModel(userRateMap, itemRateMap, y, 30);
            LOG.info("Model build completed...");
            calculateCFPrecisionRecall(userRateMap, testDataMap, recommender, topN);
            overAllPrecision += totalPrecision/size;
            overAllRecall += totalRecall/size;
            overAllTotalTime += totalTime/size;
        }
        String reccClassName = recommender.getClass().getSimpleName();
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + reccClassName + " Precision");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(reccClassName + "Precision = " + overAllPrecision / smoothRun);
        LOG2.info(reccClassName + "Recall = " + overAllRecall / smoothRun);
        LOG2.info(reccClassName + "AvgRecommTime = " + overAllTotalTime / smoothRun + ";");

    }

    public static void calculateCFPrecisionRecall(
            HashMap<String, HashMap<String, Integer>> userRateMap,
            HashMap<String, HashMap<String, Integer>> testDataMap,
            AbstractCFRecommender recommender, int topN)
    {
        totalPrecision = 0;
        totalRecall = 0;
        totalTime = 0;
        long startTime;
        long endTime;
        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            HashMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null) {
                continue;
            }
            try {
                startTime = System.currentTimeMillis();
                Set<String> retrieved = recommender.recommendItems(userRateMap, userId, topN);
                endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += Precision.calculatePrecision(relevant, retrieved);
                totalRecall += Recall.calculateRecall(relevant, retrieved);
            } catch (NullPointerException e) {
                //LOG.error(e.getLocalizedMessage());
            }
        }

    }

}
