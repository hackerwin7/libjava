package com.github.hackerwin7.libjava.test.common;

import com.github.hackerwin7.libjava.utils.TimeUtils;

import java.util.Date;

/**
 * @author : wenqi.jk
 * @since : 7/11/24, 21:18
 **/
public class UtilTest {
  public static void main(String[] args) {
    testTimeUtils();
  }

  public static void testTimeUtils() {
    long currentTimestamp = System.currentTimeMillis();
    long roundedTimestamp = TimeUtils.roundDownTimeStampDate(currentTimestamp);
    System.out.println("Current timestamp: " + new Date(currentTimestamp));
    System.out.println("Rounded timestamp: " + new Date(roundedTimestamp));
  }
}
