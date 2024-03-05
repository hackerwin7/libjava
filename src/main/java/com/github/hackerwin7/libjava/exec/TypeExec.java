package com.github.hackerwin7.libjava.exec;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author : wenqi.jk
 * @since : 3/3/24, 14:09
 **/
public class TypeExec {

  public static void main(String[] args) {
    test1();
  }

  public static void test1() {
    Long longData = 1705925616L;
    BigInteger bigInteger = BigInteger.valueOf(longData);
    System.out.println(bigInteger.longValue());
    System.out.println(new Date(bigInteger.longValue()));
    System.out.println(new Date(bigInteger.longValue() * 1000));
    System.out.println(new Date(bigInteger.longValue() * 1000 * 1000));
  }

}
