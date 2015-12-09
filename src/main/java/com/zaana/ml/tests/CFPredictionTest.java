package com.zaana.ml.tests;

import com.zaana.ml.MAE;
import com.zaana.ml.prediction.IBNNPrediction;
import com.zaana.ml.prediction.UBNNPrediction;

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
