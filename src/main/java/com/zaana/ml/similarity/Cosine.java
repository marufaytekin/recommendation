package com.zaana.ml.similarity;

import com.zaana.ml.SortHashMap;
import net.openhft.koloboke.collect.ObjIterator;
import net.openhft.koloboke.collect.map.hash.HashObjObjMap;
import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;

public final class Cosine implements Similarity {
    static Logger LOG = Logger.getLogger(Cosine.class);

    //private final static int minSharedNumber = 1; // non shared items 0 kabul et.

    private Cosine() {
    }


    public static LinkedHashMap<String, Double> getSimilarityListWithCandidateSet(
            String itemId, Set<String> candidateSet,
            HashObjObjMap<String, HashObjObjMap<String, Integer>> rateMap, int y) {
        
        LinkedHashMap<String, Double> similarityHashMap = new LinkedHashMap<>();
        HashObjObjMap<String, Integer> mapA = rateMap.get(itemId);
        Set<String> setA = mapA.keySet();
        Double similarity = 0.0;
        Set<String> intersectionAB;
        HashObjObjMap<String, Integer> mapB;
        for (String user : candidateSet) {
            try {
                mapB = rateMap.get(user);
                intersectionAB = new HashSet<>(setA);
                intersectionAB.retainAll(mapB.keySet());
                if (!intersectionAB.isEmpty()) {
                    similarity = calculateCosineSimilarity(intersectionAB, mapA,
                            mapB, y);
                    similarityHashMap.put(user, similarity);
                }
                else {
                    similarityHashMap.put(user, similarity);
                }
            } catch (NullPointerException e) {
                continue;
            }
        }
        LinkedHashMap<String, Double> sorted = SortHashMap.sortByValues(
                similarityHashMap);

        return sorted;

    }

    public static double getCosineSimilarity(
            String userId1, String userId2,
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> ratingMap, int y)
    {
        HashObjObjMap<String, Integer> ratings1 = ratingMap.get(userId1);
        HashObjObjMap<String, Integer> ratings2 = ratingMap.get(userId2);
        Set<String> set1 = ratings1.keySet();
        Set<String> set2 = ratings2.keySet();

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        if (intersection.isEmpty()) {
            return 0;
        }

        return calculateCosineSimilarity(intersection, ratings1, ratings2, y);

    }

    /**
     * This implementation calculates cosine similarity based on common ratings.
     * @param intersection
     * @param map1
     * @param map2
     * @param y
     * @return
     */
    public static double calculateCosineSimilarity(
            final Set<String> intersection,
            final HashObjObjMap<String, Integer> map1,
            final HashObjObjMap<String, Integer> map2, int y)
    {
        int intersec_size = intersection.size();
        int num = 0;
        int denum1 = 0;
        int denum2 = 0;
        int a;
        int b;
        for (String key : intersection) {
            a = map1.get(key);
            b = map2.get(key);
            num += a * b;
            denum1 += a * a;
            denum2 += b * b;
        }
        double sim = (double) num / (Math.sqrt(denum1) * Math.sqrt(denum2));

        double signif_weight = (double) Math.min(y, intersec_size) / y;

        return signif_weight * sim; // account for significance

    }

    /**
     * This implementation calculates cosine with all ratings in l2 form.
     * @param intersection
     * @param map1
     * @param map2
     * @param y
     * @return
     */
    public static double calculateCosineSimilarity2(
            final Set<String> intersection,
            final HashObjObjMap<String, Integer> map1,
            final HashObjObjMap<String, Integer> map2, int y)
    {
        int intersec_size = intersection.size();
        int num = 0;
        int a;
        int b;
        for (String key : intersection) {
            a = map1.get(key);
            b = map2.get(key);
            num += a * b;
        }
        double denum1 = l2NormOfVector(map1);
        double denum2 = l2NormOfVector(map2);
        double sim = (double) num / (denum1 * denum2);

        double signif_weight = (double) Math.min(y, intersec_size) / y;

        return signif_weight * sim; // account for significance

    }

    private static double l2NormOfVector(HashObjObjMap<String, Integer> map) {
        int total = 0;
        for (String key : map.keySet()) {
            int value = map.get(key);
            total += value * value;
        }
        return Math.sqrt(total);
    }

    /**
     * Use following methods to create a similarity matrix in
     * HashMap format.
     */
    public static double[][] createDistanceMatrix(
            final HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMap, int y) {

        HashMap<String, LinkedHashMap<String, Double>> similarityMatrix = new HashMap<>();
        HashObjObjMap<String, HashObjObjMap<String, Integer>> userRateMapCopy =
                HashObjObjMaps.getDefaultFactory().newMutableMap(userRateMap);

        ObjIterator<Entry<String, HashObjObjMap<String, Integer>>> entryAIter = userRateMap
                .entrySet().iterator();

        ArrayList<Double> tempArr = new ArrayList<>();
        while (entryAIter.hasNext()) {
            Entry<String, HashObjObjMap<String, Integer>> userItemRatesPairA = entryAIter
                    .next();
            String userIdA = userItemRatesPairA.getKey();
            HashObjObjMap<String, Integer> userItemRatesA = userItemRatesPairA
                    .getValue();
            Set<String> ratedItemIDSetA = userItemRatesA.keySet();
            userRateMapCopy.remove(userIdA);
            for (Entry<String, HashObjObjMap<String, Integer>> entryB : userRateMapCopy
                    .entrySet()) {
                String userIdB = entryB.getKey();
                //if ( userIdA == userIdB) continue;
                HashObjObjMap<String, Integer> userItemRatesB = entryB.getValue();
                Set<String> ratedItemIDSetB = userItemRatesB.keySet();
                Set<String> intersectionAB = new HashSet<>(
                        ratedItemIDSetA);
                intersectionAB.retainAll(ratedItemIDSetB);
                Double distance = 1.0;
                if (!intersectionAB.isEmpty()) {
                    distance = 1.0 - calculateCosineSimilarity(intersectionAB,
                            userItemRatesA, userItemRatesB, y);
                }
                if (similarityMatrix.containsKey(userIdA)) {
                    similarityMatrix.get(userIdA).put(userIdB, distance);
                } else {
                    LinkedHashMap<String, Double> map = new LinkedHashMap<>();
                    map.put(userIdB, distance);
                    similarityMatrix.put(userIdA, map);
                }
                tempArr.add(distance);
            }
        }

        double x[][] = new double[1][tempArr.size()];
        for(int i = 0; i < tempArr.size(); i++){
            x[0][i] = 10 * tempArr.get(i);
        }
        return x;
    }

}
