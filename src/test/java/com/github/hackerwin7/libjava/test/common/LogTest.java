package com.github.hackerwin7.libjava.test.common;

import com.github.hackerwin7.libjava.common.LogInstanceMarker;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wenqi.jk
 * @since : 9/15/24, 13:09
 **/
@Slf4j
public class LogTest {
  public static void main(String[] args) {
    testLoggerMarker();
  }

  public static void testLoggerMarker() {
    LogInstanceMarker.loadInstanceMarker(123L);
    log.info("test log marker");
  }
  public static void testLoggerName() {
    log.info("123123");
    log.info("logger name is: {}, log class name: {}", log.getName(), log.getClass().getName());
  }
}
