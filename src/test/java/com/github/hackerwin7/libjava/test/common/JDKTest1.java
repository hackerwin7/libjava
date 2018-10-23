package com.github.hackerwin7.libjava.test.common;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/04/18
 * Time: 7:15 PM
 * Desc:
 */
public class JDKTest1 {

    private enum CloseMode {
        GRACEFUL(true),
        NOTIFY_ONLY(true),
        DISCARD_NO_NOTIFY(false);

        boolean notifyDisconnect;

        CloseMode(boolean notifyDisconnect) {
            this.notifyDisconnect = notifyDisconnect;
        }
    }

    private static CloseMode mode;

    public static void main(String[] args) throws Exception {
        testMAX();
    }

    public static void testMAX() {
        System.out.println(Long.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE);
    }

    public static void testOrderOfMap() {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        map.put(1,1); map.put(2,2); map.put(3,3); map.put(4,4);
        for (Map.Entry<Integer, Integer> entry : map.entrySet())
            System.out.println(entry);
        Integer removed = map.remove(1);
        map.put(1, removed);
        for (Map.Entry<Integer, Integer> entry : map.entrySet())
            System.out.println(entry);
    }

    public static void testEnumDefault() {
        System.out.println(mode);
    }

    public static void testGetAndIncrement() {
        AtomicLong al = new AtomicLong(1);
        System.out.println(al.getAndIncrement());
        System.out.println(al.get());
    }

    public static void testNullInteger() {

    }

    public static void testEnum() {
        CloseMode close1 = CloseMode.GRACEFUL;
        CloseMode close2 = CloseMode.NOTIFY_ONLY;
        CloseMode close3 = CloseMode.DISCARD_NO_NOTIFY;

        System.out.println(close1 + "," + close2 + "," + close3);
        if (close1.notifyDisconnect) {
            // no op
        }

        System.out.println(close1 == close2);
        System.out.println(close2 == close3);
        System.out.println(close1.equals(close2));
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
