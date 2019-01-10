package com.github.hackerwin7.libjava.test.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/12/25
 * Time: 15:26
 * Desc: a new JDK test for instance
 */
public class JDKTest {
    public static void main(String[] args) {
        conversion();
    }

    public static void conversion() {
        byte b = 10; // -128 ~ 127
        int a = 10;
        int c = 1000;
        b = (byte) a;
        System.out.println(b);
        b = (byte) c;
        System.out.println(b);
    }

    public static void javaRuntime() {
        System.out.println(System.getProperty("java.specification.version"));
    }

    /**
     * LRU test demo for map
     */
    public static void LRU() {
//        Map<Long, String> lru = new LinkedHashMap<>();
//        lru.forEach();
    }
}
