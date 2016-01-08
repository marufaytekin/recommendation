package com.zaana.ml.tests;

import com.zaana.ml.LSH;
import com.zaana.ml.Vector;

import java.util.*;

/**
 * Created by maruf on 13/12/15.
 */
public class LSHMetricsTest extends AbstractTest{

    public static void runHashFunctionsLSHEvaluation(
            String dataFileBase, int numOfRun, int smoothRun, String separator, int numOfBands, int topN, int y)
    {
        ArrayList<Double> diversityList = new ArrayList<>();
        ArrayList<Double> noveltyList = new ArrayList<>();
        ArrayList<Double> serendipityList = new ArrayList<>();
        ArrayList<Double> aggrDiversityList = new ArrayList<>();
        ArrayList<Double> precisionList = new ArrayList<>();
        ArrayList<Double> recallList = new ArrayList<>();
        ArrayList<Double> candidateSetList = new ArrayList<>();
        ArrayList<Double> topNList = new ArrayList<>();
        ArrayList<Double> evaluatedUsersList = new ArrayList<>();
        ArrayList<Double> hitRatioList = new ArrayList<>();
        ArrayList<Double> kNNSizeList = new ArrayList<>();
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        HashMap<Integer, HashMap<String, Set<String>>> hashTables;

        int numOfHashFunctions = 1;

        for (int i = 0; i < numOfRun; i++) {
            double diversity = 0;
            double novelty = 0;
            double serendipity = 0;
            double aggrDiversity = 0;
            double precision = 0;
            double recall = 0;
            double candidateSet = 0;
            double topNSize = 0;
            double evaluatedUsers = 0;
            double hitRatio = 0;
            double kNNSize = 0;
            long startTime ;
            long endTime;
            long totalTime = 0;
            for (int s = 0; s < smoothRun; s++) {
                preprocessDataForRecommendation(dataFileBase, (s+1), separator, smoothRun, numOfBands, numOfHashFunctions);
                Set<String> itemSet = itemRateMap.keySet();
                Set<String> userSet = userRateMap.keySet();
                vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                hashKeyLookupTable = LSH.getHashKeyLookupTable();
                startTime = System.currentTimeMillis();
                Metrics.calculateLSHMetrics(
                        userRateMap, itemRateMap,testDataMap, hashTables, hashKeyLookupTable,
                        userSet, topN, y);
                endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
                diversity += Metrics.getDiversity();
                novelty += Metrics.getNovelty();
                //serendipity += Metrics.getSerendipity();
                aggrDiversity += Metrics.getAggregateDiversity();
                precision += Metrics.getPrecision();
                recall += Metrics.getRecall();
                candidateSet += Metrics.getCandidateSetSize();
                topNSize += Metrics.getTopNSize();
                //evaluatedUsers += Metrics.getEvaluatedUsers();
                //hitRatio += Metrics.getHitRatio();
                //kNNSize += Metrics.getkNNSize();
            }
            diversityList.add(diversity / smoothRun);
            noveltyList.add(novelty / smoothRun);
            //serendipityList.add(serendipity / smoothRun);
            aggrDiversityList.add(aggrDiversity / smoothRun);
            precisionList.add(precision / smoothRun);
            recallList.add(recall / smoothRun);
            candidateSetList.add(candidateSet / smoothRun);
            topNList.add(topNSize / smoothRun);
            evaluatedUsersList.add(evaluatedUsers / smoothRun);
            hitRatioList.add(hitRatio / smoothRun);
            kNNSizeList.add(kNNSize / smoothRun);
            LOG.info("numOfBands = " + numOfBands);
            LOG.info("numOfHashFunctions = " + numOfHashFunctions);
            LOG.info("Avg Evaluation Time = " + (double) totalTime/smoothRun);
            numOfHashFunctions++;

        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case : LSH Hash Functions Evaluation") ;
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFileBase);
        LOG2.info("numOfHashTables = " + numOfBands);
        LOG2.info("DiversityList = "
                + diversityList.toString() + ";");
        LOG2.info("NoveltyList = "
                + noveltyList.toString() + ";");
        LOG2.info("SerendipityList = "
                + serendipityList.toString() + ";");
        LOG2.info("AggrDiversityList = "
                + aggrDiversityList.toString() + ";");
        LOG2.info("PrecisionList = "
                + precisionList.toString() + ";");
        LOG2.info("RecallList = "
                + recallList.toString() + ";");
        LOG2.info("CandidateSetList = "
                + candidateSetList.toString() + ";");
        LOG2.info("TopNList = "
                + topNList.toString() + ";");
        LOG2.info("EvaluatedUsersList = "
                + evaluatedUsersList.toString() + ";");
        LOG2.info("HitRatioList = "
                + hitRatioList.toString() + ";");
        LOG2.info("KNNSizeList = "
                + kNNSizeList.toString() + ";");


    }


