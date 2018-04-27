package com.github.hackerwin7.libjava.test.common;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/04/18
 * Time: 7:15 PM
 * Desc:
 */
public class JDKTest1 {
    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        Map<String, String> ss = new HashMap<>();
        ss.put("1", "111");
        ss.put("2", "222");
        ss.forEach((k, v) -> {
            System.out.println(k + "," + v);
        });
    }
}
