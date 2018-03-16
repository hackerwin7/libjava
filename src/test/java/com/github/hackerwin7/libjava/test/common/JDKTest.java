package com.github.hackerwin7.libjava.test.common;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/01/22
 * Time: 10:44 AM
 * Desc:
 */
public class JDKTest {
    public static void main(String[] args) throws Exception {
        strFormatTest();
    }

    private static void ceilTest() {
        System.out.println(Math.ceil(3 * 0.0000001));
    }

    private static void compareMap() {
        Map<Integer, List<String>> m1 = new HashMap<>();
        m1.put(1, Arrays.asList("1", "11", "111"));
        Map<Integer, List<String>> m2 = new HashMap<>();
        m2.put(1, Arrays.asList("1", "11", "111"));
        Map<Integer, List<String>> m3 = new HashMap<>();
        m3.put(1, Arrays.asList("1", "12", "123"));
        System.out.println(m1.equals(m2));
        System.out.println(m1.equals(m3));
    }

    private static void executeAndSubmit() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("execute:" + new Date());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        });
        System.out.println("=================================================================");
        ExecutorService submitter = Executors.newSingleThreadExecutor();
        submitter.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("submit: " + new Date());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        });

    }

    private static void threadReuse() throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("end.");
            }
        });
        thread.start();
        Thread.sleep(2000);
        if (!thread.isAlive()) {
            System.out.println("restart");
            thread.start();
        }
    }

    private static void splitTest() {
        String s = "111:1,222:2,3:333";
        String s1 = "444";
        System.out.println(s.split(":")[0]);
        System.out.println(s1.split(":")[0]);
    }

    private static void strFormatTest() {
        String original = String.format("%1$-" + 10 + "s", "");
        String current = original.replace(' ', 'x');
        System.out.println("val = " + original + "#EOF");
        System.out.println("val = " + current + "#EOF");
        System.out.println(current.length());
        System.out.println(current.getBytes().length);
    }
}
