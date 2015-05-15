package com.zaana.ml.similarity;

import com.zaana.ml.SortHashMap;
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
            HashMap<String, HashMap<String, Integer>> rateMap, int y) {
        
        LinkedHashMap<String, Double> similarityHashMap = new LinkedHashMap<>();
        HashMap<String, Integer> mapA = rateMap.get(itemId);
        Set<String> setA = mapA.keySet();
        Double similarity = 0.0;
        Set<String> intersectionAB;
        HashMap<String, Integer> mapB;
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

    public static double getCosineSimilarity(String userId1, String userId2,
                                             final HashMap<String, HashMap<String, Integer>> ratingMap, int y)
    {
        HashMap<String, Integer> ratings1 = ratingMap.get(userId1);
        HashMap<String, Integer> ratings2 = ratingMap.get(userId2);
        Set<String> set1 = ratings1.keySet();
        Set<String> set2 = ratings2.keySet();

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        if (intersection.isEmpty()) {
            return 0;
        }

        return calculateCosineSimilarity(intersection, ratings1, ratings2, y);

    }

    private static double calculateCosineSimilarity(
            final Set<String> intersection,
            final HashMap<String, Integer> map1,
            final HashMap<String, Integer> map2, int y)
    {
        if (intersection.isEmpty()) return 0;
        Iterator<String> iter = intersection.iterator();
        int intersec_size = intersection.size();
        int num = 0;
        int denum1 = 0;
        int denum2 = 0;
        int a;
        int b;
        while (iter.hasNext()) {
            String key = iter.next();
            a = map1.get(key);
            b = map2.get(key);
            num += a * b;
            denum1 += a * a;
            denum2 += b * b;
        }
        double sim = (double) num / (Math.sqrt(denum1) * Math.sqrt(denum2));

        int numerator = Math.min(y, intersec_size);

        double signif_sim = ((double)numerator / y) * sim;


        return signif_sim;// account for significance

    }

    /*
     * Use following methods to create a similarity matrix in
     * HashMap format.
     */
    public static HashMap<String, LinkedHashMap<String, Double>> createSimilarityMatrix(
            final HashMap<String, HashMap<String, Integer>> userRateMap)
    {
        HashMap<String, LinkedHashMap<String, Double>> similarityMatrix = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> userRateMapCopy = new HashMap<>(userRateMap);

        Iterator<Entry<String, HashMap<String, Integer>>> entryAIter = userRateMap
                .entrySet().iterator();

        while (entryAIter.hasNext()) {
            Entry<String, HashMap<String, Integer>> userItemRatesPairA = entryAIter
                    .next();
            String userIdA = userItemRatesPairA.getKey();
            HashMap<String, Integer> userItemRatesA = userItemRatesPairA
                    .getValue();
            Set<String> ratedItemIDSetA = userItemRatesA.keySet();
            userRateMapCopy.remove(userIdA);
            for (Entry<String, HashMap<String, Integer>> entryB : userRateMapCopy
                    .entrySet()) {
                String userIdB = entryB.getKey();
                //if ( userIdA == userIdB) continue;
                HashMap<String, Integer> userItemRatesB = entryB.getValue();
                Set<String> ratedItemIDSetB = userItemRatesB.keySet();
                Set<String> intersectionAB = new HashSet<>(
                        ratedItemIDSetA);
                intersectionAB.retainAll(ratedItemIDSetB);
                Double similarity;
                if (!intersectionAB.isEmpty()) {
                    similarity = calculateCosineSimilarity(intersectionAB,
                            userItemRatesA, userItemRatesB, 1);
                    if (similarityMatrix.containsKey(userIdA)) {
                        similarityMatrix.get(userIdA).put(userIdB, similarity);
                    } else {
                        LinkedHashMap<String, Double> map = new LinkedHashMap<>();
                        map.put(userIdB, similarity);
                        similarityMatrix.put(userIdA, map);
                    }
                }
            }
        }

        return similarityMatrix;

    }

}
