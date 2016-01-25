package com.zaana.ml;

import com.google.common.collect.MinMaxPriorityQueue;
import com.zaana.ml.similarity.Cosine;

import java.io.*;
import java.util.*;

/**
 * Created by maruf on 14/12/15.
 */
public class ModelBuild  implements Serializable {
    /**
     * Use following methods to create a similarity matrix in
     * HashMap format.
     */
    static class CustomComparator implements Comparator<Map.Entry <String, Double>>, Serializable {
        public int compare(Map.Entry <String, Double> o1, Map.Entry<String, Double> o2) {
            return Double.compare(o2.getValue(), o1.getValue());
        }
    }

    public static HashMap<String, MinMaxPriorityQueue<Map.Entry<String, Double>>> createSimilarityMatrix(
            final HashMap<String, HashMap<String, Integer>> ratingMap, int y, int k)
    {
        HashMap<String, MinMaxPriorityQueue<Map.Entry<String, Double>>> similarityMatrix = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> userRateMapCopy = new HashMap<>(ratingMap);
        for (Map.Entry<String, HashMap<String, Integer>> userItemRatesPairA : ratingMap
                .entrySet()) {
            String objectIdA = userItemRatesPairA.getKey();
            HashMap<String, Integer> objectRatingsA = userItemRatesPairA
                    .getValue();
            Set<String> ratedItemIDSetA = objectRatingsA.keySet();
            for (Map.Entry<String, HashMap<String, Integer>> entryB : userRateMapCopy
                    .entrySet()) {
                String objectIdB = entryB.getKey();
                if (objectIdA == objectIdB) continue;
                HashMap<String, Integer> objectRatingsB = entryB.getValue();
                Set<String> ratedItemIDSetB = objectRatingsB.keySet();
                Set<String> intersectionAB = new HashSet<>(ratedItemIDSetA);
                intersectionAB.retainAll(ratedItemIDSetB);
                Double similarity;
                if (!intersectionAB.isEmpty()) {
                    similarity = Cosine.calculateCosineSimilarity(intersectionAB,
                            objectRatingsA, objectRatingsB, y);
                    if (similarityMatrix.containsKey(objectIdA)) {
                        similarityMatrix.get(objectIdA).offer(new HashMap.SimpleEntry<>(objectIdB, similarity));
                    } else {
                        MinMaxPriorityQueue<Map.Entry<String, Double>> q = MinMaxPriorityQueue
                                .orderedBy(new CustomComparator())
                                .maximumSize(k)
                                .create();
                        q.offer(new HashMap.SimpleEntry<>(objectIdB, similarity));
                        similarityMatrix.put(objectIdA, q);
                    }
                }
            }
        }
        return similarityMatrix;
    }

    public static void createAndWriteModel(
            final HashMap<String, HashMap<String, Integer>> userRateMap, String filePath, int y, int k) {

        HashMap<String, MinMaxPriorityQueue<Map.Entry<String, Double>>> model = createSimilarityMatrix(userRateMap, y, k);
        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(model);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + filePath);
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    public static HashMap<String, PriorityQueue<Map.Entry<String, Double>>> readModelFromFile (String filePath) {
        HashMap model = null;
        try
        {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            model = (HashMap) in.readObject();
            in.close();
            fileIn.close();
        } catch(FileNotFoundException e)
        {
            System.out.println("Model file not found..");
            e.printStackTrace();
            return null;
        } catch(IOException e)
        {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Deserialized model...");

        return model;
    }

}