    public static void runHashTablesLSHEvaluation(
            String dataFileBase, int numOfRun, int smoothRun, String separator, int numOfHashFunctions, int topN, int y)
    {
        ArrayList<Double> diversityList = new ArrayList<>();
        ArrayList<Double> noveltyList = new ArrayList<>();
        ArrayList<Double> serendipityList = new ArrayList<>();
        ArrayList<Double> aggrDiversityList = new ArrayList<>();
        ArrayList<Double> precisionList = new ArrayList<>();
        ArrayList<Double> recallList = new ArrayList<>();
        ArrayList<Double> candidateSetList = new ArrayList<>();
        ArrayList<Double> topNList = new ArrayList<>();
        ArrayList<Double> evaluatedUsersList = new ArrayList<>();
        ArrayList<Double> hitRatioList = new ArrayList<>();
        ArrayList<Double> kNNSizeList = new ArrayList<>();
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        HashMap<Integer, HashMap<String, Set<String>>> hashTables;

        int numOfBands = 1;

        for (int i = 0; i < numOfRun; i++) {
            double diversity = 0;
            double novelty = 0;
            double serendipity = 0;
            double aggrDiversity = 0;
            double precision = 0;
            double recall = 0;
            double candidateSet = 0;
            double topNSize = 0;
            double evaluatedUsers = 0;
            double hitRatio = 0;
            double kNNSize = 0;
            long startTime ;
            long endTime;
            long totalTime = 0;
            for (int s = 0; s < smoothRun; s++) {
                preprocessDataForRecommendation(dataFileBase, (s+1), separator, smoothRun, numOfBands, numOfHashFunctions);
                Set<String> itemSet = itemRateMap.keySet();
                Set<String> userSet = userRateMap.keySet();
                vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, userSet);
                hashTables = LSH.buildIndexTables(itemRateMap, vmap, numOfBands);
                hashKeyLookupTable = LSH.getHashKeyLookupTable();
                startTime = System.currentTimeMillis();
                Metrics.calculateLSHMetrics(
                        userRateMap, itemRateMap,testDataMap, hashTables, hashKeyLookupTable,
                        userSet, topN, y);
                endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
                diversity += Metrics.getDiversity();
                novelty += Metrics.getNovelty();
                //serendipity += Metrics.getSerendipity();
                aggrDiversity += Metrics.getAggregateDiversity();
                precision += Metrics.getPrecision();
                recall += Metrics.getRecall();
                candidateSet += Metrics.getCandidateSetSize();
                topNSize += Metrics.getTopNSize();
                //evaluatedUsers += Metrics.getEvaluatedUsers();
                //hitRatio += Metrics.getHitRatio();
                //kNNSize += Metrics.getkNNSize();
            }
            diversityList.add(diversity / smoothRun);
            noveltyList.add(novelty / smoothRun);
            //serendipityList.add(serendipity / smoothRun);
            aggrDiversityList.add(aggrDiversity / smoothRun);
            precisionList.add(precision / smoothRun);
            recallList.add(recall / smoothRun);
            candidateSetList.add(candidateSet / smoothRun);
            topNList.add(topNSize / smoothRun);
            evaluatedUsersList.add(evaluatedUsers / smoothRun);
            hitRatioList.add(hitRatio / smoothRun);
            kNNSizeList.add(kNNSize / smoothRun);
            LOG.info("numOfBands = " + numOfBands);
            LOG.info("numOfHashFunctions = " + numOfHashFunctions);
            LOG.info("Avg Evaluation Time = " + (double) totalTime/smoothRun);
            numOfBands++;

        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case : LSH Hash Tables Evaluation") ;
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFileBase);
        LOG2.info("numOfHashFunctions = " + numOfHashFunctions);
        LOG2.info("DiversityList = "
                + diversityList.toString() + ";");
        LOG2.info("NoveltyList = "
                + noveltyList.toString() + ";");
        LOG2.info("SerendipityList = "
                + serendipityList.toString() + ";");
        LOG2.info("AggrDiversityList = "
                + aggrDiversityList.toString() + ";");
        LOG2.info("PrecisionList = "
                + precisionList.toString() + ";");
        LOG2.info("RecallList = "
                + recallList.toString() + ";");
        LOG2.info("CandidateSetList = "
                + candidateSetList.toString() + ";");
        LOG2.info("TopNList = "
                + topNList.toString() + ";");
        LOG2.info("EvaluatedUsersList = "
                + evaluatedUsersList.toString() + ";");
        LOG2.info("HitRatioList = "
                + hitRatioList.toString() + ";");
        LOG2.info("KNNSizeList = "
                + kNNSizeList.toString() + ";");


    }


}
