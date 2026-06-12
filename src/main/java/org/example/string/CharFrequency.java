package org.example.string;

import java.util.HashMap;
import java.util.Map;

public class CharFrequency {

    static void main() {
        Map<Character, Integer> map = charFrequency("baby");
        System.out.println(map);

        Map<Character, Integer> map2 = charFrequencyUpd("baby");
        System.out.println(map2);
    }

    static Map<Character, Integer> charFrequency(String s) {
        Map<Character, Integer> map = new HashMap<>();
        if (s == null) return map;

        for (Character c : s.toCharArray()) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }

        }
        return map;
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
