//import java.util.*;
////import javafx.util.Pair;
//
///*
//Theoretical Questions:
//
//1. Should Keys in Map be mutable and why?
//2. Checked and Unchecked Exceptions
//3. What is the advantage of effectively final?
//4. Advantage of <? super Animal> and <? extends Animal>
//5. Code style:
//
//   public boolean func(boolean value) {
//    if (!value.equals(Boolean.FALSE)) {
//        return true;
//    } else {
//        return false;
//    }
//
//   }
//
//    What are the problems with this type of codestyle?
//*/
//
///**
// Suppose we have an unsorted log file of accesses to web resources. Each log entry consists of an access time, the ID of the user making the access, and the resource ID.
//
// The access time is represented as seconds since 00:00:00, and all times are assumed to be in the same day.
//
// Examples:
// logs1 = [
// ["200", "user_1", "resource_5"],
// ["3", "user_1", "resource_1"],
// ["620", "user_1", "resource_1"],
// ["620", "user_3", "resource_1"],
// ["34", "user_6", "resource_2"],
// ["95", "user_9", "resource_1"],
// ["416", "user_6", "resource_1"],
// ["58523", "user_3", "resource_1"],
// ["53760", "user_3", "resource_3"],
// ["58522", "user_22", "resource_1"],
// ["100", "user_3", "resource_6"],
// ["400", "user_6", "resource_2"],
// ]
//
// logs2 = [
// ["357", "user", "resource_2"],
// ["1262", "user", "resource_1"],
// ["1462", "user", "resource_2"],
// ["1060", "user", "resource_1"],
// ["756", "user", "resource_3"],
// ["1090", "user", "resource_3"],
// ]
//
// logs3 = [
// ["300", "user_10", "resource_5"],
// ]
//
// logs4 = [
// ["1", "user_96", "resource_5"],
// ["1", "user_10", "resource_5"],
// ["301", "user_11", "resource_5"],
// ["301", "user_12", "resource_5"],
// ["603", "user_12", "resource_5"],
// ["1603", "user_12", "resource_7"],
// ]
//
// logs5 = [
// ["300", "user_1", "resource_3"],
// ["599", "user_1", "resource_3"],
// ["900", "user_1", "resource_3"],
// ["1199", "user_1", "resource_3"],
// ["1200", "user_1", "resource_3"],
// ["1201", "user_1", "resource_3"],
// ["1202", "user_1", "resource_3"]
// ]
//
// Write a function that takes the logs and returns the resource with the highest number of accesses in any 5 minute window, together with how many accesses it saw.
//
// Expected Output:
// most_requested_resource(logs1) # => ('resource_1', 3) [resource_1 is accessed at 416, 620, 620]
// most_requested_resource(logs2) # => ('resource_1', 2) [resource_1 is accessed at 1060, 1262]
// most_requested_resource(logs3) # => ('resource_5', 1) [resource_5 is accessed at 300]
// most_requested_resource(logs4) # => ('resource_5', 4) [resource_5 is accessed at 1, 1, 301, 301]
// most_requested_resource(logs5) # => ('resource_3', 4) [resource_3 is accessed at 1199, 1200, 1201, and 1202]
//
// Complexity analysis variables:
//
// n: number of logs in the input
// **/
//
//public class Kar2 {
//    public static void main(String[] argv) {
//        String[][] logs1 = {
//                {"200", "user_1", "resource_5"},
//                {"3", "user_1", "resource_1"},
//                {"620", "user_1", "resource_1"},
//                {"620", "user_3", "resource_1"},
//                {"34", "user_6", "resource_2"},
//                {"95", "user_9", "resource_1"},
//                {"416", "user_6", "resource_1"},
//                {"58523", "user_3", "resource_1"},
//                {"53760", "user_3", "resource_3"},
//                {"58522", "user_22", "resource_1"},
//                {"100", "user_3", "resource_6"},
//                {"400", "user_6", "resource_2"},
//        };
//
//        String[][] logs2 = {
//                {"357", "user", "resource_2"},
//                {"1262", "user", "resource_1"},
//                {"1462", "user", "resource_2"},
//                {"1060", "user", "resource_1"},
//                {"756", "user", "resource_3"},
//                {"1090", "user", "resource_3"},
//        };
//
//        String[][] logs3 = {
//                {"300", "user_10", "resource_5"},
//        };
//
//        String[][] logs4 = {
//                {"1", "user_96", "resource_5"},
//                {"1", "user_10", "resource_5"},
//                {"301", "user_11", "resource_5"},
//                {"301", "user_12", "resource_5"},
//                {"603", "user_12", "resource_5"},
//                {"1603", "user_12", "resource_7"},
//        };
//
//        String[][] logs5 = {
//                {"300", "user_1", "resource_3"},
//                {"599", "user_1", "resource_3"},
//                {"900", "user_1", "resource_3"},
//                {"1199", "user_1", "resource_3"},
//                {"1200", "user_1", "resource_3"},
//                {"1201", "user_1", "resource_3"},
//                {"1202", "user_1", "resource_3"},
//        };
//
//
//        // Map<String, int[]> userSessionDuration = getUserSessionDuration(logs1);
//        // printOutput(userSessionDuration);
//        // userSessionDuration = getUserSessionDuration(logs2);
//        // printOutput(userSessionDuration);
//        // userSessionDuration = getUserSessionDuration(logs3);
//        // printOutput(userSessionDuration);
//        // userSessionDuration = getUserSessionDuration(logs4);
//        // printOutput(userSessionDuration);
//        // userSessionDuration = getUserSessionDuration(logs5);
//        // printOutput(userSessionDuration);
//
//
//        // Map<String, int[]> userSessionDuration = getUserSessionDuration(logs2);
//
//        // for (Map.Entry<String, int[]> entry : userSessionDuration.entrySet()) {
//
//        //   String userId = entry.getKey();
//
//        //   int[] accessTime = entry.getValue();
//
//        //   System.out.println("User Id: " + userId + ", first: " + accessTime[0] + ", last: " + accessTime[1]);
//        // }
//
//        Pair<String, Integer> ans = getMaxUsedResource(logs1);
//        System.out.println(ans.getKey() + ", " + ans.getValue());
//        ans = getMaxUsedResource(logs2);
//        System.out.println(ans.getKey() + ", " + ans.getValue());
//        ans = getMaxUsedResource(logs3);
//        System.out.println(ans.getKey() + ", " + ans.getValue());
//        ans = getMaxUsedResource(logs4);
//        System.out.println(ans.getKey() + ", " + ans.getValue());
//        ans = getMaxUsedResource(logs5);
//        System.out.println(ans.getKey() + ", " + ans.getValue());
//    }
//
//
//
//    private static Pair<String, Integer> getMaxUsedResource(String[][] logsConstant) {
//        int rows = logsConstant.length;
//        int cols = logsConstant[0].length;
//        String[][] logs = new String[rows][cols];
//        for (int r = 0; r < rows; r++) {
//            logs[r] = Arrays.copyOf(logsConstant[r], logsConstant[r].length);
//        }
//
//        Arrays.sort(logs, (a, b) -> {
//            return Integer.compare(Integer.parseInt(a[0]), Integer.parseInt(b[0]));
//        });
//
//        Map<String, List<Integer>> resourceUsageTimes = new HashMap<>();
//        for (String[] log : logs) {
//            int accessTime = Integer.parseInt(log[0]);
//            String userId = log[1];
//            String resourceId = log[2];
//
//            if (resourceUsageTimes.containsKey(resourceId)) {
//                List<Integer> resourceUsage = resourceUsageTimes.get(resourceId);
//
//                resourceUsage.add(accessTime);
//
//                resourceUsageTimes.put(resourceId, resourceUsage);
//            } else {
//                List<Integer> resourceUsage = new ArrayList<>();
//                resourceUsage.add(accessTime);
//
//                resourceUsageTimes.put(resourceId, resourceUsage);
//            }
//        }
//
//        int maxUsage = 0;
//        String maxUsageResource = "";
//        for (Map.Entry<String, List<Integer>> entry : resourceUsageTimes.entrySet()) {
//            String resourceId = entry.getKey();
//            List<Integer> accessTime = entry.getValue();
//
//            for (int left = 0, right = 0; right < accessTime.size(); right++) {
//                // if (accessTime.get(right) - accessTime.get(left) <= 300) {
//
//                // }
//
//                while (accessTime.get(right) - accessTime.get(left) > 300) {
//                    left += 1;
//                }
//
//                int currSize = right - left + 1;
//                if (currSize > maxUsage) {
//                    maxUsage = currSize;
//                    maxUsageResource = resourceId;
//                }
//            }
//        }
//
//        return new Pair(maxUsageResource, maxUsage);
//
//    }
//
//    private static void printOutput(Map<String, int[]> userSessionDuration) {
//        System.out.println("============================");
//        for (Map.Entry<String, int[]> entry : userSessionDuration.entrySet()) {
//
//            String userId = entry.getKey();
//
//            int[] accessTime = entry.getValue();
//
//            System.out.println("User Id: " + userId + ", first: " + accessTime[0] + ", last: " + accessTime[1]);
//        }
//    }
//
//
//    private static Map<String, int[]> getUserSessionDuration(String[][] logs) {
//        Map<String, int[]> userSessionDuration = new HashMap<>();
//
//        for (String[] log : logs) {
//            int accessTime = Integer.parseInt(log[0]);
//            String userId = log[1];
//
//            if (userSessionDuration.containsKey(userId)) {
//                int[] userSessions = userSessionDuration.get(userId);
//
//                userSessions[0] = Math.min(userSessions[0], accessTime);
//                userSessions[1] = Math.max(userSessions[1], accessTime);
//
//                userSessionDuration.put(userId, userSessions);
//            } else {
//                int[] userSessions = new int[]{accessTime, accessTime};
//
//                userSessionDuration.put(userId, userSessions);
//            }
//        }
//
//        return userSessionDuration;
//    }
//}
//
//
