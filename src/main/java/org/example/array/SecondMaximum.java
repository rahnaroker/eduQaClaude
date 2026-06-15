package org.example.array;

public class SecondMaximum {

    static void main() {
        System.out.println(secondMax(new int[]{5, 5, 3}));
    }

    static Integer secondMax(int[] nums) {
        if (nums.length < 2) return null;
        // {1, 3, 2}
        int max = nums[0];
        Integer secondMax = null;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > max) {
                secondMax = max;
                max = nums[i];
            } else if (nums[i] < max && (secondMax == null || nums[i] > secondMax)) {
                secondMax = nums[i];
            }

        }
        return secondMax;

    }
}
