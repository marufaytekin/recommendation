package com.zaana.ml.tests;

import com.zaana.ml.recomm.lsh.AbstractLSHReccommender;

import java.util.*;

/**
 * Created by maruf on 13/12/15.
 */
public class LSHPrecisionRecallTest extends AbstractTest{

    private static long totalTime;
    private static double precision;
    private static double recall;
    private static double topNSize;
    private static double candidateItemListSize;
    private static double uniqueItemListSize;


    public static void runHashFunctionsLSHEvaluation(
            AbstractLSHReccommender recommender, String dataFileBase, String separator,
            int numOfRun, int smoothRun, int numOfBands, int topN)
    {
        ArrayList<Double> avgRecommTime = new ArrayList<>();
        ArrayList<Double> precisionList = new ArrayList<>();
        ArrayList<Double> recallList = new ArrayList<>();
        ArrayList<Double> topNList = new ArrayList<>();
        ArrayList<Double> avgCandidateItemListSize = new ArrayList<>();
        ArrayList<Double> avgUniqueItemListSize = new ArrayList<>();
        int numOfHashFunctions = 6;
        for (int i = 0; i < numOfRun; i++) {
            precision = 0;
            recall = 0;
            topNSize = 0;
            totalTime = 0;
            candidateItemListSize = 0;
            uniqueItemListSize = 0;
            for (int s = 0; s < smoothRun; s++) {
                preprocessDataForRecommendation(dataFileBase, (s + 1), separator);
                recommender.buildModel(userRateMap, itemRateMap, numOfBands, numOfHashFunctions);
                calculateLSHMetrics(recommender, topN);
            }
            precisionList.add(precision/smoothRun);
            recallList.add(recall/smoothRun);
            topNList.add(topNSize/smoothRun);
            avgRecommTime.add((double) totalTime/smoothRun);
            avgCandidateItemListSize.add(candidateItemListSize/smoothRun);
            avgUniqueItemListSize.add(uniqueItemListSize/smoothRun);
            LOG.info("numOfBands = " + numOfBands);
            LOG.info("numOfHashFunctions = " + numOfHashFunctions);
            LOG.info("Avg Recommendation Time = " + (double) totalTime/smoothRun);
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


    public static void runHashTablesLSHEvaluation(
            AbstractLSHReccommender recommender, String dataFileBase, String separator,
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
            precision = 0;
            recall = 0;
            topNSize = 0;
            totalTime = 0;
            candidateItemListSize = 0;
            uniqueItemListSize = 0;
            for (int s = 0; s < smoothRun; s++) {
                preprocessDataForRecommendation(dataFileBase, (s + 1), separator);
                recommender.buildModel(userRateMap, itemRateMap, numOfBands, numOfHashFunctions);
                calculateLSHMetrics(recommender, topN);
            }
            precisionList.add(precision / smoothRun);
            recallList.add(recall / smoothRun);
            topNList.add(topNSize / smoothRun);
            avgRecommTime.add((double) totalTime/smoothRun);
            avgCandidateItemListSize.add(candidateItemListSize/smoothRun);
            avgUniqueItemListSize.add(uniqueItemListSize/smoothRun);
            LOG.info("numOfBands = " + numOfBands);
            LOG.info("numOfHashFunctions = " + numOfHashFunctions);
            LOG.info("Avg Evaluation Time = " + (double) totalTime/smoothRun);
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

    private static void calculateLSHMetrics(AbstractLSHReccommender recommender, int topN) {
        Metrics.calculateLSHMetrics(userRateMap, testDataMap,recommender,topN);
        precision += Metrics.getPrecision();
        recall += Metrics.getRecall();
        topNSize += Metrics.getTopNSize();
        totalTime += Metrics.getAvgRecommTime();
        candidateItemListSize += Metrics.getCandidateItemListSize();
        uniqueItemListSize += Metrics.getUniqueItemListSize();
    }


}
