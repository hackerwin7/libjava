package com.github.hackerwin7.libjava.test.common;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author : wenqi.jk
 * @since : 2/17/25, 13:54
 **/
public class HashTest {

  public static void main(String[] args) {
    testIntHash1200();
  }

  private static void testIntHash1200() {
    int max = 1200;
    Map<Integer, List<Integer>> map = new TreeMap<>();
    // sort by hash
    for (int i = 0; i < max; i++) {
      // sort by hash num and print the original num
      int hash = hashNum(BigInteger.valueOf(i), max);
      if (!map.containsKey(hash)) {
        map.put(hash, new java.util.ArrayList<>());
      }
      map.get(hash).add(i);
    }
    for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
      System.out.println(entry.getKey() + ":" + entry.getValue());
    }
  }

  private static int hashNum(BigInteger num, int mod) {
    return Math.abs(numHash64(num).mod(BigInteger.valueOf(mod)).intValue());
  }

  // https://github.com/aappleby/smhasher/blob/master/src/MurmurHash3.cpp#L81
  private static BigInteger numHash64(BigInteger x) {
    x = x.xor(x.shiftRight(33));
    x = x.multiply(new BigInteger("18397679294719823053")); // 0xff51afd7ed558ccdL
    x = x.xor(x.shiftRight(33));
    x = x.multiply(new BigInteger("14181476777654086739")); // 0xc4ceb9fe1a85ec53L
    x = x.xor(x.shiftRight(33));
    return x;
  }
}
