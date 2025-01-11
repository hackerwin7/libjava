package com.github.hackerwin7.libjava.test.common;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author : wenqi.jk
 * @since : 8/1/24, 15:49
 **/
public class EnumTest {
  public static void main(String[] args) {
    testHttpMethods();
  }

  public static void testHttpMethods() {
    System.out.println(HTTPMethods.GET.value());
    System.out.println(HTTPMethods.GET.name());
    System.out.println(HTTPMethods.GET.ordinal());
    System.out.println(HTTPMethods.GET.getClass().getName());
  }

  public static void test1() {
    System.out.println(Arrays.stream(EnumInstance.values()).map(EnumInstance::toString).collect(Collectors.toList()));
  }

  public enum HTTPMethods {
    GET,
    POST,
    PUT,
    HEAD,
    DELETE;

    private HTTPMethods() {
    }

    public String value() {
      return this.name();
    }

    public static HTTPMethods fromValue(String v) {
      return valueOf(v);
    }
  }
}
