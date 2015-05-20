package com.zaana.ml;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;


/**
 * LSH is the class that implements Locality Sensitive Hashing algorithm for
 * rating data sets of CF.
 * 
 * @author Maruf Aytekin
 * @version %I%, %G%
 * 
 */
public final class LSH {

    static Logger LOG = Logger.getLogger(LSH.class);
    
    static double avg_bucket_size;

    public double getAvg_bucket_size()
    {
        return avg_bucket_size;
    }

    /**
     * This method creates and builds the LSH hash tables and hash functions.
     * Hash tables considered as bands in LSH algorithm. Hash tables stored in
     * class variable hashTables. Hash functions are created as random rating
     * vectors and stored in class variable vmap. It hashes the users/items to
     * the hash tables with the computed hash keys. This method is used for both
     * user based and item based LSH implementation.
     * 
     * @param ratingMap
     *            useritem/itemuser rating map
     *
     * @param l
     *            number of bands
     * @return hashTables
     * 
     */
    public static HashMap<Integer,HashMap<String,Set<String>>> buildIndexTables(
            HashMap<String, HashMap<String, Integer>> ratingMap,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            int l) {

        long startTime = System.currentTimeMillis();
        HashMap<Integer, HashMap<String, Set<String>>> hashTables = generateHashTables(l);
        Iterator<Entry<String, HashMap<String, Integer>>> iter = ratingMap
                .entrySet().iterator();

        while (iter.hasNext()) {
            Entry<String, HashMap<String, Integer>> entry = iter.next();
            String K = entry.getKey();
            HashMap<String, Integer> V = entry.getValue();
            for (int hashTableNum = 0; hashTableNum < l; hashTableNum++) {
                HashMap<String, Set<String>> hashTable = hashTables
                        .get(hashTableNum);
                String hashKey = generateHashKeyForVector(vmap, V, hashTableNum);
                insertItemInHashTable(hashKey, K, hashTable);
            }
        }

        long endTime = System.currentTimeMillis();
        avg_bucket_size = avg_num_of_buckets(hashTables);

        LOG.info("LSH Index Tables generated in " + (endTime - startTime)
                + " ms ...");
        LOG.info("Avg number of buckets : " + avg_bucket_size);

        return hashTables;

    }

    /**
     * This method calculates candidate set for a user/item based on its
     * ratings. Generates hashkey for each hash table(band) and queries the
     * candidates based on generated hashkey and bulds the candidate set.
     * for each band(hashtable) in hash tables:
     *     generate hashkey for userId,
     *     retreive hashkey and value set,
     *     append value set to candidate set.
     * 
     * @param userId
     * @param userRates
     *            rate vector of the userId
     * @return candidate set for userId
     */
    public static Set<String> getCandidateSet(
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            String userId,
            HashMap<String, Integer> userRates) {
        Set<String> candidateSet = new HashSet<>();
        for (int hashTableNum = 0; hashTableNum < hashTables.size(); hashTableNum++) {
            String hashKey = generateHashKeyForVector(vmap, userRates,
                    hashTableNum);
            Set<String> candidates = hashTables.get(
                    Integer.valueOf(hashTableNum)).get(hashKey);
            candidateSet.addAll(candidates);
        }
        candidateSet.remove(userId);

        return candidateSet;
    }



    /**
     * Returns candidate sets with frequency as an ArrayList.*/
    public static List<String> getCandidateSetsWithFrequency(
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            HashMap<String, Integer> userRates) {

        List<String> candidateSets = new ArrayList();

        for (int hashTableNum = 0; hashTableNum < hashTables.size(); hashTableNum++) {
            String hashKey = generateHashKeyForVector(vmap, userRates,
                    hashTableNum);
            Set<String> candidates = hashTables.get(
                    Integer.valueOf(hashTableNum)).get(hashKey);
            candidateSets.addAll(candidates);
        }

        return candidateSets;
    }


