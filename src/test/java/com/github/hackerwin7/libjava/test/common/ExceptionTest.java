package com.github.hackerwin7.libjava.test.common;

/**
 * @author : wenqi.jk
 * @since : 7/27/24, 17:40
 **/
public class ExceptionTest {
  public static void main(String[] args) {
    catchNpeTest();
  }

  public static void catchNpeTest() {
    try {
      String s = null;
      System.out.println(s.length());
    } catch (Exception e) {
      System.out.println("catch exception " + e.getClass().getName());
      System.out.println(e.getMessage());
    }
  }

  public static void tryResourceCatchTest() {
    try (AutoCloseable c1 = getStringThrow()) {
      throw new RuntimeException("test");
    } catch (Exception e) {
      System.out.println("catch exception " + e.getClass().getName());
      System.out.println(e.getMessage());
    }
  }

  public static AutoCloseable getStringThrow() {
    throw new RuntimeException("get string throw");
  }

  public static void runtimeExceptionTest() {
    try {
      throw new RuntimeException("test");
    } catch (Exception e) {
      System.out.println("catch exception " + e.getClass().getName());
      System.out.println(e.getMessage());
    }
  }
}
