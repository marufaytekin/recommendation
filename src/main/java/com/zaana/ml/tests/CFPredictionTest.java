package com.zaana.ml.tests;

import com.zaana.ml.MAE;
import com.zaana.ml.prediction.IBKNNPrediction;
import com.zaana.ml.prediction.UBKNNPrediction;

/**
 * Created by maruf on 08/12/15.
 */
public class CFPredictionTest extends AbstractTest {

    public static void runCFPredictionTests(
            String dataFilePath, String dataFileBase, String type,
            int smoothRun, String seperator, int kNN, int y)
    {
        double mae = 0;
        double runTime = 0;
        for (int j = 0; j < smoothRun; j++) {
            preprocessDataForValidation(dataFileBase, (j+1), "test", seperator);
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
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " Prediction");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info(type + "MaeList = " + mae/ smoothRun);
        LOG2.info(type + "Runtime = " + runTime / smoothRun);
    }
}
