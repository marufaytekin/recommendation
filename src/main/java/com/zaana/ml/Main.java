package com.zaana.ml;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;



public final class Main {
    static Logger LOG = Logger.getLogger(Main.class);
    static Logger LOG2 = Logger.getLogger("RESULTS_LOGGER");


    private Main() {
    }

    private static HashMap<String, HashMap<String, Integer>> userRateMap = null;
    private static HashMap<String, HashMap<String, Integer>> itemRateMap = null;
    private static HashMap<String, HashMap<String, Integer>> testDataMap = null;

    static String dataFilePath = "data/100k/ml";
    static String dataFileBase = "data/100k/ml";
    static String trainDataFilePath;
    static String testDataFilePath;
    static final String seperator = "\\t";
    static int simType = 2;
    // “in most real-world situations, a neighborhood of 20 to 50 neighbors
    // seems reasonable” ( Herlocker et al. 2002 ).
    static int kNN = 20;
    static int y = 1; // significance value. Must not be 0!
    static int alpha = 0;
    static int numOfRun = 10;
    static final int smoothRun = 3;
    static int kStep = 5;
    // l: number of bands
    // k: number of hash functions
    static int l = 5;
    static int k = 6;

    public static void main(final String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        String selection = null;
        do {
            System.out.println("");
            System.out.println("Options");
            System.out.println("===================================");
            System.out.println("0 - preprocess train/test data sets");
            System.out.println("1 - Set k-NN ( " + kNN + " )");
            System.out.println("");
            System.out.println("Parameter CV Tests");
            System.out.println("50 - User-based - Prediction vs. k");
            System.out.println("500 - User-based - Prediction 2D (y & k) test");
            System.out.println("51 - Item-based - Prediction vs. k");
            System.out.println("52 - UBLSH - Prediction vs. k");
            System.out.println("57 - UBLSH - Predicton - 2D (Hash Functions & k) test");
            System.out.println("58 - UBLSH - Predicton - 2D (Hash Functions & y) test");
            System.out.println("");
            System.out.println("Experimental Tests");
            System.out.println("10 - Model Build Time - All");
            System.out.println("54 - UBLSH - Prediction - HashTables change ( inc. by 1 )");
            System.out.println("55 - UBLSH - Prediction - HashFunctions change ( inc. by 1 )");
            System.out.println("56 - UBLSH - Predicton - 2D test");




            System.out.println("99 - Exit");
            System.out.println("===================================");
            System.out.println("Enter a command number to execute:");

            selection = scanner.nextLine();
            LOG.info("Selected menu: " + selection);
            runSelection(selection, scanner, 25);

        } while (!selection.equals("99"));

        scanner.close();

    }

    private static void runSelection(final String selection, Scanner scanner, int numOfTestUserPercentage)
    {
        switch (selection) {

            case "0":
                runSelection("50",scanner,5);
                runSelection("500",scanner,5);
                runSelection("51",scanner,5);
                runSelection("52",scanner,5);
                runSelection("57",scanner,5);
                runSelection("58",scanner,5);
//                DataParser.readTrainingDataFile(trainDataFilePath, seperator);
//                DataParser.readTestDataFile(testDataFilePath, seperator);
//                userRateMap = DataParser.getUserRateMap();
//                itemRateMap = DataParser.getItemRateMap();
//                testDataMap = DataParser.getTestDataMap();
                break;
            case "00":
                DataParser.processDataFile(dataFilePath, seperator, 0, 100);
                userRateMap = DataParser.getUserRateMap();
                DataParser.calculateDataSetHistogram(userRateMap);
                break;

            case "1":
                System.out.println("Enter k-NN: ");
                kNN = Integer.parseInt(scanner.nextLine());
                break;
            case "2":
                System.out.println("Enter number of bands ( l ) :");
                l = Integer.parseInt(scanner.nextLine());
                System.out.println("Enter of hash functions ( k ) :");
                k = Integer.parseInt(scanner.nextLine());
                break;
            case "3":
                System.out.println("Enter data file path: ");
                dataFilePath = scanner.nextLine();
                break;

            case "10": //old 8
                runModelBuildTimeTest("UB");
                runModelBuildTimeTest("UBLSH");
                break;

            case "50":
                runCFPredictionSimulation("UB");
                break;
            case "500":
                runCFPredictionSimulation2D("UB");
                break;
            case "51":
                runCFPredictionSimulation("IB");
                break;
            case "52":
                LOG.info("Running User-based LSH Prediction...");
                runLSHPredictionSimulationForK("UBLSH", 0);
                break;
            case "54":
                runLSHPredictionPerformanceTests("UBLSH", "HashTables");
                LOG.info("NOT implemented yet..!");
                break;
            case "55":
                runLSHPredictionPerformanceTests("UBLSH", "HashFunctions");
                LOG.info("NOT implemented yet..!");
                break;
            case "56":
                runLSH2DPredictionTests("UBLSH");
                break;
            case "57":
                runLSH2DHashFunctionsAndParamTests("UBLSH", "k");
                break;
            case "58":
                runLSH2DHashFunctionsAndParamTests("UBLSH", "y");
                break;


            case "99":
                LOG.info("bye...\n");
                break;

            default:
                LOG.info("Command not recognized...\n");
                break;
        }

    }



