package com.github.hackerwin7.libjava.test.common;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/01/22
 * Time: 10:44 AM
 * Desc:
 */
public class JDKTest {

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

    private static void sortMapTest() {
        HashMap<Integer, Integer> map = new LinkedHashMap<>();
        map.put(1, 1);
        map.put(3, 3);
        map.put(10, 10);
        map.put(0, 0);
        map.put(-1 , -1);
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }

        Set<Map.Entry<Integer, Integer>> set = map.entrySet();
        LinkedList<Map.Entry<Integer, Integer>> list = new LinkedList<>(set);
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                //return o1.getValue().compareTo(o2.getValue());
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        System.out.println(list);
        Map<Integer, Integer> newMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list)
            newMap.put(entry.getKey(), entry.getValue());
        for (Map.Entry<Integer, Integer> entry: newMap.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }

    private static void classLoaderTest() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.println(loader);
        System.out.println(loader.getParent());
        System.out.println(loader.getParent().getParent());
    }

    private static void classLoaderTest1() throws Exception {
        ClassLoader loader = JDKTest.class.getClassLoader();
        System.out.println(loader);
        String prefix = JDKTest.class.getPackage().getName() + ".";
        System.out.println("prefix = " + prefix);
//        loader.loadClass(prefix + "Test2");
        Class.forName(prefix + "Test2");
//        Class.forName(prefix + "Test2", false, loader);
        System.out.println(JDKTest.class.getPackage().getName());
    }

    private static void classLoaderLoopTest() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + " start");
                DeadLoopClass dlc = new DeadLoopClass();
                System.out.println(Thread.currentThread() + " end");
            }
        };
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        t1.start();
        t2.start();
    }

    private static void classLoaderArrayTest() {
//        Test2 t = new Test2();
        Test2[] t2 = new Test2[10];
    }

    private static void constClassLoaderTest() {
        System.out.println(ConstClass.h);
    }

    private static void classLoaderStaticTest() {
        StaticTest.staticFunc();
    }

    private static void reflectionTest() {
        Class c = ReflectionTest.class;

        String className = c.getName();
        try {
            Constructor[] constructors = c.getDeclaredConstructors();
            System.out.println(constructors.length);
            for (int i = 0; i < constructors.length; i++) {
                int mod = constructors[i].getModifiers();
                System.out.println(mod);
                System.out.print(Modifier.toString(mod) + " " + className + "(");
                Class[] parameterTypes = constructors[i].getParameterTypes();
                for (int j = 0; j < parameterTypes.length; j++) {
                    System.out.print(parameterTypes[j].getName());
                    if (parameterTypes.length > j + 1)
                        System.out.print(", ");
                }
                System.out.println(")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void reflectionMethodTest() {
        Class c = ReflectionTest.class;
        String className = c.getName();

        Method[] methods = c.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            int mod = methods[i].getModifiers();
            System.out.print(Modifier.toString(mod) + " ");
            System.out.print(methods[i].getReturnType().getName());
            System.out.print(" " + methods[i].getName() + "(");
            Class[] parameterTypes = methods[i].getParameterTypes();
            for (int j = 0; j < parameterTypes.length; j++) {
                System.out.print(parameterTypes[j].getName());
                if (parameterTypes.length > j + 1)
                    System.out.print(", ");
            }
            System.out.println(")");
        }
    }

    public static void main(String[] args) throws Exception {
        reflectionMethodTest();
    }
}

class ReflectionTest {

    int a;
    double b;
    String c;

    ReflectionTest() {
        a = 1;
        b = 2.0d;
        c = "ccccc";
    }

    public ReflectionTest(int i) {
        System.out.println(i);
    }

    private ReflectionTest(int a, int b, int c) {

    }

    protected ReflectionTest(String tt) {

    }

    public void run() {

    }

    public void run(int a, int b) {

    }

    private int run(String cc) {
        return 0;
    }
}

class StaticTest {
    static StaticTest st = new StaticTest();

    static {
        System.out.println("1");
    }

    {
        System.out.println("2");
    }

    StaticTest() {
        System.out.println("3");
        System.out.println("a = " + a + ", b = " + b);
    }

    public static void staticFunc() {
        System.out.println("4");
    }

    int a = 110;
    static int b = 112;
}

class ConstClass {
    static {
        System.out.println("const static block");
    }

    public static final String h = "hhhhh";
}

class DeadLoopClass {
    static {
        if (true) {
            System.out.println(Thread.currentThread() + " init DeadLoopClass");
//            while (true) {
//
//            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Test2 {
    static int t = 1;
    int k = 0;

    static {
        System.out.println("init static block!");
    }

    static {
        s = 0;
//        System.out.println(s); // static block can only change static value after it, can not access the static value after the static block
    }

    static int s = 1;

    public Test2() {
        k = 111;

    }
}

class MyClassLoader extends ClassLoader {

    private String root;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        if (classData == null)
            throw new ClassNotFoundException();
        else
            return defineClass(name, classData, 0, classData.length);
    }

    private byte[] loadClassData(String className) {
        String fileName = root + File.separator + className.replace('.', File.separatorChar) + ".class";
        try {
            InputStream ins = new FileInputStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}