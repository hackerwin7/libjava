package com.github.hackerwin7.libjava.test.common;

import java.util.Set;

/**
 * @author : wenqi.jk
 * @since : 12/11/24, 15:49
 **/
public class CollectionTest {
  public static void main(String[] args) {
    testContainsIgnoreCase();
  }

  public static void testContainsIgnoreCase() {
    Set<String> set = Set.of("aBc", "dEf", "gHi");
    String abc = "abc";
    System.out.println(set.contains(abc));
    System.out.println(set.stream().anyMatch(abc::equalsIgnoreCase));
  }
}
