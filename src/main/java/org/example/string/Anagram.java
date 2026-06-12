package org.example.string;

import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;

public class Anagram {

    static void main(String[] args) {
        System.out.println(isAnagramSort("go3to", "roog3"));
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

}
