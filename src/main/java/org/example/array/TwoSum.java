package org.example.array;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    static void main() {
//        for (int i : twoSum(new int[]{2, 3, 5, 2, 7}, 12)) {
//            System.out.println(i);
//        }
//
//        for (int i : twoSumHashMap(new int[]{2, 3, 5, 2, 7}, 12)) {
//            System.out.println(i);
//        }

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        System.out.println("Наша полная мапа: " + map);
//        System.out.println("Беру по ключу b: " + map.get("b"));
//        System.out.println("Содержит ключ d?: " + map.containsKey("d"));
//        System.out.println("Содержит значение 1?: " + map.containsValue(1));
//        System.out.println(map.getOrDefault("d", 0));

        map.put("aac", map.getOrDefault("aac", 3) + 114);
//        System.out.println(map.getOrDefault("d", 0));

        System.out.println("Содержит значение 1?: " + map.containsValue(1));
        System.out.println("Наша полная мапа: " + map);

    }

    static int[] twoSum(int[] nums, int target) {
        if (nums.length < 2) return null;

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) return new int[]{i, j};
            }
        }
        return null;
    }

    static int[] twoSumHashMap(int[] nums, int target) {
        if (nums.length < 2) return null;

        int tail;
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            tail = target - nums[i];
            if (map.containsKey(tail)) return new int[]{map.get(tail), i};
            map.put(nums[i], i);
        }
        return null;


    }
}

