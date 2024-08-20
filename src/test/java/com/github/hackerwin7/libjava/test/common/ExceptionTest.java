package com.github.hackerwin7.libjava.test.common;

/**
 * @author : wenqi.jk
 * @since : 7/27/24, 17:40
 **/
public class ExceptionTest {
  public static void main(String[] args) {
    runtimeExceptionTest();
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
