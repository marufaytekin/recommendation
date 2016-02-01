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

    public static double getCosineSimilarity(
            String userId1, String userId2,
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

    public static double calculateCosineSimilarity(
            final Set<String> intersection,
            final HashMap<String, Integer> map1,
            final HashMap<String, Integer> map2, int y)
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
     * Use following methods to create a similarity matrix in
     * HashMap format.
     */
    public static double[][] createDistanceMatrix(
            final HashMap<String, HashMap<String, Integer>> userRateMap, int y) {

        HashMap<String, LinkedHashMap<String, Double>> similarityMatrix = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> userRateMapCopy = new HashMap<>(userRateMap);

        Iterator<Entry<String, HashMap<String, Integer>>> entryAIter = userRateMap
                .entrySet().iterator();

        ArrayList<Double> tempArr = new ArrayList<>();
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
