package org.example.integer;

import java.util.ArrayList;
import java.util.List;

public class Fibonacci {

    static void main() {
        System.out.println(fib(0));
    }

    static long fib(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0");
        if (n < 2) return n;            // fib(0)=0, fib(1)=1

        long prev = 0, curr = 1;
        for (int i = 2; i <= n; i++) {
            long next = prev + curr;
            prev = curr;
            curr = next;
        }
        return curr;
    }

    static long fib2(int num) {
        if (num < 0) throw new IllegalArgumentException("num must be >= 0");
        if (num < 2) return num;

        long curr = 0;
        long prev = 1;

        for (int i = 2; i <= num; i++) {
            long next = curr + prev;
            prev = curr;
            curr = next;
        }
        return curr;
    }


    // рекурсия
    static int sumR(int n) {
        if (n == 0) return 0;          // база
        return n + sumR(n - 1);
    }
    // цикл
    static int sumF(int n) {
        int acc = 0;
        for (int i = 1; i <= n; i++) acc += i;
        return acc;
    }
}
