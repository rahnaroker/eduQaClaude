package org.example.string;

import java.util.ArrayDeque;
import java.util.Deque;

public class ValidBrackets {

    static void main() {

        System.out.println(isValidBrackets("()"));
        System.out.println(isValidBrackets("()[]{}"));
        System.out.println(isValidBrackets("([{}])"));
        System.out.println(isValidBrackets("(]"));
        System.out.println(isValidBrackets("([)]"));
        System.out.println(isValidBrackets("(((("));
        System.out.println(isValidBrackets(")("));

    }

    static boolean isValidBrackets(String s) {
        if (s == null || s.length() % 2 > 0) return false;

        Deque<Character> stack = new ArrayDeque<>();

        for (Character c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '[') stack.push(']');
            else if (c == '{') stack.push('}');
            else {
                if (stack.isEmpty()) return false;
                if (stack.pop() != c) return false;
            }
        }
        return stack.isEmpty();
    }
}
