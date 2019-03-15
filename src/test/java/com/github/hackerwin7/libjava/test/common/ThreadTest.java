package com.github.hackerwin7.libjava.test.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/04/26
 * Time: 10:28 AM
 * Desc:
 */
public class ThreadTest {
    public static void main(String[] args) throws Exception {
        deadLockProgram();
    }

    public static void deadLockProgram() {
        Object resourceA = new Object();
        Object resourceB = new Object();
        Thread lockingAFirst = new Thread(new DeadlockRunnable(resourceA, resourceB));
        Thread lockingBFirst = new Thread(new DeadlockRunnable(resourceB, resourceA));
        lockingAFirst.start();
        lockingBFirst.start();
    }

    private static class DeadlockRunnable implements Runnable {
        private final Object firstResource;
        private final Object secondResource;

        public DeadlockRunnable(Object firstResource, Object secondResource) {
            this.firstResource = firstResource;
            this.secondResource = secondResource;
        }

        @Override
        public void run() {
            try {
                synchronized (firstResource) {
                    printLockedResource(firstResource);
                    Thread.sleep(1000);
                    synchronized (secondResource) {
                        printLockedResource(secondResource);
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Exception occurred: " + e);
            }
        }

        private static void printLockedResource(Object resource) {
            System.out.println(Thread.currentThread().getName() + ": lock resource -> " + resource);
        }
    }

    public static void interLeavingOutput4() {
        final int N = 20;
        final AtomicInteger n = new AtomicInteger(1);
        final AtomicReference<Boolean> signal = new AtomicReference<>(false);
        final AtomicReference<Boolean> beginBlock = new AtomicReference<>(false);
        Object lock = new Object();

        Thread th1 = new Thread(() -> {
            // must begin from 1
//            while (n.get() != 1) {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            while (n.get() < N) { // another thread have enter the sync and output the N if <= N will output N + 1
                synchronized (lock) {
                    while (signal.get()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("th1:" + n.get());
                    n.incrementAndGet();
                    signal.set(true);
                    lock.notify();
                }
            }
        });

        Thread th2 = new Thread(() -> {
            // must begin from 2
//            while (n.get() != 2) {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            while (n.get() < N) {
                synchronized (lock) {
                    while (!signal.get()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("th2:" + n.get());
                    n.incrementAndGet();
                    signal.set(false);
                    lock.notify();
                }
            }
        });

        th1.start();
        th2.start();
    }

    public static void interLeavingOutput3() {
        String a = "123";
        String b = "abc";

        class SignalOutput {
            Object lock = new Object();
            String signal = "a";

            public void doWait(String src) {
                synchronized (lock) {
                    while (!signal.equals(src)) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    signal = "o"; // clear the signal after exit the wait using the signal
                }
            }

            public void doNotify(String dst) {
                synchronized (lock) {
                    signal = dst;
                    lock.notify();
                }
            }
        }

        class Outputer extends Thread {
            String str;
            SignalOutput sig;
            String src, dst;
            Outputer(String str, SignalOutput sig, String src, String dst) {
                this.str = str;
                this.sig = sig;
                this.src = src;
                this.dst = dst;
            }

            @Override
            public void run() {
                for (int i = 0; i < str.length(); i++) {
                    sig.doWait(src);
                    System.out.print(str.charAt(i));
                    sig.doNotify(dst);
                }
            }
        }

        SignalOutput signal = new SignalOutput();
        Outputer outa = new Outputer(a, signal, "a", "b");
        Outputer outb = new Outputer(b, signal, "b", "a");
        outa.start();
        outb.start();
    }

    public static void notifyAllTest() throws Exception {
        Object lock = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("starting 1");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("wait 1");
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("starting 2");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("wait 2");
            }
        });
        Thread t3 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("starting 3");
                lock.notifyAll();
                System.out.println("notifyall 1");
            }
        });
        t1.start();
        t2.start();
        Thread.sleep(1000);
        t3.start();
    }

    public static void interLeavingOutput2() {
        String a = "123";
        String b = "abc";
        AtomicReference<String> gSignal = new AtomicReference<>("a");
        HashMap<String, String> sigs = new HashMap<>(); sigs.put("a", "b"); sigs.put("b", "a");
        Object lock = new Object();

        class Outputer extends Thread {
            String str;
            String signal;
            // you can add global signal here
            Outputer(String str, String singal) {
                this.str = str;
                this.signal = singal;
            }

            @Override
            public void run() {
                for (int i = 0; i < str.length(); i++) {
                    synchronized (lock) {
                        while (!gSignal.get().equals(signal)) { // wait and notify basic model
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } // after exit the spin lock maybe we need clear the signal
                        System.out.print(str.charAt(i));
                        gSignal.set(sigs.get(signal)); // same as clear signal
                        lock.notify();
                    }
                }
            }
        }

        Outputer outa = new Outputer(a, "a");
        Outputer outb = new Outputer(b, "b");
        outa.start();
        outb.start();
    }

    public static void interLeavingOutput1() {
        String a = "123";
        String b = "abc";
        final Object lock = new Object();
        AtomicReference<String> ab = new AtomicReference<>();
        ab.set("a");

        Thread ta = new Thread(() -> {
            synchronized (lock) {
                for (char s : a.toCharArray()) {
                    while (!ab.get().equals("a")) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(s);
                    ab.set("b");
                    lock.notify();
                }
            }
        });
        Thread tb = new Thread(() -> {
            synchronized (lock) {
                for (char s : b.toCharArray()) {
                    while (!ab.get().equals("b")) { // while is better than if
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(s);
                    ab.set("a");
                    lock.notify();
                }
            }
        });
        ta.start();
        tb.start();
    }

    public static void interLeavingOutput() {
        String a = "123";
        String b = "abc";
        final Object lock = new Object();

        class Outputer extends Thread {
            private String str;
            Outputer(String s) {
                str = s;
            }

            @Override
            public void run() {
                int  i = 0;
                while (i < str.length()) {
                    synchronized (lock) {
                        System.out.print(str.charAt(i));
                        lock.notify();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    i++;
                }
                synchronized (lock) {
                    lock.notify();
                }
            }
        }
        Outputer outa = new Outputer(a);
        Outputer outb = new Outputer(b);
        outa.start(); // maybe need firstly start outa and sleep to start outb
        outb.start();
    }

    public static volatile int cnt = 0;

    public static void volatileTest() throws Exception {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                cnt++;
            }
        };
        int sum = 1000;
        Thread[] threads = new Thread[sum];
        for (int i = 0; i < sum; i++) {
            threads[i] = new Thread(run);
        }
        for (int i = 0; i < sum; i++) {
            threads[i].start();
        }
        Thread.sleep(1000);
        System.out.println(cnt);
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
