package library.collectionssyntax;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class TreeMapSyntax {

    public static void main(String[] args) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(3, 3);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        treeMap.put(7, 7);
        treeMap.put(8, 8);
        treeMap.put(9, 9);
        treeMap.put(10, 10);

        // -------------------- Traverse a Tree Map --------------------
        Iterator<Map.Entry<Integer, Integer>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        treeMap.forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });

        for (Integer key : treeMap.keySet()) {
            System.out.println("Key: " + key + ", Value: " + treeMap.get(key));
        }

        for (Integer value : treeMap.values()) {
            System.out.println("Value: " + value);
        }
        // ------------------------------------------------------------


        Integer searchKey = 4;
        System.out.println("Search Key: " + searchKey);

        Integer greaterThanEqual = treeMap.ceilingKey(searchKey);
        System.out.println("Greater than or equal to " + searchKey + " : " + greaterThanEqual + " | Value: " + treeMap.get(greaterThanEqual));

        Integer greaterThan = treeMap.higherKey(searchKey);
        System.out.println("Greater than " + searchKey + " : " + greaterThan + " | Value: " + treeMap.get(greaterThan));

        Integer lesserThanEqual = treeMap.floorKey(searchKey);
        System.out.println("Lesser than or equal to " + searchKey + " : " + lesserThanEqual + " | Value: " + treeMap.get(lesserThanEqual));

        Integer lesserThan = treeMap.lowerKey(searchKey);
        System.out.println("Lesser than " + searchKey + " : " + lesserThan + " | Value: " + treeMap.get(lesserThan));
    }

}
