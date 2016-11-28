package com.zaana.ml;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.*;

public class IBNNPredictionTest
{

    private static final double delta = 1e-5; // the
                                              // maximum
                                              // delta
                                              // between
    // expected and actual for which
    // both numbers are still
    // considered equal.
    private static HashMap<String, HashMap<String, Integer>> userRateMap;
    private static HashMap<String, HashMap<String, Integer>> testDataMap;

    @Before
    public void testSetup()
    {

        // insertDataInMap( itemID, userID, rating, itemRateMap );
        userRateMap = new HashMap<String, HashMap<String, Integer>>();
        // similarityMatrix = new HashMap<String, LinkedHashMap<String,
        // Double>>( );
        testDataMap = new HashMap<String, HashMap<String, Integer>>();
        // movieId: 1,2,3 //similarity if movie1 to movie2 and 3 is 0.85 and
        // 0.75 respectively.
        // Question what is the predicted rating of movie 1 of user 10: 4.53125;
        LinkedHashMap<String, Double> entry = new LinkedHashMap<String, Double>();
        entry.put("2", 0.85);
        entry.put("3", 0.75);
        // similarityMatrix.put( "1, entry );
        HashMap<String, Integer> testData = new HashMap<String, Integer>();
        testData.put("1", 5);
        testDataMap.put("10", testData);
        // userId=10
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("2", 5);
        map.put("3", 4);
        map.put("1", 4);
        userRateMap.put("10", map);

    }

    @After
    public void testCleanup()
    {
        userRateMap = null;
        testDataMap = null;
    }

    @Test
    public void testPredictItemUserRate()
    {
        // TODO: implement
    }

    @Test
    public void testRunItemBasedNNPredictionOnTestData()
    {

    }

}
