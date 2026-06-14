package org.example.string;

public class Palindrome {

    public static void main() {
        System.out.println(isPalindromeNormalized("mamaamam "));
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

    static boolean isPalindromeNormalized(String s) {
        if (s == null) return false;
        if (s.length() < 2) return true;

        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) left++;
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) return false;
            left++;
            right--;
        }
        return true;
    }
}
