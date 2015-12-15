package com.zaana.ml;

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
    static class MyComparator implements Comparator<Map.Entry <String, Double>>, Serializable {
        public int compare(Map.Entry <String, Double> o1, Map.Entry<String, Double> o2) {
            return Double.compare(o2.getValue(), o1.getValue());
        }
    }

    public static HashMap<String, PriorityQueue<Map.Entry<String, Double>>> createSimilarityMatrix(
            final HashMap<String, HashMap<String, Integer>> userRateMap, int y)
    {
        HashMap<String, PriorityQueue<Map.Entry<String, Double>>> similarityMatrix = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> userRateMapCopy = new HashMap<>(userRateMap);

        for (Map.Entry<String, HashMap<String, Integer>> userItemRatesPairA : userRateMap
                .entrySet()) {
            String userIdA = userItemRatesPairA.getKey();
            HashMap<String, Integer> userItemRatesA = userItemRatesPairA
                    .getValue();
            Set<String> ratedItemIDSetA = userItemRatesA.keySet();
            //userRateMapCopy.remove(userIdA);
            for (Map.Entry<String, HashMap<String, Integer>> entryB : userRateMapCopy
                    .entrySet()) {
                String userIdB = entryB.getKey();
                if (userIdA == userIdB) continue;
                HashMap<String, Integer> userItemRatesB = entryB.getValue();
                Set<String> ratedItemIDSetB = userItemRatesB.keySet();
                Set<String> intersectionAB = new HashSet<>(ratedItemIDSetA);
                intersectionAB.retainAll(ratedItemIDSetB);
                Double similarity;
                if (!intersectionAB.isEmpty()) {
                    similarity = Cosine.calculateCosineSimilarity(intersectionAB,
                            userItemRatesA, userItemRatesB, y);
                    if (similarityMatrix.containsKey(userIdA)) {
                        similarityMatrix.get(userIdA).add(new HashMap.SimpleEntry<>(userIdB, similarity));
                    } else {
                        PriorityQueue<Map.Entry<String, Double>> q = new PriorityQueue<>(100, new MyComparator());
                        q.add(new HashMap.SimpleEntry<>(userIdB, similarity));
                        similarityMatrix.put(userIdA, q);
                    }
                }
            }
            PriorityQueue<Map.Entry<String, Double>> q = similarityMatrix.get(userIdA);
            q.removeIf()
        }
        return similarityMatrix;
    }

    public static void createAndWriteModel(
            final HashMap<String, HashMap<String, Integer>> userRateMap, String filePath, int y) {

        HashMap<String, PriorityQueue<Map.Entry<String, Double>>> model = createSimilarityMatrix(userRateMap, y);
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
