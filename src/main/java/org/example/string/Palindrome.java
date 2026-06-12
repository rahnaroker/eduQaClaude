package org.example.string;

public class Palindrome {

    public static void main() {
        System.out.println(isPalindrome("mama"));
    }

    static boolean isPalindrome(String s) {

        if (s == null) return false;
        if (s.length() < 2) return true;

        char[] chars = s.toCharArray();
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (chars[left] == chars[right]) {
                left++;
                right--;
            } else {
                return false;
            }
        }
        return true;
    }
}
