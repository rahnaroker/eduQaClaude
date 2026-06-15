package org.example.array;

public class MissingNumber {

    static void main() {
        System.out.println(missingNumber(new int[]{3, 0, 1}));
        System.out.println(missingNumberXor(new int[]{3, 0, 1}));
    }

    static Integer missingNumberXor(int[] nums) {
        int xor = nums.length;

        for (int i =0; i < nums.length; i++) {
            xor = xor ^ i ^ nums[i];
        }
        return xor;
    }

    static Integer missingNumber(int[] nums) {
        if (nums == null) return null;

        int arrLength = nums.length;
        int sum = 0;

        for (int i : nums) {
            sum = sum + i;
        }

        return (arrLength * (arrLength + 1) / 2) - sum;

    }
}
