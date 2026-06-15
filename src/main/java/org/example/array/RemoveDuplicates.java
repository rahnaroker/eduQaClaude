package org.example.array;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class RemoveDuplicates {

    static void main() {
        System.out.println(removeDuplicatesSortedArray(new int[]{1, 1, 2, 2, 3, 3}));
    }

    static int removeDuplicatesSortedArray(int[] nums) {
        if (nums.length < 2) return nums.length;

        int slow = 0;
        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast];
            }
        }
        return slow + 1;
    }

    static int[] removeDuplicatesHashSet(int[] nums) {
        if (nums.length < 2) return nums;

        Set<Integer> set = new HashSet<>();

        for (int i : nums) {
            set.add(i);
        }

        int[] dedup = new int[set.size()];
        int idx = 0;
        for (int s : set) {
            dedup[idx] = s;
            idx++;
        }
        return dedup;
    }

    static int[] removeDuplicatesLinkedHashSet(int[] nums) {
        if (nums.length < 2) return nums;

        Set<Integer> set = new LinkedHashSet<>();

        for (int i : nums) {
            set.add(i);
        }

        int[] dedup = new int[set.size()];
        int idx = 0;
        for (int s : set) {
            dedup[idx] = s;
            idx++;
        }
        return dedup;
    }

    static int[] removeDuplicatesLinkedHashSetStream(int[] nums) {
        if (nums.length < 2) return nums;

        Set<Integer> set = new LinkedHashSet<>();

        for (int i : nums) {
            set.add(i);
        }

        return set.stream().mapToInt(Integer::intValue).toArray();
    }

}
