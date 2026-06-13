package org.example.string;

import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Anagram {

    static void main(String[] args) {
//        System.out.println(isAnagramSort("go3to", "roog3"));
//        System.out.println(isAnagramSortByArray("og", "go3"));
        System.out.println(isAnagramSortByMap("aabb", "abba"));
    }

    static boolean isAnagramSort(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        if (a.equals(b)) return true;

        char[] aChars = a.toCharArray();
        char[] bChars = b.toCharArray();

        Arrays.sort(aChars);
        Arrays.sort(bChars);

        return Arrays.equals(aChars, bChars);
    }

    static boolean isAnagramSortByArray(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        if (a.equals(b)) return true;

        int[] counts = new int[128];
        char[] aChars = a.toCharArray();
        char[] bChars = b.toCharArray();

        for (char c : aChars) {
            counts[c]++;
        }

        for (char c : bChars) {
            counts[c]--;
        }

        for (int i : counts) {
            if (i !=0) return false;
        }

        return true;
    }

    static boolean isAnagramSortByMap(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        if (a.equals(b)) return true;

        Map<Character, Integer> counts = new HashMap<>();
        char[] aChars = a.toCharArray();
        char[] bChars = b.toCharArray();

        for (char c : aChars) {
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        for (char c : bChars) {
            counts.put(c, counts.getOrDefault(c, 0) - 1);
        }

        for (int v : counts.values()) {
            if (v !=0) return false;
        }
        return true;
    }

}
