package library.collectionssyntax;

import java.util.TreeSet;

public class TreeSetSyntax {

    public static void main(String[] args) {
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(1);
        treeSet.add(2);
        treeSet.add(3);
        treeSet.add(4);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(7);
        treeSet.add(8);
        treeSet.add(9);
        treeSet.add(10);

        treeSet.forEach(k -> System.out.print(k + "\t"));
        System.out.println();

        Integer searchElement = 3;
        System.out.println("Search Element: " + searchElement);


        Integer greaterThanEqual = treeSet.ceiling(searchElement);
        System.out.println("GTE: " + greaterThanEqual);

        Integer greaterThan = treeSet.higher(searchElement);
        System.out.println("GT: " + greaterThan);

        Integer lessThanEqual = treeSet.floor(searchElement);
        System.out.println("LTE: " + lessThanEqual);

        Integer lessThan = treeSet.lower(searchElement);
        System.out.println("LT: " + lessThan);
    }

}
