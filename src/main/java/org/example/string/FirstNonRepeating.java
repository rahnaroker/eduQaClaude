package org.example.string;

import java.util.HashMap;
import java.util.Map;

public class FirstNonRepeating {

    static void main() {
        System.out.println(firstNonRepeatingCharacter("baaby"));
    }

    static Character firstNonRepeatingCharacter(String s) {
        if (s == null) return null;
        Map<Character, Integer> map = charFrequencyUpd(s);

        for (Character c : s.toCharArray()) {
            if (map.get(c) == 1) return c;
        }
        return null;
    }

    static Map<Character, Integer> charFrequencyUpd(String s) {
        Map<Character, Integer> map = new HashMap<>();
        if (s == null) return map;

        for (Character c : s.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        return map;
    }
}
