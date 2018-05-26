package com.zaana.ml.utils;

import net.openhft.koloboke.collect.ObjIterator;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import net.openhft.koloboke.collect.set.hash.HashObjSets;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public final class DataParser
{

    static Logger LOG = Logger.getLogger(DataParser.class);
    static Logger LOG2 = Logger.getLogger("RESULTS_LOGGER");

    private DataParser() {}

    private static HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap;
    private static HashObjObjMap<String, HashObjObjMap<String, Integer>> itemRateMap;
    private static HashObjObjMap<String, HashObjObjMap<String, Integer>> testDataMap;
    static HashObjObjMap<Object, Object> userRateSet;
    static HashObjObjMap<Object, Object> itemRateSet;
    static HashObjObjMap<Object, Object> testRateSet;
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

    public static HashObjObjMap<String, HashObjObjMap<String, Integer>> getUserRateMap()
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

    public static HashObjObjMap<String, HashObjObjMap<String, Integer>> getItemRateMap()
    {
        return itemRateMap;
    }

    public static HashObjObjMap<String, HashObjObjMap<String, Integer>> getTestDataMap()
    {
        return testDataMap;
    }

    public static HashObjObjMap<Object, Object> getUserRateSet() {
        return userRateSet;
    }

    public static HashObjObjMap<Object, Object> getItemRateSet() {
        return itemRateSet;
    }

    public static HashObjObjMap<Object, Object> getTestRateSet() {
        return testRateSet;
    }

    public static void processDataFile(final String filePath,
            final String seperator, final double testDataPercentage,
            final double dataSizePercentage)
    {
        userRateMap = HashObjObjMaps.getDefaultFactory().newMutableMap();
        itemRateMap = HashObjObjMaps.getDefaultFactory().newMutableMap();
        testDataMap = HashObjObjMaps.getDefaultFactory().newMutableMap();
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
            while (in != null && in.ready()) {
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
                } else LOG.debug(String.format("Invalid data entry: %s", Arrays.toString(data)));
            }
            LOG.info(String.format("Data file: %s", filePath));
            LOG.info(String.format("Total Ratings: %s", totalDataSampleSize));
            LOG.info(String.format("Selected test data: %d of %s samples", testDataSampleSize, totalDataSampleSize));
            computeSparsity(userSet, itemSet, totalDataSampleSize);
        } catch (NumberFormatException | IOException e) {
            LOG.error(e);
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (IOException | NullPointerException e) {
                // Do nothing
            }
        }

    }

    public static void readTrainingDataFile(
            final String filePath, int cvFoldNum, final String seperator)
    {
        userRateMap = HashObjObjMaps.getDefaultFactory().newMutableMap();
        itemRateMap = HashObjObjMaps.getDefaultFactory().newMutableMap();
        userRateSet = HashObjObjMaps.getDefaultFactory().newMutableMap();
        itemRateSet = HashObjObjMaps.getDefaultFactory().newMutableMap();

        userSet = new HashSet<>();
        itemSet = new HashSet<>();
        itemSetCount = new HashMap<>();
        userSetCount = new HashMap<>();

        BufferedReader in = null;
        int totalDataSampleSize = 0;

        for (int i=0; i <10; i++) {
            try {
                if (i == cvFoldNum) {
                    continue;
                }
                in = new BufferedReader(new FileReader(filePath + i));
                while (in.ready()) {
                    String line = in.readLine();
                    String[] data = line.split(seperator);
                    if (data.length < 3) continue;
                    String userID = data[0];
                    String itemID = data[1];
                    Integer rating = (int) Math.round(Double.parseDouble(data[2]));
                    userSet.add(userID);
                    itemSet.add(itemID);
                    insertDataInMap(userID, itemID, rating, userRateMap);
                    insertDataInMap(itemID, userID, rating, itemRateMap);
                    insertDataInSet(userID, itemID, userRateSet);
                    insertDataInSet(itemID, userID, itemRateSet);
                    updateCounter(itemID, itemSetCount);
                    updateCounter(userID, userSetCount);
                    totalDataSampleSize++;
                }
            }
            catch (NumberFormatException | IOException e) {
                LOG.error(e.getStackTrace());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        LOG.error(e.getStackTrace());
                    }
                }
            }
        }
        computeSparsity(userSet, itemSet, BigDecimal.valueOf(totalDataSampleSize));
        LOG.info("Total train datasize: " + totalDataSampleSize);
    }

    public static void readTestDataFile(
            final String filePath, int cvFoldNum, final String seperator)
    {
        testDataMap = HashObjObjMaps.getDefaultFactory().newMutableMap();
        testRateSet =  HashObjObjMaps.getDefaultFactory().newMutableMap();
        BufferedReader in = null;

        int totalDataSampleSize = 0;

        try {
            in = new BufferedReader(new FileReader(filePath + cvFoldNum));
            while (in != null && in.ready()) {
                String line = in.readLine();
                String[] data = line.split(seperator);
                if (data.length < 3) continue;
                String userID = data[0];
                String itemID = data[1];
                Integer rating = (int) Math.round(Double.parseDouble(data[2]));
                insertDataInMap(userID, itemID, rating, testDataMap);
                insertDataInSet(userID,itemID,testRateSet);
                totalDataSampleSize++;
            }
        }  catch (NumberFormatException | IOException e) {
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
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> dataMap)
    {
        if (dataMap.containsKey(id1)) {
            dataMap.get(id1).put(id2, rating);
        } else {
            HashObjObjMap<String, Integer> map = HashObjObjMaps.getDefaultFactory().newMutableMap();
            map.put(id2, rating);
            dataMap.put(id1, map);
        }
    }

    private static void insertDataInSet(final String id1, final String id2,
                                        final HashObjObjMap<Object, Object> dataSet)
    {
        if (dataSet.containsKey(id1)) {
            HashObjSet<String> s = (HashObjSet<String>) dataSet.get(id1);
            s.add(id2);
        } else {
            HashObjSet <String> s = HashObjSets.getDefaultFactory().newMutableSet();
            s.add(id2);
            dataSet.put(id1, s);
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

        LOG.info("numOfUsers = " + numOfUsers);
        LOG.info("numOfItems = " + numOfItems);
        LOG.info("sparsity = " + sparsity.toString());
    
    }

    public static void calculateDataSetHistogram(HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap)
    {
        ObjIterator<Map.Entry<String, HashObjObjMap<String, Integer>>> iter =
                userRateMap.entrySet().iterator();
        HashMap <Integer, Integer>histogram = new HashMap<>();
        for (int i=0; i <= 10; i++) {
            histogram.put(i,0); //init historgram
        }
        while (iter.hasNext()) {
            Map.Entry<String, HashObjObjMap<String, Integer>> entry = iter.next();
            int ratings = entry.getValue().size();
            Integer rate = (ratings / 10);
            Integer key = Math.min(rate, 10);

            histogram.put(key, histogram.get(key) + 1);
        }

        Iterator<Map.Entry<Integer, Integer>> iterator = histogram.entrySet().iterator();
        int total = 0;
        while (iterator.hasNext()) {
            total += iterator.next().getValue();
        }
        LinkedHashMap<Integer, Integer> histo = SortHashMap.sortByKeys(histogram);
        LOG2.info("Total number of Ratings: " + total);
        LOG2.info("Histogram = " + histo);
        LOG2.info("numberOfRatings = " + histo.keySet());
        LOG2.info("numberOfUsers = " + histo.values());

    }



}
