package com.github.hackerwin7.libjava.test.common;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : wenqi.jk
 * @since : 9/30/25, 11:40
 **/
public class DateTest {

  public static void main(String[] args) {
//    testDateTransform();
    timestampTest();
  }

  public static void testDateTransform() {
    String dateStr = "2025-09-28 00:15:58.208556";
    String format = "yyyy-MM-dd HH:mm:ss";
    LocalDateTime dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(format));
    System.out.println(Timestamp.valueOf(dateTime));
  }

  public static void timestampTest() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    System.out.println(timestamp);
  }
}
