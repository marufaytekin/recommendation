package com.zaana.ml.tests;

import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.lsh.AbstractLSHRecommender;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;

import java.util.*;

/**
 * Created by maruf on 13/12/15.
 */
public class LSHPrecisionRecallTests extends AbstractTest{

    private static double totalTime;
    private static double precision;
    private static double recall;
    private static double topNSize;
    private static double candidateItemListSize;
    private static double uniqueItemListSize;


    public static void runHashFunctionsLSHEvaluation(
            AbstractLSHRecommender recommender,
            String dataFileBase, String separator,
            int numOfRun, int smoothRun, int numOfBands, int topN)
    {
        ArrayList<Double> avgRecommTime = new ArrayList<>();
        ArrayList<Double> precisionList = new ArrayList<>();
        ArrayList<Double> recallList = new ArrayList<>();
        ArrayList<Double> topNList = new ArrayList<>();
        ArrayList<Double> avgCandidateItemListSize = new ArrayList<>();
        ArrayList<Double> avgUniqueItemListSize = new ArrayList<>();
        int numOfHashFunctions = 4;
        for (int i = 0; i < numOfRun; i++) {
            initMetrics();
            for (int s = 0; s < smoothRun; s++) {
                preprocessDataForRecommendation(dataFileBase, (s + 1), separator);
                recommender.buildModel(userRateMap, itemRateMap, numOfBands, numOfHashFunctions);
                calculateLSHMetrics(userRateSet, testDataMap,recommender,topN);
            }
            precisionList.add(precision/smoothRun);
            recallList.add(recall/smoothRun);
            topNList.add(topNSize/smoothRun);
            avgRecommTime.add(totalTime/smoothRun);
            avgCandidateItemListSize.add(candidateItemListSize/smoothRun);
            avgUniqueItemListSize.add(uniqueItemListSize/smoothRun);
            LOG.info("numOfBands = " + numOfBands);
            LOG.info("numOfHashFunctions = " + numOfHashFunctions);
            LOG.info("Avg Recommendation Time = " + totalTime/smoothRun);
            numOfHashFunctions++;
        }
        String reccClassName = recommender.getClass().getSimpleName();
        LOG2.info("# ========================================================");
        LOG2.info("# test case : " + reccClassName + " Hash Functions Evaluation") ;
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFileBase);
        LOG2.info("numOfHashTables = " + numOfBands);
        LOG2.info(reccClassName + "HashFunctionsPrecision = "
                + precisionList.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsPecall = "
                + recallList.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsTopN = "
                + topNList.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsAvgRecommTime = "
                + avgRecommTime.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsCandidateItemListSize = "
                + avgCandidateItemListSize.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsUniqueItemListSize = "
                + avgUniqueItemListSize.toString() + ";");

    }


    private static void initMetrics() {
        precision = 0;
        recall = 0;
        topNSize = 0;
        totalTime = 0;
        candidateItemListSize = 0;
        uniqueItemListSize = 0;
    }


    public static void runHashTablesLSHEvaluation(
            AbstractLSHRecommender recommender,
            String dataFileBase, String separator,
            int numOfRun, int smoothRun, int numOfHashFunctions, int topN)
    {
        ArrayList<Double> avgRecommTime = new ArrayList<>();
        ArrayList<Double> precisionList = new ArrayList<>();
        ArrayList<Double> recallList = new ArrayList<>();
        ArrayList<Double> topNList = new ArrayList<>();
        ArrayList<Double> avgCandidateItemListSize = new ArrayList<>();
        ArrayList<Double> avgUniqueItemListSize = new ArrayList<>();
        int numOfBands = 1;
        for (int i = 0; i < numOfRun; i++) {
            initMetrics();
            for (int s = 0; s < smoothRun; s++) {
                preprocessDataForRecommendation(dataFileBase, (s + 1), separator);
                recommender.buildModel(userRateMap, itemRateMap, numOfBands, numOfHashFunctions);
                calculateLSHMetrics(userRateSet, testDataMap,recommender,topN);
            }
            precisionList.add(precision / smoothRun);
            recallList.add(recall / smoothRun);
            topNList.add(topNSize / smoothRun);
            avgRecommTime.add(totalTime/smoothRun);
            avgCandidateItemListSize.add(candidateItemListSize/smoothRun);
            avgUniqueItemListSize.add(uniqueItemListSize/smoothRun);
            LOG.info("numOfBands = " + numOfBands);
            LOG.info("numOfHashFunctions = " + numOfHashFunctions);
            LOG.info("Avg Evaluation Time = " + totalTime/smoothRun);
            numOfBands++;
        }
        String reccClassName = recommender.getClass().getSimpleName();
        LOG2.info("# ========================================================");
        LOG2.info("# test case : "  + reccClassName + " Hash Tables Evaluation") ;
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFileBase);
        LOG2.info("numOfHashFunctions = " + numOfHashFunctions);
        LOG2.info(reccClassName + "HashTablesPrecision = "
                + precisionList.toString() + ";");
        LOG2.info(reccClassName + "HashTablesRecall = "
                + recallList.toString() + ";");
        LOG2.info(reccClassName + "HashTablesTopN = "
                + topNList.toString() + ";");
        LOG2.info(reccClassName + "HashTablesAvgRecommTime = "
                + avgRecommTime.toString() + ";");
        LOG2.info(reccClassName + "HashTablesCandidateItemListSize = "
                + avgCandidateItemListSize.toString() + ";");
        LOG2.info(reccClassName + "HashTablesUniqueItemListSize = "
                + avgUniqueItemListSize.toString() + ";");

    }


    private static void calculateLSHMetrics(
            final HashObjObjMap<Object, Object> userRateSet,
            final HashMap<String, HashMap<String, Integer>> testDataMap,
            AbstractLSHRecommender recommender, int topN)
    {
        double totalCandidateItemList = 0;
        double totalUniqueItemList = 0;
        double totalPrecision = 0;
        double totalRecall = 0;
        int totalTopN = 0;
        int cnt = 0;
        long startTime ;
        long endTime;
        long totalReccTime = 0;
        for (Map.Entry<String, HashMap<String, Integer>> entry : testDataMap
                .entrySet()) {
            String targetUserId = entry.getKey();
            HashObjSet <String> ratedItemSet = (HashObjSet<String>) userRateSet.get(targetUserId);
            if (ratedItemSet == null) {
                continue;
            }
            startTime = System.currentTimeMillis();
            List<String> candidateList =
                    recommender.getCandidateItemList(userRateMap, userRateSet, targetUserId, ratedItemSet);
            Set<String> topNRecommendedItems =
                    recommender.recommendItems(targetUserId, candidateList, topN);
            endTime = System.currentTimeMillis();
            totalReccTime += (endTime - startTime);

            totalPrecision += Precision
                    .getPrecision(topNRecommendedItems, entry);
            totalRecall += Recall
                    .getRecall(topNRecommendedItems, entry);
            totalTopN += topNRecommendedItems.size();
            totalCandidateItemList += recommender.getCandidateItemListSize();
            totalUniqueItemList += recommender.getUniqueCandidateItemListSize();
            cnt++;
        }
        LOG.info(String.format("Avg Top-N Rec Time for one user = %s", (double) totalReccTime / cnt));
        precision += totalPrecision / cnt;
        recall += totalRecall/cnt;
        topNSize += totalTopN/cnt;
        totalTime += (double) totalReccTime/cnt;
        candidateItemListSize += totalCandidateItemList/cnt;
        uniqueItemListSize += totalUniqueItemList/cnt;

    }

}
