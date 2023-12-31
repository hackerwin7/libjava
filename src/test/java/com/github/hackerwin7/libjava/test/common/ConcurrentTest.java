package com.github.hackerwin7.libjava.test.common;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentTest {

    private static boolean flag = false;
    private static Object obj = new Object();

    public static void main(String[] args) throws Exception {
        processor();
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

    public static void callableTest() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Task task = new Task();
        Future<Integer> result = executor.submit(task);
        executor.shutdown();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main threading");
        try {
            System.out.println("task return = " + result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("ended.");
    }

    public static void callableTest1() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Task task = new Task();
        FutureTask<Integer> ft = new FutureTask<>(task);
        executor.submit(ft);
        executor.shutdown();

//        Task task = new Task();
//        FutureTask<Integer> ft = new FutureTask<>(task);
//        Thread thread = new Thread(ft);
//        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main threading");
        try {
            System.out.println("task return = " + ft.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("ended.");
    }

    public static void barrierTest() {
        final CyclicBarrier cb = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("Ended.");
            }
        });
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("t0 starting");
                        try {
                            cb.await(2000, TimeUnit.MILLISECONDS);
                        } catch (Exception  e) {
                            e.printStackTrace();
                        }
                        System.out.println("t0 ended");
                    }
                }).start();
            } else if (i == 1){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("t1 starting");
                        try {
                            cb.await();
                        } catch (Exception  e) {
                            e.printStackTrace();
                        }
                        System.out.println("t1 ended");
                    }
                }).start();
            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("t1 starting");
                        try {
                            cb.await();
                        } catch (Exception  e) {
                            e.printStackTrace();
                        }
                        System.out.println("t1 ended");
                    }
                }).start();
            }
        }
    }

    public static void countDownLatchTest() {
        final CountDownLatch cdl = new CountDownLatch(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " starting");
                try {
                    cdl.await(2000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end");
            }
        }, "t0").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " starting");
                try {
                    Thread.sleep(1000);
                    cdl.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end");
            }
        }, "t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " starting");
                try {
                    Thread.sleep(5000);
                    cdl.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " end");
            }
        }, "t2").start();
    }

    public static void threadLocalTest() {
        ThreadLocal<Integer> longLocal = new ThreadLocal<>();
        ThreadLocal<String> stringLocal = new ThreadLocal<>();

        Integer i = 123;
        String str = "132";

        try {
            i = longLocal.get();
            str = stringLocal.get();
        } catch (Exception e) {
            System.out.println("dddd");
            e.printStackTrace();
        }

        System.out.println("str = " + str + ", i = " + i);
    }

    public static void threadReuseTest() throws Exception {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("th");
            }
        });
        th.start();
        Thread.sleep(3000);
        System.out.println(th.isAlive());
        //th.start();
        th = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thh");
            }
        });
        th.start();
    }

    public static void executorsTest() {
        //ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        executor.schedule(new ScheduleTask("1"), 1, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new ScheduleTask("2"), 0, 10, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new ScheduleTask("3"), 0, 2, TimeUnit.SECONDS); // if task exceed period, the scheduler will have to retain the rate, the next task will immediate run after "timeout" task ended
        executor.scheduleWithFixedDelay(new ScheduleTask("4"), 0, 1, TimeUnit.SECONDS);
    }
    public static void printABTest() {
        new Thread(new MyRunnable1()).start();
        new Thread(new MyRunnable2()).start();
    }

    static class MyRunnable1 implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (obj) {
                    if (flag) {
                        try {
                            System.out.println("1 waiting...");
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println('A');
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    flag = true;
                    obj.notify();
                }
            }
        }
    }

    static class MyRunnable2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (obj) {
                    if (!flag) {
                        try {
                            System.out.println("2 waiting...");
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println('B');
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    flag = false;
                    obj.notify();
                }
            }
        }
    }

    static class A {}

    public static void java8Syntax() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("new syntax");
            }
        }).start();
    }

    static class TestSync2 implements Runnable {
        int b = 100;
        synchronized void m1() throws InterruptedException {
            b = 1000;
            Thread.sleep(500);
            System.out.println("b=" + b);
        }
        synchronized void m2() throws InterruptedException {
            Thread.sleep(250);
            b = 2000;
        }
        public static void main(String[] args) throws InterruptedException {
            TestSync2 tt = new TestSync2();
            Thread t = new Thread(tt);
            t.start();
            System.out.println("ddd b = " + tt.b);
            tt.m2();
            System.out.println("ddd b = " + tt.b);
            System.out.println("main thread b = " + tt.b);
        }
        @Override
        public void run() {
            try {
                m1();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    static void processor() {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}

class Task implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("sub-threading");
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 100; i++)
            sum += i;
        return sum;
    }
}

class ScheduleTask implements Runnable {
    private String id;
    public ScheduleTask(String id) {
        this.id = id;
    }
    @Override
    public void run() {
        System.out.println(new Date() + ", " + id + " ...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}