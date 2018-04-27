package com.github.hackerwin7.libjava.test.common;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/04/26
 * Time: 10:28 AM
 * Desc:
 */
public class ThreadTest {
    public static void main(String[] args) throws Exception {
        callableTest();
    }

    public static void callableTest() throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(new CallImpl("111"));
        Thread thread = new Thread(futureTask);
        thread.start();
        String ret = futureTask.get();
        System.out.println(ret);
    }

    static class CallImpl implements Callable<String> {

        private String name;

        CallImpl(String name) {
            this.name = name;
        }

        @Override
        public String call() {
            return name;
        }
    }
}
