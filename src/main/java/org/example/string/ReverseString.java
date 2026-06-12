package org.example.string;

public class ReverseString {
    static void main(String[] args) {
        System.out.println(reverseString("hillo"));
    }

    public static String reverseString(String s) {
        if (s == null || s.length() < 2) return s;

        int left = 0;
        int right = s.length() - 1;
        char[] chars = s.toCharArray();

        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
        return new String(chars);
    }


}

