package com.zaana.ml.tests;

import com.zaana.ml.metrics.Diversity;
import com.zaana.ml.metrics.Novelty;
import com.zaana.ml.metrics.Precision;
import com.zaana.ml.metrics.Recall;
import com.zaana.ml.recomm.lsh.AbstractLSHRecommender;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.set.hash.HashObjSet;

import java.util.*;

/**
 * Created by maruf on 13/12/15.
 */
public class LSHTopNRecommTests extends AbstractTest{

    private static double totalTime;
    private static double precision;
    private static double recall;
    private static double diversity;
    private static double aggrDiversity;
    private static double novelty;
    private static double topNSize;
    private static double candidateItemListSize;
    private static double topNRatio;


    public static void runHashFunctionsLSHEvaluation(
            AbstractLSHRecommender recommender,
            String dataFileBase, String separator,
            int numOfRun, int cvFoldNum, int numOfBands, int topN)
    {
        ArrayList<Double> avgRecommTime = new ArrayList<>();
        ArrayList<Double> precisionList = new ArrayList<>();
        ArrayList<Double> recallList = new ArrayList<>();
        ArrayList<Double> diversityList = new ArrayList<>();
        ArrayList<Double> aggrDiversityList = new ArrayList<>();
        ArrayList<Double> noveltyList = new ArrayList<>();
        ArrayList<Double> topNList = new ArrayList<>();
        ArrayList<Double> topNRatioList = new ArrayList<>();
        ArrayList<Double> avgCandidateItemListSize = new ArrayList<>();
        //ArrayList<Double> avgUniqueItemListSize = new ArrayList<>();
        int numOfHashFunctions = 4;
        for (int i = 0; i < numOfRun; i++) {
            initMetrics();
            for (int s = 0; s < cvFoldNum; s++) {
                preprocessDataForRecommendation(dataFileBase, s, separator);
                recommender.buildModel(userRateMap, itemRateMap, numOfBands, numOfHashFunctions);
                calculateLSHMetrics(userRateSet, testDataMap, recommender,topN);
            }
            precisionList.add(precision/ cvFoldNum);
            recallList.add(recall/ cvFoldNum);
            diversityList.add(diversity/ cvFoldNum);
            aggrDiversityList.add(aggrDiversity/ cvFoldNum);
            noveltyList.add(novelty/ cvFoldNum);
            topNList.add(topNSize/ cvFoldNum);
            avgRecommTime.add(totalTime/ cvFoldNum);
            avgCandidateItemListSize.add(candidateItemListSize/ cvFoldNum);
            topNRatioList.add(topNRatio/ cvFoldNum);
            //avgUniqueItemListSize.add(uniqueItemListSize/smoothRun);
            LOG.info("numOfBands = " + numOfBands);
            LOG.info("numOfHashFunctions = " + numOfHashFunctions);
            LOG.info("Avg Recommendation Time = " + totalTime/ cvFoldNum);
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
        LOG2.info(reccClassName + "HashFunctionsDiversity = "
                + diversityList.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsAggrDiversity = "
                + aggrDiversityList.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsNovelty = "
                + noveltyList.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsTopN = "
                + topNList.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsAvgRecommTime = "
                + avgRecommTime.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsCandidateItemListSize = "
                + avgCandidateItemListSize.toString() + ";");
        LOG2.info(reccClassName + "HashFunctionsTopNRatioListSize = "
                + topNRatioList.toString() + ";");
        //LOG2.info(reccClassName + "HashFunctionsUniqueItemListSize = "
        //        + avgUniqueItemListSize.toString() + ";");

    }


    private static void initMetrics() {
        precision = 0;
        recall = 0;
        topNSize = 0;
        totalTime = 0;
        candidateItemListSize = 0;
        topNRatio = 0;
        diversity = 0;
        aggrDiversity = 0;
        novelty = 0;
    }


    public static void runHashTablesLSHEvaluation(
            AbstractLSHRecommender recommender,
            String dataFileBase, String separator,
            int numOfRun, int cvFoldNum, int numOfHashFunctions, int topN)
    {
        ArrayList<Double> avgRecommTime = new ArrayList<>();
        ArrayList<Double> precisionList = new ArrayList<>();
        ArrayList<Double> recallList = new ArrayList<>();
        ArrayList<Double> diversityList = new ArrayList<>();
        ArrayList<Double> aggrDiversityList = new ArrayList<>();
        ArrayList<Double> noveltyList = new ArrayList<>();
        ArrayList<Double> topNList = new ArrayList<>();
        ArrayList<Double> topNRatioList = new ArrayList<>();
        ArrayList<Double> avgCandidateItemListSize = new ArrayList<>();
        //ArrayList<Double> avgUniqueItemListSize = new ArrayList<>();
        int numOfBands = 4;
        for (int i = 0; i < numOfRun; i++) {
            initMetrics();
            for (int s = 0; s < cvFoldNum; s++) {
                preprocessDataForRecommendation(dataFileBase, s, separator);
                recommender.buildModel(userRateMap, itemRateMap, numOfBands, numOfHashFunctions);
                calculateLSHMetrics(userRateSet, testDataMap,recommender,topN);
            }
            precisionList.add(precision / cvFoldNum);
            recallList.add(recall / cvFoldNum);
            diversityList.add(diversity/ cvFoldNum);
            aggrDiversityList.add(aggrDiversity/ cvFoldNum);
            noveltyList.add(novelty/ cvFoldNum);
            topNList.add(topNSize / cvFoldNum);
            avgRecommTime.add(totalTime/ cvFoldNum);
            avgCandidateItemListSize.add(candidateItemListSize/ cvFoldNum);
            topNRatioList.add(topNRatio/ cvFoldNum);
            //avgUniqueItemListSize.add(uniqueItemListSize/smoothRun);
            LOG.info("numOfBands = " + numOfBands);
            LOG.info("numOfHashFunctions = " + numOfHashFunctions);
            LOG.info("Avg Evaluation Time = " + totalTime/ cvFoldNum);
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
        LOG2.info(reccClassName + "HashTablesDiversity = "
                + diversityList.toString() + ";");
        LOG2.info(reccClassName + "HashTablesAggrDiversity = "
                + aggrDiversityList.toString() + ";");
        LOG2.info(reccClassName + "HashTablesNovelty = "
                + noveltyList.toString() + ";");
        LOG2.info(reccClassName + "HashTablesTopN = "
                + topNList.toString() + ";");
        LOG2.info(reccClassName + "HashTablesAvgRecommTime = "
                + avgRecommTime.toString() + ";");
        LOG2.info(reccClassName + "HashTablesCandidateItemListSize = "
                + avgCandidateItemListSize.toString() + ";");
        LOG2.info(reccClassName + "HashTablesTopNRatioListSize = "
                + topNRatioList.toString() + ";");
    }


    private static void calculateLSHMetrics(
            final HashObjObjMap<Object, Object> userRateSet,
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap,
            AbstractLSHRecommender recommender, int topN)
    {
        double totalCandidateItemList = 0;
        //double totalUniqueItemList = 0;
        double totalPrecision = 0;
        double totalRecall = 0;
        double totalDiversity = 0;
        double totalNovelty = 0;
        int totalTopN = 0;
        int cnt = 0;
        long startTime ;
        long endTime;
        long totalReccTime = 0;
        Set <String> uniqueItemSet = new HashSet<>();
        for (Map.Entry<String, HashObjObjMap<String, Integer>> entry : testDataMap.entrySet()) {
            String targetUserId = entry.getKey();
            HashObjSet <String> ratedItemSet = (HashObjSet<String>) userRateSet.get(targetUserId);
            if (ratedItemSet == null) {
                continue;
            }
            //////////////////////////////////
            startTime = System.currentTimeMillis();
            List<String> candidateList =
                    recommender.getCandidateItemList(userRateMap, userRateSet, targetUserId, ratedItemSet);
            List<String> topNRecommendedItemsList =
                    recommender.recommendItems(targetUserId, candidateList, topN);
            endTime = System.currentTimeMillis();
            //////////////////////////////////
            Set <String> topNRecommendedItemsSet = new HashSet<>(topNRecommendedItemsList);
            totalReccTime += (endTime - startTime);
            if (topNRecommendedItemsList.isEmpty()) continue;
            totalTopN += topNRecommendedItemsList.size();
            totalPrecision += Precision.calculateMeanAveragePrecision(entry.getValue().keySet(), topNRecommendedItemsList);
            totalRecall += Recall.calculateRecall(entry.getValue().keySet(), topNRecommendedItemsSet);
            totalDiversity += Diversity.intraListDissimilarity(topNRecommendedItemsSet, itemRateMap, 5);
            totalNovelty += Novelty.novelty(topNRecommendedItemsSet, userSet, itemSetCount);
            uniqueItemSet.addAll(new HashSet<>(topNRecommendedItemsList));
            totalCandidateItemList += recommender.getCandidateItemListSize();
            //totalUniqueItemList += recommender.getUniqueCandidateItemListSize();
            cnt++;
        }
        LOG.info(String.format("Avg Top-N Rec Time for one user = %s", (double) totalReccTime / cnt));
        LOG.debug(String.format("Test data size: %s, ", testDataMap.entrySet().size()));
        LOG.debug(String.format("Count: %s, ", cnt));
        precision += totalPrecision/cnt;
        recall += totalRecall/cnt;
        diversity += totalDiversity/cnt;
        aggrDiversity += uniqueItemSet.size();
        novelty += totalNovelty/cnt;
        topNSize += totalTopN/cnt;
        totalTime += (double) totalReccTime/cnt;
        candidateItemListSize += totalCandidateItemList/cnt;
        topNRatio += (double)cnt/testDataMap.entrySet().size();
        //uniqueItemListSize += totalUniqueItemList/cnt;

    }

}