    private static void runModelBuildTimeTest (String type)
    {
        ArrayList<Long> modelBuildTimeList = new ArrayList<>();
        ArrayList<Integer> numOfUsersList = new ArrayList<>();
        ArrayList<Integer> numOfItemsList = new ArrayList<>();
        long startTime ;
        long endTime;
        long runningTime;
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        for (int i = 1; i <= 10; i++) {
            preprocessDataForModelBuildTest(dataFilePath, seperator, (100-i*10));
            numOfUsersList.add(userRateMap.keySet().size());
            numOfItemsList.add(itemRateMap.keySet().size());
            Set<String> itemSet = DataParser.getItemSet();
            Set<String> userSet = DataParser.getUserSet();
            startTime = System.currentTimeMillis();
            int numOfBands = l;
            if (type == "UB") {
                Similarity.createCosineSimilarityMatrix(userRateMap);
            } else if (type == "IB") {
                Similarity.createCosineSimilarityMatrix(itemRateMap);
            } else if (type == "UBLSH") {
                vmap = Vector.generateHashFunctions(-5, 5, l, k, itemSet);
                startTime = System.currentTimeMillis();
                LSH.buildIndexTables(userRateMap, vmap,
                        numOfBands);
            } else if (type == "IBLSH") {
                vmap = Vector.generateHashFunctions(-5, 5, l, k, userSet);
                startTime = System.currentTimeMillis();
                LSH.buildIndexTables(itemRateMap, vmap,
                        numOfBands);
            } else {
                throw new UnsupportedOperationException("Invalid type for Model build.");
            }
            endTime = System.currentTimeMillis();
            runningTime = (endTime - startTime);
            modelBuildTimeList.add(runningTime);
            LOG.info("modelBuildTimeList: " + modelBuildTimeList);
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case : " + type + " Model Build");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath + ";");
        LOG2.info(type + "numOfUsersList = " + numOfUsersList.toString() + ";");
        LOG2.info(type + "numOfItemsList = " + numOfItemsList.toString() + ";");
        LOG2.info(type + "modelBuildTimeList = " + modelBuildTimeList.toString() + ";");
    }


    private static void runCFPredictionSimulation(String type)
    {
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> runTimeList = new ArrayList<>();
        kNN = 1;
        LOG.info("Running" + type + " PredictionSimulation for k = " + kNN);
        for (int i = 0; i < 10; i++) {
            double mae = 0;
            double runTime = 0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForKFold(dataFileBase, (j+1));
                if (type == "UB") {
                    runTime += UBNNPrediction.runUserBasedNNPredictionOnTestData(userRateMap,
                            testDataMap, simType, kNN, y);
                    mae += MAE.calculateMAE(UBNNPrediction.getOutputList(),
                            UBNNPrediction.getTargetList());
                } else if (type == "IB") {
                    runTime += IBNNPrediction.runItemBasedNNPredictionOnTestData(itemRateMap, userRateMap,
                            testDataMap, simType, kNN, y);
                    mae += MAE.calculateMAE(IBNNPrediction.getOutputList(),
                            IBNNPrediction.getTargetList());
                } else {
                    throw new UnsupportedOperationException("Invalid operation for CF type.");
                }
            }
            LOG.info(type + "MAE = " + mae / smoothRun);
            maeList.add(mae/smoothRun);
            runTimeList.add(runTime/smoothRun);
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


    private static void runCFPredictionSimulation2D(String type)
    {
        ArrayList<Object> runTimeList2D = new ArrayList<>();
        ArrayList<Object> maeList2D = new ArrayList<>();
        kNN = 1;
        y = 1;
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Long> runtimeList = new ArrayList<>();
            ArrayList<Double> maeList = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                long runTime = (long) 0;
                double mae = 0;
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForKFold(dataFileBase, (s+1));
                    if (type == "UB") {
                        runTime += UBNNPrediction.runUserBasedNNPredictionOnTestData(userRateMap,
                                testDataMap, simType, kNN, y);
                        mae += MAE.calculateMAE(UBNNPrediction.getOutputList(),
                                UBNNPrediction.getTargetList());
                    } else if (type == "IB") {
                        runTime += IBNNPrediction.runItemBasedNNPredictionOnTestData(itemRateMap, userRateMap,
                                testDataMap, simType, kNN, y);
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
                y += 5;
            }
            runTimeList2D.add(runtimeList);
            maeList2D.add(maeList);
            kNN += kStep;
            y = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + " k and y 2D ");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info(type + "MaeList2D = " + maeList2D.toString() + ";");
        LOG2.info(type + "RunTimeList2D = " + runTimeList2D.toString() + ";");

    }

    private static void runLSHPredictionSimulationForK(String type, int alpha)
    {
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> runTimeList = new ArrayList<>();
        ArrayList<Double> candidateSetList = new ArrayList<>();
        HashMap<Integer, HashMap<String, Set<String>>> hashTables;
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        int numOfHashFunctions = k;
        int numberOfHashTables = l;
        kNN = 1;
        LOG.info("Running LSH Prediction Simulation for k = " + kNN);
        for (int i = 0; i < 10; i++) {
            double totalMae = 0.0;
            double runTimeTotal = 0;
            double totalCandSize = 0.0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForKFold(dataFileBase, (j+1));
                Set<String> itemSet = itemRateMap.keySet();
                if (type == "UBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numberOfHashTables, numOfHashFunctions, itemSet);
                    hashTables = LSH.buildIndexTables(userRateMap, vmap, numberOfHashTables);
                    runTimeTotal += UBLSHPrediction.runUserBasedLSHPredictionOnTestData(userRateMap,
                            itemRateMap, testDataMap, hashTables, vmap, kNN, alpha, y);
                } else {
                    throw new UnsupportedOperationException("Invalid operation for LSH type.");
                }
                totalMae += MAE.calculateMAE(
                        AbstractPrediction.getOutputList(),
                        AbstractPrediction.getTargetList());

                totalCandSize += UBLSHPrediction.getAvg_candidate_set_size();
            }
            maeList.add(totalMae / smoothRun);
            runTimeList.add(runTimeTotal / smoothRun);
            candidateSetList.add(totalCandSize / smoothRun);
            kNN += kStep;
            LOG.info("k = " + kNN);
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case " + type + " vs. k - Prediction");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info(type + "MaeList = " + maeList.toString());
        LOG2.info(type + "Runtime = " + runTimeList.toString());
        LOG2.info(type + "CandidateSetList = " + candidateSetList.toString());

    }


    private static void runLSH2DPredictionTests(String testType) {
        int numOfBands = 1;
        int numOfHashFunctions = 1;
        ArrayList<Object> runTimeList2D = new ArrayList<>();
        ArrayList<Object> maeList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Long> hashFuncRuntimeList = new ArrayList<>();
            ArrayList<Double> hashFuncMaeList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                long runTime = (long) 0;
                double mae = 0;
                double candidate_set_size = 0;
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForKFold(dataFileBase, (s+1));
                    Set<String> itemSet = itemRateMap.keySet();
                    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                    if (testType == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildIndexTables(userRateMap, vmap,
                                numOfBands);
                        runTime += UBLSHPrediction
                                .runUserBasedLSHPredictionOnTestData(userRateMap,
                                        itemRateMap, testDataMap, hashTables, vmap, kNN, alpha, y);
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                    candidate_set_size += UBLSHPrediction.getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            UBLSHPrediction.getOutputList(),
                            UBLSHPrediction.getTargetList());
                }
                LOG.info("numOfBands:" + numOfBands
                        + " numOfHashFunctions:" + numOfHashFunctions);
                LOG.info(testType + "MAE: " + mae / smoothRun);
                LOG.info(testType + "Runtime: " + runTime / smoothRun);
                hashFuncMaeList.add(mae / smoothRun);
                hashFuncRuntimeList.add(runTime / smoothRun);
                candidate_set_list.add(candidate_set_size / smoothRun);
                numOfHashFunctions += 1;
            }
            runTimeList2D.add(hashFuncRuntimeList);
            maeList2D.add(hashFuncMaeList);
            candidate_set_list2D.add(candidate_set_list);

