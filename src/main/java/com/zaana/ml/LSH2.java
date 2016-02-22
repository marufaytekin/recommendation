package com.zaana.ml;

import net.openhft.koloboke.collect.ObjIterator;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;
import net.openhft.koloboke.collect.set.hash.HashObjSet;
import net.openhft.koloboke.collect.set.hash.HashObjSets;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by maruf on 21/02/16.
 */
public final class LSH2 {
    static Logger LOG = Logger.getLogger(LSH.class);

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
                String hashKey = LSH.generateHashKeyForVector(vmap, V, hashTableNum);
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
}
