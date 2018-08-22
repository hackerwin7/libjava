package com.github.hackerwin7.libjava.test.common;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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
    public static void main(String[] args) throws Exception {
        test2();
    }

    public static void test1() throws Exception {
        Map<String, String> ss = new HashMap<>();
        ss.put("1", "111");
        ss.put("2", "222");
        ss.forEach((k, v) -> {
            System.out.println(k + "," + v);
        });
        while (true) {
            Thread.sleep(1000);
            System.out.println("sleeping...");
        }
    }

    public static void test2() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DAY_OF_MONTH, 16);
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 58);
        Date d1 = cal.getTime();
        System.out.println(d1.getTime());
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 57);
        cal.set(Calendar.SECOND, 44);
        Date d2 = cal.getTime();
        System.out.println(d2.getTime());
        Thread.sleep(d1.getTime() - d2.getTime());
    }
}
