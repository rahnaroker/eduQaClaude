package org.example.string;

public class temp {

    static void main() {
        System.out.println(recurs(4));
    }

    static int recurs(int i) {
        if (i == 1) return 1;
        return i * recurs(i - 1);
    }
}
