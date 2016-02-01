package com.zaana.ml;

import java.io.IOException;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimilarityTest
{
    private static final double delta = 1e-15; // the
                                               // maximum
                                               // delta
                                               // between
    // expected and actual for which
    // both numbers are still
    // considered equal.

    private static HashMap<String, HashMap<String, Integer>> userRateMap;
    private static HashMap<String, Integer> userItemRates;

    @BeforeClass
    public static void testSetup()
    {
        userRateMap = new HashMap<String, HashMap<String, Integer>>();
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("24", 5);
        map.put("21", 4);
        map.put("23", 4);
        userRateMap.put("10", map);

        map = new HashMap<String, Integer>();
        map.put("24", 1);
        map.put("21", 2);
        map.put("20", 1);
        userRateMap.put("11", map);

        map = new HashMap<String, Integer>();
        map.put("20", 2);
        map.put("21", 5);
        map.put("23", 3);

        userRateMap.put("12", map);

        userItemRates = new HashMap<String, Integer>();
        map = new HashMap<String, Integer>();
        userItemRates.put("20", 1);
        userItemRates.put("21", 4);
    }

    @AfterClass
    public static void testCleanup()
    {
        userRateMap = null;
    }

    @Test
    public void getPCSimilarityMatrixTest() throws IOException
    {
        /*
         * String actual = Similarity.createPCSimilarityMatrix( userRateMap )
         * .toString( );
         * 
         * LinkedHashMap<String, Double> pcSimilarityList = Similarity
         * .getPCSimilarityList( userRateMap, userItemRates );
         * System.out.println( "pcSimilarityList : " +
         * pcSimilarityList.toString( ) ); // final HashMap<String,
         * HashMap<String, Integer>> userRateMap // final HashMap<String,
         * Integer> userItemRates )
         * 
         * String expepcted =
         * "{10={12=-0.5547001962252291, 11=-0.7999999999999996}, 11={12=0.9778024140774095, 10=-0.7999999999999996}, 12={11=0.9778024140774095, 10=-0.5547001962252291}}"
         * ; Assert.assertEquals( expepcted, actual );
         */
    }
    /*
     * @Test public void getJaccardSimilarityMatrixTest( ) throws IOException {
     * String actual = Similarity.getJaccardSimilarityMatrix( userRateMap
     * ).toString( ); String expepcted =
     * "{10={11=0.5, 12=0.5}, 11={10=0.5, 12=0.5}, 12={10=0.5, 11=0.5}}";
     * Assert.assertEquals( expepcted, actual ); }
     */

}
