package com.github.hackerwin7.libjava.test.common;

public class OOPTest {
    public static void main(String[] args) {
        Son son = new Son();
    }
}


class Grandpa {
    public Grandpa() {
        System.out.println("this is grandpa.");
    }

    public Grandpa(int age) {
        System.out.println("grandpa is " + age + " years old.");
    }

    private Height height = new Height(1.5f);

    public static Gender gender = new Gender(true, "grandpa");
}

class Father extends Grandpa {

    public Father() {
        System.out.println("this is father.");
    }

    public Father(int age) {
        System.out.println("father is " + age + " years old.");
    }

    private Height height = new Height(1.6f);

    public static Gender gender = new Gender(true, "father");
}

class Son extends Father {

    public Son() {
        super(50);
        System.out.println("this is son.");
    }

    public Son(int age) {
        System.out.println("son is " + age + " years old.");
    }

    private Height height = new Height(1.8f);

    public static Gender gender = new Gender(true, "son");
}

class Height {
    public Height(float height) {
        System.out.println("initializing height " + height + " meters.");
    }
}

class Gender {
    public Gender(boolean isMale) {
        if (isMale) {
            System.out.println("this is a male.");
        } else {
            System.out.println("this is a female.");
        }
    }

    public Gender(boolean isMale, String identify) {
        if (isMale) {
            System.out.println(identify + " is a male.");
        } else {
            System.out.println(identify + " is a female.");
        }
    }
}