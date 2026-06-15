///*
//Your catering service is organizing a wedding reception that will bring together multiple families with a lot of complicated history between them.
//
//We know which families are mutual enemies:
//
//enemies_1 = [
//    # family 1 is an enemy of family 2 and family 2 is an enemy of family 1
//    [ "1", "2" ],
//]
//
//We have invited some guests:
//
//guests_1 = [
//    # name, family
//    [ "Alice", "1" ],
//    [ "Bob", "1" ],
//    [ "Dima", "2" ],
//    [ "Miriam", "2" ],
//]
//
//Guests who belong to enemy families cannot sit such that they can hear each other. For example, Alice and Miriam can't sit near enough to hear each other.
//
//We have a table layout that shows which pairs of tables are too close to each other. For example, in this layout:
//
//table_layout_1 = [
//    [ "T1", "T2" ],
//    [ "T2", "T3" ],
//]
//
//T1 and T2 are too close to each other, so people at T1 can hear people at T2 and vice versa. Similarly, people at T2 can hear people at T3, and people at T3 can hear people at T2.
//
//People seated at the same table can also hear each other.
//
//We have a proposed seating arrangement:
//
//seating_1 = [
//    [ "Alice", "T1" ],
//    [ "Bob", "T1" ],
//    [ "Dima", "T3" ],
//    [ "Miriam", "T2" ],
//]
//
//Your job is to verify that this seating arrangement is valid, ie that no members of enemy families are sitting such that they can hear each other.
//
//In this case, the arrangement won't work. Alice and Bob at Table 1 could hear Miriam at Table 2. The arrangement would be valid if Miriam were at Table 3 instead.
//
//Write a function that given enemies, guests, table_layout, and seating, returns True or False depending on whether the arrangement is valid.
//
//All test cases:
//
//is_valid_arrangement(enemies_1, guests_1, table_layout_1, seating_1) -> False
//is_valid_arrangement(enemies_1, guests_1, table_layout_1, seating_2) -> True
//is_valid_arrangement(enemies_1, guests_1, table_layout_1, seating_6) -> False (Alice and Dima can't both be at Table 10)
//is_valid_arrangement(enemies_2, guests_2, table_layout_2, seating_3) -> False (Alice and Miriam can't both be at Table 1)
//is_valid_arrangement(enemies_2, guests_2, table_layout_2, seating_4) -> False (Alice and Joumana can hear each other)
//is_valid_arrangement(enemies_2, guests_2, table_layout_2, seating_5) -> True
//
//Complexity variables:
//F = the number of families
//G = the number of guests
//T = the number of tables
//*/
//
//import java.io.*;
//import java.util.*;
//import javafx.util.Pair;
//
//public class Kar3 {
//    public static void main(String[] argv) {
//        String[][] enemies_1 = {
//                {"1", "2"}
//        };
//        String[][] enemies_2 = {
//                {"1", "2"},
//                {"4", "5"},
//                {"1", "3"}
//        };
//        String[][] guests_1 = {
//                {"Alice", "1"},
//                {"Bob", "1"},
//                {"Dima", "2"},
//                {"Miriam", "2"}
//        };
//        String[][] guests_2 = {
//                {"Alice", "1"},
//                {"Bob", "1"},
//                {"Esther", "5"},
//                {"Dima", "4"},
//                {"Joumana", "3"},
//                {"Miriam", "2"},
//                {"Abe", "3"},
//                {"Klaus", "4"},
//                {"Noor", "1"}
//        };
//        String[][] table_layout_1 = {
//                {"T1", "T2"},
//                {"T2", "T3"}
//        };
//        String[][] table_layout_2 = {
//                {"T1", "T2"},
//                {"T2", "T3"},
//                {"T1", "T5"}
//        };
//        String[][] seating_1 = {
//                {"Alice", "T1"},
//                {"Bob", "T1"},
//                {"Dima", "T3"},
//                {"Miriam", "T2"}
//        };
//        String[][] seating_2 = {
//                {"Alice", "T1"},
//                {"Bob", "T1"},
//                {"Dima", "T3"},
//                {"Miriam", "T3"}
//        };
//        String[][] seating_3 = {
//                {"Alice", "T1"},
//                {"Bob", "T2"},
//                {"Esther", "T1"},
//                {"Dima", "T3"},
//                {"Joumana", "T2"},
//                {"Miriam", "T1"},
//                {"Abe", "T3"},
//                {"Klaus", "T2"},
//                {"Noor", "T3"}
//        };
//        String[][] seating_4 = {
//                {"Alice", "T1"},
//                {"Bob", "T2"},
//                {"Esther", "T1"},
//                {"Dima", "T3"},
//                {"Joumana", "T2"},
//                {"Miriam", "T3"},
//                {"Abe", "T3"},
//                {"Klaus", "T3"},
//                {"Noor", "T3"}
//        };
//        String[][] seating_5 = {
//                {"Alice", "T1"},
//                {"Bob", "T2"},
//                {"Esther", "T4"},
//                {"Dima", "T3"},
//                {"Joumana", "T4"},
//                {"Miriam", "T4"},
//                {"Abe", "T4"},
//                {"Klaus", "T3"},
//                {"Noor", "T2"}
//        };
//        String[][] seating_6 = {
//                {"Alice", "T10"},
//                {"Dima", "T10"}
//        };
//    }
//
//
//    private static Integer calculateMostPopular(int[] friendLikes, int[][] likesData) {
//        Map<Integer, Integer> freq = new HashMap<>();
//
//        Set<Integer> friendLiked = new HashSet<>();
//        for (int like : friendLikes) {
//            friendLiked.add(like);
//        }
//
//        for (int[] likesRow : likesData) {
//            int common = 0;
//            Set<Integer> uncommon = new HashSet<>();
//            for (int like : likesRow) {
//                if (friendLiked.contains(like)) {
//                    common += 1;
//                } else {
//                    uncommon.add(like);
//                }
//            }
//
//            if (common >= 2) {
//                for (Integer like : uncommon) {
//                    int currFreq = freq.getOrDefault(like, 0);
//                    freq.put(like, currFreq + 1);
//                }
//            }
//        }
//
//        int ans = -1;
//        int maxFreq = -1;
//        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
//            int restaurant = entry.getKey();
//            int count = entry.getValue();
//
//            if (count > maxFreq) {
//                maxFreq = count;
//                ans = restaurant;
//            }
//        }
//
//        return ans;
//    }
//
//    private static Map<String, Pair<Integer, Integer>> getCustomerEventsAggregated(String[][] transactions) {
//        Map<String, Set<String>> customerEvents = new HashMap<>();
//        Map<String, Integer> customerTotals = new HashMap<>();
//
//        for (String[] transaction : transactions) {
//            String customerId = transaction[0];
//            String eventId = transaction[1];
//            int spent = Integer.parseInt(transaction[2]);
//
//            Set<String> eventsTillNow = customerEvents.getOrDefault(customerId, new HashSet<>());
//            int spentTillNow = customerTotals.getOrDefault(customerId, 0);
//
//            eventsTillNow.add(eventId);
//            spentTillNow += spent;
//
//            customerEvents.put(customerId, eventsTillNow);
//            customerTotals.put(customerId, spentTillNow);
//        }
//
//        Map<String, Pair<Integer, Integer>> ans = new HashMap<>();
//        for (Map.Entry<String, Set<String>> entry : customerEvents.entrySet()) {
//            String customerId = entry.getKey();
//            Integer eventsAttended = entry.getValue().size();
//            Integer totalSpent = customerTotals.get(customerId);
//
//            Pair<Integer, Integer> temp = new Pair<>(eventsAttended, totalSpent);
//
//            ans.put(customerId, temp);
//        }
//
//        return ans;
//    }
//}
//
