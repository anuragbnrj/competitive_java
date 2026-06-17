package library.collectionssyntax;

import java.util.*;

public class MapSyntax {

    // ---------------------------------------------------------------
    // HashMap - O(1) average get/put, NO ordering guarantee.
    // Key idioms below are the ones that matter under OA time pressure.
    //
    // Ordering variants:
    //   - LinkedHashMap : insertion order (or access order -> LRU)
    //   - TreeMap       : sorted by key (see TreeMapSyntax.java)
    //
    // GOTCHAS:
    //   - map.get(missingKey) returns null; unboxing it to int -> NPE.
    //     Use getOrDefault.
    //   - Iteration order of a plain HashMap is unspecified.
    //   - Removing during a for-each throws ConcurrentModification;
    //     use an Iterator.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();

        map.put("a", 1);
        map.putIfAbsent("a", 99);              // no-op, "a" exists
        System.out.println(map.get("a"));      // expect: 1

        // getOrDefault: safe read, no NPE on missing key
        System.out.println(map.getOrDefault("z", 0)); // expect: 0

        // Frequency counting - the two canonical one-liners
        String[] words = {"x", "y", "x", "x", "y"};
        Map<String, Integer> freq = new HashMap<>();
        for (String w : words) {
            freq.merge(w, 1, Integer::sum);            // preferred
            // freq.put(w, freq.getOrDefault(w, 0) + 1); // equivalent
        }
        System.out.println(freq);              // expect: {x=3, y=2}

        // computeIfAbsent: build a multimap (key -> list) cleanly
        Map<Integer, List<String>> groups = new HashMap<>();
        groups.computeIfAbsent(1, k -> new ArrayList<>()).add("p");
        groups.computeIfAbsent(1, k -> new ArrayList<>()).add("q");
        System.out.println(groups);            // expect: {1=[p, q]}

        // Iteration
        for (Map.Entry<String, Integer> e : freq.entrySet()) {
            System.out.println(e.getKey() + "=" + e.getValue());
        }

        // Safe removal while iterating: use the Iterator
        Iterator<Map.Entry<String, Integer>> it = freq.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue() < 3) it.remove();
        }
        System.out.println(freq);              // expect: {x=3}

        // LinkedHashMap preserves insertion order
        Map<String, Integer> ordered = new LinkedHashMap<>();
        ordered.put("first", 1);
        ordered.put("second", 2);
        ordered.put("third", 3);
        System.out.println(ordered.keySet());  // expect: [first, second, third]
    }
}
