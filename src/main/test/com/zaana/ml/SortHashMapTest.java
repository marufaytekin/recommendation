package com.zaana.ml;

import java.util.LinkedHashMap;

import com.zaana.ml.SortHashMap;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SortHashMapTest
{

    private static LinkedHashMap<String, Double> unSortedHashMap;
    private static LinkedHashMap<String, Double> sortedHashMapByValues;

    @BeforeClass
    public static void testSetup()
    {

        unSortedHashMap = new LinkedHashMap<String, Double>();
        sortedHashMapByValues = new LinkedHashMap<String, Double>();
        // create unSortedHashMap lists
        unSortedHashMap.put("4", -1.0);
        unSortedHashMap.put("1", 5.0);
        unSortedHashMap.put("3", 2.0);
        unSortedHashMap.put("2", 4.0);

        // create sortedHashMap lists
        sortedHashMapByValues.put("1", 5.0);
        sortedHashMapByValues.put("2", 4.0);
        sortedHashMapByValues.put("3", 2.0);
        sortedHashMapByValues.put("4", -1.0);

    }

    @AfterClass
    public static void testCleanup()
    {
        unSortedHashMap = null;
        sortedHashMapByValues = null;
    }

    @Test
    public void testSortByValues()
    {
        Assert.assertEquals(sortedHashMapByValues.values().toString(),
                SortHashMap.sortByValues(unSortedHashMap).values()
                        .toString());
    }

}
