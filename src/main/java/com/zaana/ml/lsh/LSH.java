package com.zaana.ml.lsh;

import net.openhft.koloboke.collect.ObjIterator;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import net.openhft.koloboke.collect.set.hash.HashObjSets;
import java.util.*;

/**
 * Created by maruf on 21/02/16.
 */
public final class LSH {

    static HashObjObjMap<Object, Object> hashKeyLookupTable;
    static HashObjObjMap<Object, Object> hashTables;

    public static HashObjObjMap<Object, Object> getHashKeyLookupTable() {
        return hashKeyLookupTable;
    }

    public static HashObjObjMap<Object, Object> getHashTables() {
        return hashTables;
    }

    private static HashObjObjMap<Object, Object> generateHashTables(int l) {

        HashObjObjMap<Object, Object> hashTables = HashObjObjMaps.getDefaultFactory().newMutableMap();
        for (int tableNum = 0; tableNum < l; tableNum++) {
            HashObjObjMap<Object, Object> hashTable = HashObjObjMaps.getDefaultFactory().newMutableMap();
            hashTables.put(tableNum, hashTable);
        }
        return hashTables;
    }

    public static HashObjObjMap<Object, Object> buildModel(
            HashObjObjMap<String, HashObjObjMap<String, Integer>> ratingMap,
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap, int l) {

        HashObjObjMap<Object, Object> hashTables = generateHashTables(l);

        ObjIterator<Map.Entry<String, HashObjObjMap<String, Integer>>> iter = ratingMap.entrySet().iterator();
        hashKeyLookupTable = HashObjObjMaps.getDefaultFactory().newMutableMap();

        while (iter.hasNext()) {
            Map.Entry<String, HashObjObjMap<String, Integer>> entry = iter.next();
            String K = entry.getKey();
            HashObjObjMap<String, Integer> V = entry.getValue();

            for (int hashTableNum = 0; hashTableNum < l; hashTableNum++) {
                HashObjObjMap<Object, Object> hashTable = (HashObjObjMap<Object, Object>) hashTables.get(hashTableNum);
                String hashKey = generateHashKeyForVector(vmap, V, hashTableNum);
                insertItemInHashTable(hashKey, K, hashTable);
                insertHashKeyWithObjectIdAndTableNumber(hashKey, K, hashTableNum, hashKeyLookupTable);
            }
        }

        return hashTables;

    }

    private static void insertItemInHashTable(String hashKey, String k, HashObjObjMap<Object, Object> hashTable) {

        if (hashTable.containsKey(hashKey)) {
            HashObjSet <String> s = (HashObjSet<String>) hashTable.get(hashKey);
            s.add(k);
        }
        else {
            HashObjSet <String> s = HashObjSets.getDefaultFactory().newMutableSet();
            s.add(k);
            hashTable.put(hashKey, s);
        }
    }

    private static void insertHashKeyWithObjectIdAndTableNumber(String hashKey, String k, int hashTableNum, HashObjObjMap<Object, Object> hashKeyLookupTable) {

        if (hashKeyLookupTable.containsKey(k+":"+ Integer.toString(hashTableNum)))
            throw new DuplicateFormatFlagsException("duplicate item key in a table");
        else hashKeyLookupTable.put(k + ":" + hashTableNum, hashKey);
    }

    public static HashObjSet<String> getCandidateItemSetForTopNRecommendation(
            HashObjObjMap<Object, Object> hashTables,
            Set<String> ratedItemSet,
            String ratedItemId,
            HashObjObjMap<Object, Object> hashKeyLookupTable)
    {
        HashObjSet <String> candidateSet = getCandidateSet(hashTables, ratedItemId, hashKeyLookupTable);
        candidateSet.removeAll(ratedItemSet);

        return candidateSet;

    }

    public static HashObjSet<String> getCandidateSetFromHashTables(
            HashObjObjMap<Object, Object> hashTables,
            String userId,
            HashObjObjMap<Object, Object> hashKeyLookupTable)
    {

        HashObjSet<String> candidateSet = getCandidateSet(hashTables, userId, hashKeyLookupTable);

        return candidateSet;


    }

    public static List<String> getCandidateListFromHashTables(
            HashObjObjMap<Object, Object> hashTables,
            String objectId,
            HashObjObjMap<Object, Object> hashKeyLookupTable)
    {
        List<String> candidateList = new ArrayList<>();
        for (int hashTableNum = 0; hashTableNum < hashTables.size(); hashTableNum++)
        {
            String hashKey = (String) hashKeyLookupTable.get(objectId + ":" + hashTableNum);
            HashObjObjMap<Object, Object> hashTable = (HashObjObjMap<Object, Object>) hashTables.get(hashTableNum);
            HashObjSet <String> candidates = (HashObjSet<String>) hashTable.get(hashKey);
            if (candidates != null)
                candidateList.addAll(candidates);
        }
        candidateList.remove(objectId);

        return candidateList;
    }

    private static HashObjSet<String> getCandidateSet(
            HashObjObjMap<Object, Object> hashTables,
            String objectId,
            HashObjObjMap<Object, Object> hashKeyLookupTable) {
        HashObjSet<String> candidateSet = HashObjSets.getDefaultFactory().newMutableSet();
        for (int hashTableNum = 0; hashTableNum < hashTables.size(); hashTableNum++)
        {
            String hashKey = (String) hashKeyLookupTable.get(objectId + ":" + hashTableNum);
            HashObjObjMap<Object, Object> hashTable = (HashObjObjMap<Object, Object>) hashTables.get(hashTableNum);
            HashObjSet <String> candidates = (HashObjSet<String>) hashTable.get(hashKey);
            candidateSet.addAll(candidates);
        }
        candidateSet.remove(objectId);

        return candidateSet;

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
    protected static String generateHashKeyForVector(
            HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> vmap,
            HashObjObjMap<String, Integer> V, int hashTableNum) {

        HashMap<Integer, HashMap<String, Integer>> vectors = vmap
                .get(hashTableNum);
        ArrayList<Integer> key = new ArrayList<>();
        for (int i = 0; i < vectors.size(); i++) {
            HashMap<String, Integer> vector = vectors.get(Integer.valueOf(i));
            int dotProduct = com.zaana.ml.tools.Vector.calculateDotProduct(V, vector);
            if (dotProduct < 0) key.add(0);
            else key.add(1);
        }
        StringBuilder buf = new StringBuilder();
        for (Integer c : key) buf.append(Integer.toString(c));
        return buf.toString();
    }

}
