package library.collectionssyntax;

import java.util.*;

public class ComparatorSyntax {

    // ---------------------------------------------------------------
    // Two ways to define ordering:
    //   - Comparable<T>  : the type's ONE natural order (compareTo).
    //   - Comparator<T>  : any number of external orders (compare),
    //                      passed to sort / PriorityQueue / TreeMap.
    //
    // Both return: negative if a<b, 0 if equal, positive if a>b.
    //
    // A Comparator can be written three equivalent ways:
    //   1. named class    : class ByX implements Comparator<T> { compare... }
    //   2. anonymous class: new Comparator<T>() { compare... }
    //   3. lambda/fluent  : Comparator.comparingInt(...).thenComparing(...)
    // Prefer a named class for complex / reused / testable ordering; a lambda
    // for quick inline orders.
    //
    // GOTCHAS:
    //   - `a - b` overflows for large/negative ints -> Integer.compare.
    //   - Arrays.sort(int[]) takes NO comparator (primitives, ascending
    //     only); box to Integer[] to use one.
    //   - Stability: Arrays.sort(primitives) = dual-pivot quicksort,
    //     NOT stable; Arrays.sort(Object[]) / List.sort = TimSort,
    //     stable (equal elements keep input order).
    //   - A comparator must be consistent (total order); an
    //     inconsistent one throws "Comparison method violates its
    //     general contract" once the input is large enough.
    // ---------------------------------------------------------------

    // A type with a natural order (by age).
    static class Person implements Comparable<Person> {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public int compareTo(Person other) {
            return Integer.compare(this.age, other.age); // NOT this.age - other.age
        }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }

    // A custom comparator CLASS: a reusable, named external order with
    // handwritten multistep logic (age ascending, then name as tie-break).
    // Reach for this over a lambda when the logic is non-trivial, reused in
    // several places, or you want to unit-test the ordering on its own.
    static class ByAgeThenName implements Comparator<Person> {
        @Override
        public int compare(Person a, Person b) {
            int byAge = Integer.compare(a.age, b.age); // primary key
            if (byAge != 0) return byAge;
            return a.name.compareTo(b.name);           // tie-break
        }
    }

    public static void main(String[] args) {
        List<Person> people = new ArrayList<>(
            List.of(
                new Person("Cara", 30),
                new Person("Ann", 25),
                new Person("Bob", 30),
                new Person("Dan", 25)
            )
        );

        // 1) Natural order (Comparable.compareTo) -> by age. Stable sort, so the
        //    two age-30s keep input order: Cara before Bob.
        Collections.sort(people);
        System.out.println(people); // expect: [Ann(25), Dan(25), Cara(30), Bob(30)]

        // 2) Comparator: by age, then by name as a tie-break (fluent API).
        //    Now the 30s are ordered by name: Bob before Cara.
        people.sort(
            Comparator
                .comparingInt((Person p) -> p.age)
                .thenComparing(p -> p.name)
        );
        System.out.println(people); // expect: [Ann(25), Dan(25), Bob(30), Cara(30)]

        // 3) reversed(): by age descending
        people.sort(Comparator.comparingInt((Person p) -> p.age).reversed());
        System.out.println(people.get(0).age); // expect: 30

        // 4) Arrays: primitives have NO comparator; box to sort descending
        Integer[] nums = {3, 1, 2};
        Arrays.sort(nums, Comparator.reverseOrder());
        System.out.println(Arrays.toString(nums)); // expect: [3, 2, 1]

        // 5) PriorityQueue with a comparator (max-heap)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.addAll(List.of(1, 5, 2));
        System.out.println(maxHeap.peek()); // expect: 5

        // 6) TreeMap with a comparator (descending keys)
        TreeMap<Integer, String> desc = new TreeMap<>(Comparator.reverseOrder());
        desc.put(1, "a");
        desc.put(3, "c");
        desc.put(2, "b");
        System.out.println(desc.firstKey()); // expect: 3

        // 7) nullsFirst: tolerate null elements
        List<String> withNull = new ArrayList<>(Arrays.asList("b", null, "a"));
        withNull.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println(withNull); // expect: [null, a, b]

        // 8) Why Integer.compare matters: a - b overflows here
        int a = Integer.MAX_VALUE, b = Integer.MIN_VALUE;
        System.out.println((a - b) < 0);                // expect: true  (WRONG: overflow)
        System.out.println(Integer.compare(a, b) > 0);  // expect: true  (correct)

        // 9) Custom comparator CLASS used directly
        people.sort(new ByAgeThenName());
        System.out.println(people); // expect: [Ann(25), Dan(25), Bob(30), Cara(30)]

        // 10) The payoff: one named instance is reused across containers
        Comparator<Person> byAgeThenName = new ByAgeThenName();

        PriorityQueue<Person> pq = new PriorityQueue<>(byAgeThenName);
        pq.addAll(people);
        System.out.println(pq.peek());   // expect: Ann(25)  (min = youngest, then name)

        TreeSet<Person> set = new TreeSet<>(byAgeThenName);
        set.addAll(people);
        System.out.println(set.first()); // expect: Ann(25)

        // 11) Anonymous comparator class - the pre-lambda idiom, equivalent to
        //     the lambda `(p1, p2) -> p1.name.compareTo(p2.name)`.
        Comparator<Person> byName = new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.name.compareTo(p2.name);
            }
        };
        people.sort(byName);
        System.out.println(people); // expect: [Ann(25), Bob(30), Cara(30), Dan(25)]
    }
}
