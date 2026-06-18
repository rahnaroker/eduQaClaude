package org.example.integer;

public class FizzBuzz {

    static void main() {
        fizzBuzzMethod(15);
    }

    static void fizzBuzzMethod(int num) {
        if (num <= 0) System.out.println("1 or more");

        for (int i = 1; i <= num; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }

    }
}
