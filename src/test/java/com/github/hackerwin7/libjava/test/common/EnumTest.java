package com.github.hackerwin7.libjava.test.common;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author : wenqi.jk
 * @since : 8/1/24, 15:49
 **/
public class EnumTest {
  public static void main(String[] args) {
    test1();
  }

  public static void test1() {
    System.out.println(Arrays.stream(EnumInstance.values()).map(EnumInstance::toString).collect(Collectors.toList()));
  }
}