    /**
     * This method calculates candidate set for a user/item based on its
     * ratings. Generates hashkey for each hash table(band) and queries the
     * candidates based on generated hashkey and selects the shared candidates
     * in all hash tables.
     *
     * @param userId
     * @param userRates
     *            rate vector of the userId
     * @return candidate set for userId
     */
    public static Set<String> getCommonCandidatesInAllHashTables(
            HashMap<Integer, HashMap<String, Set<String>>> hashTables,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            String userId,
            HashMap<String, Integer> userRates) {
        String hashKey = generateHashKeyForVector(vmap, userRates, 0);
        Set<String> candidateSet = new HashSet<>(hashTables.get(0).get(hashKey));
        for (int hashTableNum = 1; hashTableNum < hashTables.size(); hashTableNum++) {
            hashKey = generateHashKeyForVector(vmap, userRates,
                    hashTableNum);
            Set<String> candidates = hashTables.get(
                    Integer.valueOf(hashTableNum)).get(hashKey);
            candidateSet.retainAll(candidates);
        }
        candidateSet.remove(userId);

        return candidateSet;
    }


    /**
     * This method generates empty hash tables for LSH to be used as bands. It
     * assigns generated hash tables to class variable hashTables.
     * 
     * @param l
     *            number of bands
     * @return 
     */
    private static HashMap<Integer,HashMap<String,Set<String>>> generateHashTables(int l) {

        HashMap<Integer, HashMap<String, Set<String>>> hashTables = new HashMap<Integer, HashMap<String, Set<String>>>();
        for (int tableNum = 0; tableNum < l; tableNum++) {
            HashMap<String, Set<String>> hashTable = new HashMap<String, Set<String>>();
            hashTables.put(tableNum, hashTable);
        }
        return hashTables;
    }

    /**
     * This method generates hash key string for a user/item and returns it. It
     * generates hash key for the rating vector V of a user/item. 
     * for each vector in vectors:
     *     calculate dot product(vector,V), 
     *     append the result to key array.
     * convert key array to string
     * 
     * @param vmap
     *            vector map of hash functions.
     * @param V
     *            rating vector of a user/item
     * @param hashTableNum
     *            hash table number in other words band number that hashkey will
     *            be generated for.
     * @return returns hash key of given rating vector V as string.
     */
    private static String generateHashKeyForVector(
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            HashMap<String, Integer> V, int hashTableNum) {

        HashMap<Integer, HashMap<String, Integer>> vectors = vmap
                .get(hashTableNum);
        ArrayList<Integer> key = new ArrayList<Integer>();
        for (int i = 0; i < vectors.size(); i++) {
            HashMap<String, Integer> vector = vectors.get(Integer.valueOf(i));
            int dotProduct = Vector.calculateDotProduct(V, vector);
            if (dotProduct < 0) {
                key.add(Integer.valueOf(0));
            } else {
                key.add(Integer.valueOf(1));
            }
        }
        StringBuffer buf = new StringBuffer();
        for (Iterator<Integer> localIterator = key.iterator(); localIterator
                .hasNext();) {
            int c = localIterator.next().intValue();
            buf.append(Integer.toString(c));
        }
        String hashKey = buf.toString();

        return hashKey;
    }

    /**
     * This method inserts a userid/itemid to given hash table (band) based on
     * calculated hashkey. Hash table uses hask keys as key and Set<String> as
     * value. Each set contains the members of the hash key. 
     * check if the hash table contains the key: 
     *     if hash table contains the key: 
     *         retreive the key and set of values, 
     *         insert the item in the set 
     *     if hash table does not contain the key: 
     *         create a new empty hash set, 
     *         insert the item in hash set,
     *         add the hashset to hash table
     * 
     * @param id
     *            hash key
     * @param item
     *            userId,/itemId
     * @param hashTable
     *            contains <hashKey, userId/itemId Set>
     */
    private static void insertItemInHashTable(String id, String item,
            HashMap<String, Set<String>> hashTable) {
        if (hashTable.containsKey(id)) {
            hashTable.get(id).add(item);
        } else {
            Set<String> set = new HashSet<>();
            set.add(item);
            hashTable.put(id, set);
        }
    }
    
    public static double avg_num_of_buckets (HashMap<Integer, 
            HashMap<String, Set<String>>> hashTables) {
        Iterator<Entry<Integer, HashMap<String, Set<String>>>> iter = 
                hashTables.entrySet().iterator();
        int total = 0;
        while (iter.hasNext()) {
            Entry<Integer, HashMap<String, Set<String>>> entry = iter.next();
            HashMap<String, Set<String>> table = entry.getValue() ;
            total += table.keySet().size();
        }
        
        return (double) total / hashTables.size();
        
    }

}
