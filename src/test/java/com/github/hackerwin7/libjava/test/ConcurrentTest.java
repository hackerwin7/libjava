package com.github.hackerwin7.libjava.test;

public class ConcurrentTest {
    public static void main(String[] args) throws Exception {
        syncTest3();
    }

    private static void syncTest() {
        final Object sync = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t1 starting...");
                    try {
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t1 ending...");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t2 starting...");
                    sync.notify();
                    System.out.println("t2 ending...");
                }
            }
        }).start();
    }

    private static void syncTest1() {
        final Object sync = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t1 starting...");
                    try {
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t1 ending...");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t2 starting...");
                    sync.notifyAll();
                    System.out.println("t2 ending...");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t3 starting...");
                    System.out.println("t3 ending...");
                }
            }
        }).start();
    }

    private static void syncTest2() throws Exception {
        final Object sync = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t1 starting...");
                    try {
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t1 ending...");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t3 starting...");
                    System.out.println("t3 ending...");
                }
            }
        }).start();
        Thread.sleep(1000);
        System.out.println("notifing...");
        synchronized (sync) {
            sync.notifyAll(); // have to inner synchronized block
        }
    }

    private static void syncTest3() {
        final Object sync = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t1 starting...");
                    try {
                        sync.wait();
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t1 ending...");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t2 starting...");
                    sync.notifyAll();
                    try {
                        sync.wait(); // skip out the synchronized and put out the lock of sync, and see the other thread to get the lock (if notify, the before this wait's wait thread can get the lock or a new thread's synchronized block to get the put out lock)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t2 ending...");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("t3 starting...");
                    System.out.println("t3 ending...");
                    //sync.notifyAll();
                }
            }
        }).start();
    }
}
