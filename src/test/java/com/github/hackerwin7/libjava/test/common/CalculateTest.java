package com.github.hackerwin7.libjava.test.common;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/04/14
 * Time: 6:11 PM
 * Desc:
 */
public class CalculateTest {
    public static void main(String[] args) {
        xorSwap();
    }

    private static void xorSwap() {
        int a = 10, b = 22, c = 10, d = 22, e = 119;
        System.out.println(a ^ c ^ b ^ d ^ e);
        System.out.println(e ^ a ^ d ^ c ^ b);
    }
}
