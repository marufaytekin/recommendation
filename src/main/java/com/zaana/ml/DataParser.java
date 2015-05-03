package com.zaana.ml;

import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public final class DataParser
{

    static Logger LOG = Logger.getLogger(DataParser.class);

    private DataParser() {}

    private static HashMap<String, HashMap<String, Integer>> userRateMap;
    private static HashMap<String, HashMap<String, Integer>> itemRateMap;
    private static HashMap<String, HashMap<String, Integer>> testDataMap;
    private static Set<String> itemSet;
    private static Set<String> userSet;
    private static HashMap<String, Integer> itemSetCount;
    private static HashMap<String, Integer> userSetCount;

    public static HashMap<String, Integer> getItemSetCount()
    {
        return itemSetCount;
    }

    public static HashMap<String, Integer> getUserSetCount()
    {
        return userSetCount;
    }

    public static HashMap<String, HashMap<String, Integer>> getUserRateMap()
    {
        return userRateMap;
    }

    public static Set<String> getItemSet()
    {
        return itemSet;
    }

    public static Set<String> getUserSet()
    {
        return userSet;
    }

    public static HashMap<String, HashMap<String, Integer>> getItemRateMap()
    {
        return itemRateMap;
    }

    public static HashMap<String, HashMap<String, Integer>> getTestDataMap()
    {
        return testDataMap;
    }


    public static void processDataFile(final String filePath,
            final String seperator, final double testDataPercentage,
            final double dataSizePercentage)
    {
        userRateMap = new HashMap<>();
        itemRateMap = new HashMap<>();
        testDataMap = new HashMap<>();
        itemSet = new HashSet<>();
        userSet = new HashSet<>();
        itemSetCount = new HashMap<>();
        userSetCount = new HashMap<>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            LOG.error(e.getStackTrace());
        }

        int testDataSampleSize = 0;
        BigDecimal totalDataSampleSize = new BigDecimal("0");
        LOG.info(totalDataSampleSize);
        BigDecimal inc = new BigDecimal("1");
        LOG.info(inc);

        try {
            while (in.ready()) {
                double x = 100 * Math.random();
                double y = 100 * Math.random();
                String line = in.readLine();
                String[] data = line.split(seperator);
                if (data.length > 2) {
                    String userID = data[0];
                    String itemID = data[1];
                    Integer rating = (int) Math.round(Double
                            .parseDouble(data[2]));
                    userSet.add(userID);
                    itemSet.add(itemID);
                    
                    if (x <= testDataPercentage) { // randomly select test data
                                                  // samples
                        insertDataInMap(userID, itemID, rating, testDataMap);
                        testDataSampleSize++;
                        totalDataSampleSize = totalDataSampleSize.add(inc);
                    } else if (y <= dataSizePercentage) {
                        insertDataInMap(userID, itemID, rating, userRateMap);
                        insertDataInMap(itemID, userID, rating, itemRateMap);
                        updateCounter(itemID, itemSetCount);
                        updateCounter(userID, userSetCount);
                        totalDataSampleSize = totalDataSampleSize.add(inc);
                    }
                } else {
                    LOG.debug("Invalid data entry: " + data);
                }
            }
            LOG.info("Data file: " + filePath);
            LOG.info("Total Ratings: " + totalDataSampleSize);
            LOG.info("Selected test data: " + testDataSampleSize + " of "
                    + totalDataSampleSize + " samples");
            computeSparsity(userSet, itemSet, totalDataSampleSize);
        } catch (NumberFormatException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // Do nothing
            }
        }

    }

    public static void readTrainingDataFile(
            final String filePath, final String seperator)
    {
        userRateMap = new HashMap<>();
        itemRateMap = new HashMap<>();

        userSet = new HashSet<>();
        itemSet = new HashSet<>();
        itemSetCount = new HashMap<>();
        userSetCount = new HashMap<>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            LOG.error(e);
        }

        int totalDataSampleSize = 0;

        try {
            while (in.ready()) {
                String line = in.readLine();
                String[] data = line.split(seperator);
                if (data.length < 3) {
                    continue;
                }
                String userID = data[0];
                String itemID = data[1];
                Integer rating = (int) Math.round(Double.parseDouble(data[2]));
                userSet.add(userID);
                itemSet.add(itemID);
                insertDataInMap(userID, itemID, rating, userRateMap);
                insertDataInMap(itemID, userID, rating, itemRateMap);
                updateCounter(itemID, itemSetCount);
                updateCounter(userID, userSetCount);
                totalDataSampleSize++;
            }
        }  catch (NumberFormatException e) {
            LOG.error(e.getStackTrace());
        } catch (IOException e) {
            LOG.error(e.getStackTrace());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Do nothing
                }
            }
        }
        computeSparsity(userSet, itemSet, BigDecimal.valueOf(totalDataSampleSize));
        LOG.info("Total train datasize: " + totalDataSampleSize);
    }

    public static void readTestDataFile(
            final String filePath, final String seperator)
    {
        testDataMap = new HashMap<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            LOG.error(e);
        }

        int totalDataSampleSize = 0;

        try {
            while (in.ready()) {
                String line = in.readLine();
                String[] data = line.split(seperator);
                if (data.length < 3) {
                    continue;
                }
                String userID = data[0];
                String itemID = data[1];
                Integer rating = (int) Math.round(Double.parseDouble(data[2]));
                insertDataInMap(userID, itemID, rating, testDataMap);
                totalDataSampleSize++;
            }
        }  catch (NumberFormatException e) {
            LOG.error(e.getStackTrace());
        } catch (IOException e) {
            LOG.error(e.getStackTrace());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Do nothing
                }
            }
        }
        LOG.info("Total test datasize: " + totalDataSampleSize);
    }

    private static void insertDataInMap(final String id1, final String id2,
            final Integer rating,
            final HashMap<String, HashMap<String, Integer>> dataMap)
    {
        if (dataMap.containsKey(id1)) {
            dataMap.get(id1).put(id2, rating);
        } else {
            HashMap<String, Integer> map = new HashMap<>();
            map.put(id2, rating);
            dataMap.put(id1, map);
        }
    }

    private static void updateCounter(final String id,
            final HashMap<String, Integer> map)
    {

        try {
            Integer cnt = map.get(id);
            map.put(id, cnt + 1);
        } catch (Exception e) {
            map.put(id, 1);
        }

    }

    private static void computeSparsity(Set<String> _userSet,
            Set<String> _itemSet, BigDecimal _totalDataSampleSize)
    {

        Integer userSetSize = _userSet.size();
        Integer itemSetSize = _itemSet.size();
        BigDecimal numOfUsers = new BigDecimal(userSetSize.toString());
        BigDecimal numOfItems = new BigDecimal(itemSetSize.toString());
        BigDecimal sparsity = _totalDataSampleSize.divide(
                numOfUsers.multiply(numOfItems), 4, RoundingMode.HALF_UP);

        //LOG.info("numOfUsers = " + numOfUsers);
        //LOG.info("numOfItems = " + numOfItems);
        //LOG.info("sparsity = " + sparsity.toString());
    
    }


}
