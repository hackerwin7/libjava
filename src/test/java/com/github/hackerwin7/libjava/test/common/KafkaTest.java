package com.github.hackerwin7.libjava.test.common;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.record.TimestampType;

/**
 * @author : wenqi.jk
 * @since : 5/22/24, 19:10
 **/
public class KafkaTest {
  public static void main(String[] args) {
    test1();
  }

  public static void test1() {
    Headers headers = new RecordHeaders();
    headers.add("k1", "v1".getBytes());
    headers.add("k2", "v2".getBytes());
    ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 1, 1L, 123L, TimestampType.NO_TIMESTAMP_TYPE,
        1L, 1, 1, "123", "456", headers);
    System.out.println(record.headers().toString());
  }
}
