package com.github.hackerwin7.libjava.test.common;

import java.math.BigInteger;

/**
 * @author : wenqi.jk
 * @since : 8/8/24, 20:20
 **/
public class NumericTest {
  public static void main(String[] args) {
    testBigIntLoop();
  }

  public static void testBigIntLoop() {
    BigInteger min = new BigInteger("84501");
    BigInteger max = new BigInteger("8146525249999583279");
    long fetchSize = 50000;
    for (BigInteger start = min; start.compareTo(max) <= 0; start = start.add(BigInteger.valueOf(fetchSize))) {
      BigInteger end = start.add(BigInteger.valueOf(fetchSize));
      if (end.compareTo(max) > 0) {
        end = max.add(BigInteger.valueOf(1L));
      }
      System.out.println(start + "," + end);
    }
  }
}
