package com.zaana.ml.recomm;

import java.util.*;

/**
 * Created by maruf on 07/05/15.
 */
public interface LSHReccommenderInterface {

    Set<String> recommendItems(
            HashMap<Integer, HashMap<String,Set<String>>> hashTables,
            HashMap<String, Integer> userRateList,
            HashMap<String, String> hashKeyLookupTable,
            int topN);
}
