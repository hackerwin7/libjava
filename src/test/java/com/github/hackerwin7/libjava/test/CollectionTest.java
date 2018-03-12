package com.github.hackerwin7.libjava.test;

import java.util.*;

public class CollectionTest {
    public static void main(String[] args) {
        sortMapTest();
    }

    private static void sortMapTest() {
        HashMap<Integer, Integer> map = new LinkedHashMap<>();
        map.put(1, 1);
        map.put(3, 3);
        map.put(10, 10);
        map.put(0, 0);
        map.put(-1 , -1);
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }

        Set<Map.Entry<Integer, Integer>> set = map.entrySet();
        LinkedList<Map.Entry<Integer, Integer>> list = new LinkedList<>(set);
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                //return o1.getValue().compareTo(o2.getValue());
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        System.out.println(list);
        Map<Integer, Integer> newMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list)
            newMap.put(entry.getKey(), entry.getValue());
        for (Map.Entry<Integer, Integer> entry: newMap.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }

    private static void concurrentCollections() {
        //Collections.synchronizedList();synchronizedMap etc..
    }
}
