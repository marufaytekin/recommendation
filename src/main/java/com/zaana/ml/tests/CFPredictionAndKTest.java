package com.zaana.ml.tests;

import com.zaana.ml.IBNNPrediction;
import com.zaana.ml.MAE;
import com.zaana.ml.UBNNPrediction;

import java.util.ArrayList;

/**
 * Created by maytekin on 06.05.2015.
 */
public class CFPredictionAndKTest extends AbstractTests{

    public static void runCFPredictionSimulation(
            String dataFilePath, String dataFileBase, String type,
            int smoothRun, String seperator, int kStep, int y)
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
            kNN += kStep;
            LOG.info("k = " + kNN);
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Prediction");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info(type + "MaeList = " + maeList.toString());
        LOG2.info(type + "Runtime = " + runTimeList.toString());
    }

}