            numOfBands += 1;
            numOfHashFunctions = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + testType + " 2D ");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info("k = " + kNN);
        LOG2.info(testType + "Mae2D = " + maeList2D.toString() + ";");
        LOG2.info(testType + "Runtime2D = " + runTimeList2D.toString() + ";");
        LOG2.info(testType + "Candidate_Set_List2D = " + candidate_set_list2D.toString() + ";");

    }


    private static void runLSH2DHashFunctionsAndParamTests(String testType, String param) {
        int numOfBands = 1; // set 1 band to measure only hash functions effect
        int numOfHashFunctions = 1;
        ArrayList<Object> runTimeList2D = new ArrayList<>();
        ArrayList<Object> maeList2D = new ArrayList<>();
        ArrayList<Object> candidate_set_list2D = new ArrayList<>();
        if (param == "k") {
            kNN = 1;
        } else if(param == "y") {
            y = 1;
        } else {
            throw new UnsupportedOperationException("Invalid parameter: " + param);
        }
        for (int i = 0; i < numOfRun; i++) {
            ArrayList<Long> hashFuncRuntimeList = new ArrayList<>();
            ArrayList<Double> hashFuncMaeList = new ArrayList<>();
            ArrayList<Double> candidate_set_list = new ArrayList<>();
            for (int j = 0; j < numOfRun; j++) {
                long runTime = (long) 0;
                double mae = 0;
                double candidate_set_size = 0;
                for (int s = 0; s < smoothRun; s++) {
                    preprocessDataForKFold(dataFileBase, (s+1));
                    Set<String> itemSet = itemRateMap.keySet();
                    HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
                    HashMap<Integer, HashMap<String, Set<String>>> hashTables;
                    if (testType == "UBLSH") {
                        vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                        hashTables = LSH.buildIndexTables(userRateMap, vmap,
                                numOfBands);
                        runTime += UBLSHPrediction
                                .runUserBasedLSHPredictionOnTestData(userRateMap,
                                        itemRateMap, testDataMap, hashTables, vmap, kNN, alpha, y);
                    } else {
                        throw new UnsupportedOperationException("Invalid type.");
                    }
                    candidate_set_size += UBLSHPrediction.getAvg_candidate_set_size();
                    mae += MAE.calculateMAE(
                            UBLSHPrediction.getOutputList(),
                            UBLSHPrediction.getTargetList());
                }
                LOG.info("numOfBands:" + numOfBands
                        + " numOfHashFunctions:" + numOfHashFunctions);
                LOG.info("k: " + kNN);
                LOG.info(testType + "MAE: " + mae / smoothRun);
                LOG.info(testType + "Runtime: " + runTime / smoothRun);
                hashFuncMaeList.add(mae / smoothRun);
                hashFuncRuntimeList.add(runTime / smoothRun);
                candidate_set_list.add(candidate_set_size / smoothRun);
                numOfHashFunctions += 1;
            }
            runTimeList2D.add(hashFuncRuntimeList);
            maeList2D.add(hashFuncMaeList);
            candidate_set_list2D.add(candidate_set_list);
            if (param == "k") {
                kNN += kStep;
            } else if(param == "y") {
                y += 5;
            }
            numOfHashFunctions = 1;
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + testType + " 2D - Hash Functions vs. " + param);
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info("k = " + kNN);
        LOG2.info(testType + param + "Mae2D = " + maeList2D.toString() + ";");
        LOG2.info(testType + param + "Runtime2D = " + runTimeList2D.toString() + ";");
        LOG2.info(testType + param + "Candidate_Set_List2D = " + candidate_set_list2D.toString() + ";");

    }


    private static void runLSHPredictionPerformanceTests(String type, String testType)
    {
        int numOfBands;
        int numOfHashFunctions;
        if (testType == "HashFunctions") {
            numOfBands = l;
            numOfHashFunctions = 1;
        } else if (testType == "HashTables"){
            numOfBands = 1;
            numOfHashFunctions = k;
        } else {
            throw new UnsupportedOperationException("Invalid type.");
        }
        ArrayList<Long> runtimeList = new ArrayList<>();
        ArrayList<Double> maeList = new ArrayList<>();
        ArrayList<Double> candidate_set_list = new ArrayList<>();
        HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap;
        HashMap<Integer, HashMap<String, Set<String>>> hashTables;
        for (int i = 0; i < numOfRun; i++) {
            long runTime = (long) 0;
            double mae = 0;
            double candidate_set_size = 0;
            for (int j = 0; j < smoothRun; j++) {
                preprocessDataForKFold(dataFileBase, (j+1));
                Set<String> itemSet = itemRateMap.keySet();
                Set<String> userSet = userRateMap.keySet();
                if (type == "UBLSH") {
                    vmap = Vector.generateHashFunctions(-5, 5, numOfBands, numOfHashFunctions, itemSet);
                    hashTables = LSH.buildIndexTables(userRateMap, vmap,
                            numOfBands);
                    runTime += UBLSHPrediction
                            .runUserBasedLSHPredictionOnTestData(userRateMap,
                                    itemRateMap, testDataMap, hashTables, vmap, kNN, alpha, y);
                } else {
                    throw new UnsupportedOperationException("Invalid type.");
                }
                candidate_set_size = AbstractPrediction
                        .getAvg_candidate_set_size();
                mae += MAE.calculateMAE(
                        AbstractPrediction.getOutputList(),
                        AbstractPrediction.getTargetList());
            }
            maeList.add(mae / smoothRun);
            runtimeList.add(runTime / smoothRun);
            candidate_set_list.add(candidate_set_size / smoothRun);

            LOG.info("numOfBands:" + numOfBands
                    + " numOfHashFunctions:" + numOfHashFunctions);
            LOG.info("Mae: " + mae/smoothRun);
            LOG.info("Runtime: " + runTime/smoothRun);
            if (testType == "HashFunctions") {
                numOfHashFunctions++;
            } else {
                numOfBands++;
            }
        }
        LOG2.info("# ========================================================");
        LOG2.info("# test case: " + type + testType + " Prediction Performance");
        LOG2.info("# ========================================================");
        LOG2.info("fileName = " + dataFilePath);
        LOG2.info("k = " + kNN);
        LOG2.info("numOfHashTables = " + l);
        LOG2.info("numOfHashFunctions = " + k);
        LOG2.info(type + testType+ "MaeList =  " + maeList.toString() + ";");
        LOG2.info(type + testType + "RuntimeList = " + runtimeList.toString()
                + ";");
        LOG2.info(type + "CandidateSetList = " + candidate_set_list
                + ";");
    }

    private static void preprocessDataForKFold(String baseUrl, int k)
    {
        trainDataFilePath = baseUrl + k + ".base";
        testDataFilePath = baseUrl + k + ".test";
        DataParser.readTrainingDataFile(trainDataFilePath, seperator);
        DataParser.readTestDataFile(testDataFilePath, seperator);
        userRateMap = DataParser.getUserRateMap();
        itemRateMap = DataParser.getItemRateMap();
        testDataMap = DataParser.getTestDataMap();
    }


    private static void preprocessDataForModelBuildTest(String _dataFilePath,
                                                        String _seperator, int num)
    {
        final long startTime = System.currentTimeMillis();
        DataParser.processDataFile(_dataFilePath, _seperator, 0, 100);
        userRateMap = DataParser.getUserRateMap();
        itemRateMap = DataParser.getItemRateMap();
        Set<String> itemSet = DataParser.getItemSet();
        Set<String> userSet = DataParser.getUserSet();
        int itemDataPercentage = itemSet.size()/100;
        int userDataPercentage = userSet.size()/100;
        LOG.info("itemRateMap Size:");
        LOG.info(itemRateMap.size());
        LOG.info("userRateMap Size:");
        LOG.info(userRateMap.size());
        Iterator<Map.Entry<String, HashMap<String, Integer>>> userDataIter =
                userRateMap.entrySet().iterator();
        for (int i = 0; i < num * userDataPercentage; i++) {
            userDataIter.next();
            userDataIter.remove();
        }
        Iterator<Map.Entry<String, HashMap<String, Integer>>> itemDataIter =
                itemRateMap.entrySet().iterator();
        for (int i = 0; i < num * itemDataPercentage; i++) {
            itemDataIter.next();
            itemDataIter.remove();
        }
        LOG.info("itemRateMap Size After:");
        LOG.info(itemRateMap.size());
        LOG.info("userRateMap Size After:");
        LOG.info(userRateMap.size());
        final long endTime = System.currentTimeMillis();
        LOG.info("preprocessDataForModelBuildTest completed in " +
                (endTime - startTime) + " ms.");
    }

}
