package com.github.hackerwin7.libjava.test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentTest {

    private static boolean flag = false;
    private static Object obj = new Object();

    public static void main(String[] args) throws Exception {
        syncTest6();
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

    private static void syncTest4() {
        final Object sync = new Object();
        final AtomicBoolean lock = new AtomicBoolean(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("notifying");
                    lock.set(false); // avoid dead-lock, call notify before wait() without condition will cause to dead-lock
                    sync.notify();
                    System.out.println("notified");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    System.out.println("waiting");
                    while (lock.get()) {
                        try {
                            sync.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("after wait, contining");
                }
            }
        }).start();
    }

    private static void innerTest() {
        final int i = 10;
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    while (flag) {
                        System.out.println("sss");
                        System.out.println(i);
                    }
                }
            }
        }).start();
    }

    public static void executeRunnable(ToothBrushBusiness toothBrushBusiness, boolean isProducer) throws Exception {
        for(int i = 0 ; i < 20 ; i++) {
            if (isProducer) {
                toothBrushBusiness.produceToothBrush();
                Thread.sleep(1000);
            } else {
                toothBrushBusiness.consumeToothBrush();
                Thread.sleep(500);
            }
        }
    }

    public class ToothBrushBusiness {
        //定义一个大小为10的牙刷缓冲队列
        private Queue<ToothBrush> toothBrushQueue = new LinkedList<>();
        private int number = 1;
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notEmpty = lock.newCondition();
        private final Condition notFull = lock.newCondition();
        public ToothBrushBusiness() { }
        //生产牙刷
        public void produceToothBrush(){
            lock.lock();
            try {
                //牙刷缓冲队列已满,则生产牙刷线程等待
                while (toothBrushQueue.size() == 10){
                    System.out.println("Full !!!!!!!!!");
                    notFull.await();
                }
                ToothBrush toothBrush = new ToothBrush(number);
                toothBrushQueue.add(toothBrush);
                System.out.println("生产: " + toothBrush.toString());
                number++;
                //牙刷缓冲队列加入牙刷后,唤醒消费牙刷线程
                notEmpty.signal();
                System.out.println("after producer signal");
            } catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                System.out.println("producer unlock");
                lock.unlock(); // release mutex
            }
        }

        //消费牙刷
        public void consumeToothBrush(){
            lock.lock();
            try {
                //牙刷缓冲队列为空,则消费牙刷线程等待
                while (toothBrushQueue.isEmpty()){
                    System.out.println("Empty !!!!!!!!");
                    notEmpty.await();
                }
                ToothBrush toothBrush = toothBrushQueue.poll();
                System.out.println("消费: " + toothBrush.toString());
                //从牙刷缓冲队列取出牙刷后,唤醒生产牙刷线程
                notFull.signal();
                System.out.println("after consumer signal");
            } catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                System.out.println("consumer unlock");
                lock.unlock();
            }
        }
    }

    public class ToothBrush {
        private int number;
        public ToothBrush(int number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "牙刷编号{" + "number=" + number + '}';
        }
    }

    public static void syncTest5() throws Exception {
        final ToothBrushBusiness business = new ConcurrentTest().new ToothBrushBusiness();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    executeRunnable(business, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "producer").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    executeRunnable(business, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "consumer").start();
    }

    public static void syncTest6() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (obj) {
                        System.out.println(Thread.currentThread().getName());
                        try {
                            throw new Exception("test exp.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            throw new Exception("out exp.");
                        }
//                        try {
//                            System.out.println("sleeping...");
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                } catch (Exception e) {
                    System.out.println("out exp.");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    System.out.println(Thread.currentThread().getName());
                }
            }
        }).start();
    }
}
