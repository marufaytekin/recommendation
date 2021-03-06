package com.zaana.ml.tests;

import com.zaana.ml.prediction.IBKNNPrediction;
import com.zaana.ml.metrics.MAE;
import com.zaana.ml.prediction.UBKNNPrediction;

import java.util.ArrayList;

/**
 * Created by maytekin on 06.05.2015.
 *
 * These tests are validation tests for parameter estimation.
 *
 */
public class CFPredictionValidationTest extends AbstractTest
{

    public static void runCFPredictionAndKTest(
            String dataFilePath, String dataFileBase, String type,
            int cvFoldNum, String seperator, int y)
    {
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> runTimeList = new ArrayList<>();
        int kNN = 1;
        LOG.info("Running" + type + " PredictionSimulation for k = " + kNN);
        for (int i = 0; i < 10; i++) {
            double mae = 0;
            double runTime = 0;
            for (int j = 0; j < cvFoldNum; j++) {
                preprocessDataForValidation(dataFileBase, j, seperator);
                if (type == "UB") {
                    runTime += UBKNNPrediction.runUserBasedNNPredictionOnTestData(userRateMap,
                            testDataMap, kNN, y);
                    mae += MAE.calculateMAE(UBKNNPrediction.getOutputList(),
                            UBKNNPrediction.getTargetList());
                } else if (type == "IB") {
                    runTime += IBKNNPrediction.runItemBasedNNPredictionOnTestData(itemRateMap, userRateMap,
                            testDataMap, kNN, y);
                    mae += MAE.calculateMAE(IBKNNPrediction.getOutputList(),
                            IBKNNPrediction.getTargetList());
                } else {
                    throw new UnsupportedOperationException("Invalid operation for CF type.");
                }
            }
            LOG.info(type + "MAE = " + mae / cvFoldNum);
            maeList.add(mae / cvFoldNum);
            runTimeList.add(runTime / cvFoldNum);
            kNN += 3;
            LOG.info("k = " + kNN);
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Prediction and k");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info(type + "MaeList = " + maeList.toString());
        LOG2.info(type + "Runtime = " + runTimeList.toString());
    }

    public static void runCFPredictionAndYTest(
            String dataFilePath, String dataFileBase, String type,
            int cvFoldNum, String seperator, int kNN)
    {
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> runTimeList = new ArrayList<>();
        int y = 1;
        LOG.info("Running" + type + " PredictionSimulation for k = " + kNN);
        for (int i = 0; i < 10; i++) {
            double mae = 0;
            double runTime = 0;
            for (int j = 0; j < cvFoldNum; j++) {
                LOG.info("cv fold: " + (j+1));
                preprocessDataForValidation(dataFileBase, j, seperator);
                if (type == "UB") {
                    runTime += UBKNNPrediction.runUserBasedNNPredictionOnTestData(userRateMap,
                            testDataMap, kNN, y);
                    mae += MAE.calculateMAE(UBKNNPrediction.getOutputList(),
                            UBKNNPrediction.getTargetList());
                } else if (type == "IB") {
                    runTime += IBKNNPrediction.runItemBasedNNPredictionOnTestData(itemRateMap, userRateMap,
                            testDataMap, kNN, y);
                    mae += MAE.calculateMAE(IBKNNPrediction.getOutputList(),
                            IBKNNPrediction.getTargetList());
                } else {
                    throw new UnsupportedOperationException("Invalid operation for CF type.");
                }
            }
            LOG.info(type + "MAE = " + mae / cvFoldNum);
            maeList.add(mae / cvFoldNum);
            runTimeList.add(runTime / cvFoldNum);
            y += 3;
            LOG.info("y = " + y);
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Prediction and y");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info(type + "MaeList = " + maeList.toString());
        LOG2.info(type + "Runtime = " + runTimeList.toString());
    }

    public static void runCFPredictionKAndY2DTest(
            String dataFileBase, String type, int numOfRun, long cvFoldNum, String seperator)
    {
        ArrayList<Object> runTimeList2D = new ArrayList<>();
        ArrayList<Object> maeList2D = new ArrayList<>();
        int kNN = 1;
        int y = 1;
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Long> runtimeList = new ArrayList<>();
            ArrayList<Double> maeList = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                long runTime = (long) 0;
                double mae = 0;
                for (int s = 0; s < cvFoldNum; s++) {
                    LOG.info("cv fold: " + (s+1));
                    preprocessDataForValidation(dataFileBase, s, seperator);
                    if (type == "UB") {
                        runTime += UBKNNPrediction.runUserBasedNNPredictionOnTestData(userRateMap,
                                testDataMap, kNN, y);
                        mae += MAE.calculateMAE(UBKNNPrediction.getOutputList(),
                                UBKNNPrediction.getTargetList());
                    } else if (type == "IB") {
                        runTime += IBKNNPrediction.runItemBasedNNPredictionOnTestData(itemRateMap, userRateMap,
                                testDataMap, kNN, y);
                        mae += MAE.calculateMAE(IBKNNPrediction.getOutputList(),
                                IBKNNPrediction.getTargetList());
                    } else {
                        throw new UnsupportedOperationException("Invalid operation for CF type.");
                    }
                }
                LOG.info("k: " + kNN);
                LOG.info("y: " + y);
                LOG.info(type + "Mae2D: " + mae / cvFoldNum);
                LOG.info(type + "Runtime2D: " + runTime / cvFoldNum);
                maeList.add(mae / cvFoldNum);
                runtimeList.add(runTime / cvFoldNum);
                y += 2;
            }
            runTimeList2D.add(runtimeList);
            maeList2D.add(maeList);
            kNN += 5;
            y = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " k and y 2D ");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(type + "UBMaeKAndYList2D = " + maeList2D.toString() + ";");
        LOG2.info(type + "RunTimeKAndYList2D = " + runTimeList2D.toString() + ";");

    }


}
