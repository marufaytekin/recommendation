package com.zaana.ml.tests;

import com.zaana.ml.prediction.IBNNPrediction;
import com.zaana.ml.MAE;
import com.zaana.ml.prediction.UBNNPrediction;

import java.util.ArrayList;

/**
 * Created by maytekin on 06.05.2015.
 */
public class CFPredictionTests extends AbstractTests
{

    public static void runCFPredictionAndKTest(
            String dataFilePath, String dataFileBase, String type,
            int smoothRun, String seperator, int y)
    {
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> runTimeList = new ArrayList<>();
        int kNN = 1;
        LOG.info("Running" + type + " PredictionSimulation for k = " + kNN);
        for (int i = 0; i < 10; i++) {
            double mae = 0;
            double runTime = 0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForValidation(dataFileBase, (j+1), "val", seperator);
                if (type == "UB") {
                    runTime += UBNNPrediction.runUserBasedNNPredictionOnTestData(userRateMap,
                            testDataMap, kNN, y);
                    mae += MAE.calculateMAE(UBNNPrediction.getOutputList(),
                            UBNNPrediction.getTargetList());
                } else if (type == "IB") {
                    runTime += IBNNPrediction.runItemBasedNNPredictionOnTestData(itemRateMap, userRateMap,
                            testDataMap, kNN, y);
                    mae += MAE.calculateMAE(IBNNPrediction.getOutputList(),
                            IBNNPrediction.getTargetList());
                } else {
                    throw new UnsupportedOperationException("Invalid operation for CF type.");
                }
            }
            LOG.info(type + "MAE = " + mae / smoothRun);
            maeList.add(mae/ smoothRun);
            runTimeList.add(runTime/ smoothRun);
            kNN += 3;
            LOG.info("k = " + kNN);
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Prediction");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info(type + "MaeList = " + maeList.toString());
        LOG2.info(type + "Runtime = " + runTimeList.toString());
    }

    public static void runCFPredictionKAndY2DTest(
            String type, String dataFileBase, int numOfRun, long smoothRun, String seperator)
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
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForValidation(dataFileBase, (s + 1), "val", seperator);
                    if (type == "UB") {
                        runTime += UBNNPrediction.runUserBasedNNPredictionOnTestData(userRateMap,
                                testDataMap, kNN, y);
                        mae += MAE.calculateMAE(UBNNPrediction.getOutputList(),
                                UBNNPrediction.getTargetList());
                    } else if (type == "IB") {
                        runTime += IBNNPrediction.runItemBasedNNPredictionOnTestData(itemRateMap, userRateMap,
                                testDataMap, kNN, y);
                        mae += MAE.calculateMAE(IBNNPrediction.getOutputList(),
                                IBNNPrediction.getTargetList());
                    } else {
                        throw new UnsupportedOperationException("Invalid operation for CF type.");
                    }
                }
                LOG.info("k: " + kNN);
                LOG.info("y: " + y);
                LOG.info(type + "Mae2D: " + mae / smoothRun);
                LOG.info(type + "Runtime2D: " + runTime / smoothRun);
                maeList.add(mae / smoothRun);
                runtimeList.add(runTime / smoothRun);
                y += 3;
            }
            runTimeList2D.add(runtimeList);
            maeList2D.add(maeList);
            kNN += 3;
            y = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " k and y 2D ");
        LOG2.info("# ========================================================");
        LOG2.info("dataFileBase = " + dataFileBase);
        LOG2.info(type + "UBMaeKAndYList2D = " + maeList2D.toString() + ";");
        LOG2.info(type + "RunTimeKAndYList2D = " + runTimeList2D.toString() + ";");

    }


    public static void runCFPredictionTests(
            String dataFilePath, String dataFileBase, String type,
            int smoothRun, String seperator, int kNN, int y)
    {
        double mae = 0;
        double runTime = 0;
        for (int j = 0; j < smoothRun; j++) {
            preprocessDataForValidation(dataFileBase, (j+1), "test", seperator);
            if (type == "UB") {
                runTime += UBNNPrediction.runUserBasedNNPredictionOnTestData(userRateMap,
                        testDataMap, kNN, y);
                mae += MAE.calculateMAE(UBNNPrediction.getOutputList(),
                        UBNNPrediction.getTargetList());
            } else if (type == "IB") {
                runTime += IBNNPrediction.runItemBasedNNPredictionOnTestData(itemRateMap, userRateMap,
                        testDataMap, kNN, y);
                mae += MAE.calculateMAE(IBNNPrediction.getOutputList(),
                        IBNNPrediction.getTargetList());
            } else {
                throw new UnsupportedOperationException("Invalid operation for CF type.");
            }
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Prediction");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info(type + "MaeList = " + mae/ smoothRun);
        LOG2.info(type + "Runtime = " + runTime / smoothRun);
    }

}
