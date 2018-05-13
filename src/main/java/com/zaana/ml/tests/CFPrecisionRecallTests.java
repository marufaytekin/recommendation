package com.zaana.ml.tests;

import com.zaana.ml.metrics.Diversity;
import com.zaana.ml.metrics.Novelty;
import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.cf.AbstractCFRecommender;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;

import java.util.*;

/**
 * Created by maruf on 07/05/15.
 */
public class CFPrecisionRecallTests extends AbstractTest {

    static double totalPrecision;
    static double totalRecall;
    static double totalTime;
    static double totalDiversity;
    static double totalAggrDiversity;
    static double totalNovelty;

    /**
     * Runs item-based top-N karypis method.
     */
    public static void runCFRecommendation(
            AbstractCFRecommender recommender, String dataFileBase, String separator,
            double cvFoldNum, int topN, int y)
    {
        LOG.info("Running runItemBasedTopNRecommendation...");
        double overAllTotalTime = 0;
        double overAllPrecision = 0;
        double overAllRecall = 0;
        double overAllDiversity = 0;
        double overAllAggrDiversity = 0;
        double overAllNovelty = 0;
        for (int j = 0; j < cvFoldNum; j++) {
            preprocessDataForRecommendation(dataFileBase, j, separator);
            int size = testDataMap.size();
            LOG.info("Model build started...");
            recommender.buildModel(userRateMap, itemRateMap, y, 30);
            LOG.info("Model build completed...");
            calculateCFPrecisionRecall(userRateMap, testDataMap, recommender, topN);
            overAllPrecision += totalPrecision/size;
            overAllRecall += totalRecall/size;
            overAllDiversity += totalDiversity/size;
            overAllAggrDiversity += totalAggrDiversity;
            overAllNovelty += totalNovelty/size;
            overAllTotalTime += totalTime/size;
        }
        String reccClassName = recommender.getClass().getSimpleName();
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + reccClassName + " Precision");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(reccClassName + "Precision = " + overAllPrecision / cvFoldNum);
        LOG2.info(reccClassName + "Recall = " + overAllRecall / cvFoldNum);
        LOG2.info(reccClassName + "Diversity = " + overAllDiversity / cvFoldNum);
        LOG2.info(reccClassName + "AggrDiversity = " + overAllAggrDiversity / cvFoldNum);
        LOG2.info(reccClassName + "Novelty = " + overAllNovelty / cvFoldNum);
        LOG2.info(reccClassName + "AvgRecommTime = " + overAllTotalTime / cvFoldNum + ";");

    }

    public static void calculateCFPrecisionRecall(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap,
            AbstractCFRecommender recommender, int topN)
    {
        totalPrecision = 0;
        totalRecall = 0;
        totalTime = 0;
        totalDiversity = 0;
        totalAggrDiversity = 0;
        totalNovelty = 0;

        long startTime;
        long endTime;
        Set <String> uniqueItemSet = new HashSet<>();
        for (Map.Entry<String, HashObjObjMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String userId = entry.getKey();
            HashObjObjMap<String, Integer> userRateList = userRateMap.get(userId);
            if (userRateList == null) {
                continue;
            }
            try {
                startTime = System.currentTimeMillis();
                Set<String> retrieved = recommender.recommendItems(userRateMap, userId, topN);
                endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
                if (retrieved.isEmpty()) continue;
                Set<String> relevant = entry.getValue().keySet();
                totalPrecision += Precision.calculatePrecision(relevant, retrieved);
                totalRecall += Recall.calculateRecall(relevant, retrieved);
                totalDiversity += Diversity.intraListDissimilarity(retrieved, itemRateMap, 5);
                totalNovelty += Novelty.novelty(retrieved, userSet, itemSetCount);
                uniqueItemSet.addAll(new HashSet<>(retrieved));
            } catch (NullPointerException e) {
                //LOG.error(e.getLocalizedMessage());
            }
        }
        totalAggrDiversity += uniqueItemSet.size();
    }

}
