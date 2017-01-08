package com.zaana.ml.utils;

import java.util.*;
import java.util.Map.Entry;

public final class SortHashMap
{

    private SortHashMap()
    {
    }

    /*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable throw
     * NullPointerException if Map contains null values It also sort values even
     * if they are duplicates
     */
    public static <K extends Comparable, V extends Comparable> LinkedHashMap<K, V> sortByValues(
            final Map<K, V> map)
    {
        List<Entry<K, V>> entries = new LinkedList<Entry<K, V>>(
                map.entrySet());
        Collections.sort(entries, new Comparator<Entry<K, V>>()
        {
            public int compare(final Entry<K, V> o1, final Entry<K, V> o2)
            {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        LinkedHashMap<K, V> sortedMap = new LinkedHashMap<K, V>();
        for (Entry<K, V> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable throw
     * NullPointerException if Map contains null values It also sort values even
     * if they are duplicates and returns list of keys.
     */
    public static <K extends Comparable, V extends Comparable> List<K> sortKeysByValues(
            final Map<K, V> map)
    {
        List<Entry<K, V>> entries = new LinkedList<>(
                map.entrySet());
        Collections.sort(entries, new Comparator<Entry<K, V>>()
        {
            public int compare(final Entry<K, V> o1, final Entry<K, V> o2)
            {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        List<K> sortedList = new ArrayList<>();
        for (Entry<K, V> entry : entries) {
            sortedList.add(entry.getKey());
        }
        return sortedList;
    }

    /*
     * Paramterized method to sort Map e.g. HashMap or Hashtable in Java throw
     * NullPointerException if Map contains null key
     */
    public static <K extends Comparable, V extends Comparable> LinkedHashMap<K, V> sortByKeys(
            Map<K, V> map)
    {
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);

        // LinkedHashMap will keep the keys in the order they are inserted
        // which is currently sorted on natural ordering
        LinkedHashMap<K, V> sortedMap = new LinkedHashMap<K, V>();
        for (K key : keys) {
            sortedMap.put(key, map.get(key));
        }

        return sortedMap;
    }

}
