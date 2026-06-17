package library.collectionssyntax;

import java.util.*;

public class EqualsHashCode {

    // ---------------------------------------------------------------
    // CONTRACT (why hash-based collections work):
    //   - If a.equals(b) then a.hashCode() == b.hashCode().
    //   - Override BOTH or NEITHER. Overriding equals but not hashCode
    //     breaks HashMap/HashSet lookups.
    //   - hashCode may collide (equal hash, non-equal objects) — that's
    //     fine; equals is the tiebreaker.
    //
    // OA TRAPS:
    //   - A custom object with NO override uses identity (==) equality,
    //     so two "equal" instances are different keys.
    //   - int[] as a key uses IDENTITY too -> a fresh equal-content
    //     array never matches. Use List<Integer> or a String key.
    //   - TreeSet/TreeMap decide equality by compareTo/comparator,
    //     NOT equals -> a comparator returning 0 silently drops a
    //     "duplicate" even when !equals.
    //   - Mutating a key's fields after insertion corrupts the bucket.
    // ---------------------------------------------------------------

    // Correct: equals + hashCode overridden together.
    static class Point {
        int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() { return Objects.hash(x, y); }

        @Override
        public String toString() { return "(" + x + "," + y + ")"; }
    }

    // Broken: no override -> identity equality.
    static class BadPoint {
        int x, y;
        BadPoint(int x, int y) { this.x = x; this.y = y; }
    }

    public static void main(String[] args) {
        // Correct type: equal-content instances are the SAME key
        Set<Point> points = new HashSet<>();
        points.add(new Point(1, 2));
        points.add(new Point(1, 2));            // duplicate, ignored
        System.out.println(points.size());      // expect: 1
        System.out.println(points.contains(new Point(1, 2))); // expect: true

        // Broken type: identity equality -> each instance is distinct
        Set<BadPoint> bad = new HashSet<>();
        bad.add(new BadPoint(1, 2));
        bad.add(new BadPoint(1, 2));
        System.out.println(bad.size());          // expect: 2  (no dedup!)

        // int[] as a key uses identity: a fresh equal array misses
        Map<int[], String> arrKey = new HashMap<>();
        arrKey.put(new int[]{1, 2}, "v");
        System.out.println(arrKey.get(new int[]{1, 2})); // expect: null

        // Fix: use List<Integer> (content-based equals/hashCode)
        Map<List<Integer>, String> listKey = new HashMap<>();
        listKey.put(List.of(1, 2), "v");
        System.out.println(listKey.get(List.of(1, 2)));  // expect: v

        // TreeSet equality is by the comparator, NOT equals:
        // comparing only by x means (1,9) is treated as a duplicate of (1,2).
        TreeSet<Point> byX = new TreeSet<>(Comparator.comparingInt(p -> p.x));
        byX.add(new Point(1, 2));
        byX.add(new Point(1, 9));               // comparator says 0 -> dropped
        System.out.println(byX.size());          // expect: 1
    }
}
