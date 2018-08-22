package com.github.hackerwin7.libjava.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/05/14
 * Time: 11:28 AM
 * Desc:
 */
public class ConsumerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerTest.class);

    public static void main(String[] args) {
        multipleConsumersRun();
    }

    public static void multipleConsumersRun() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer1 = new KafkaConsumer<String, String>(props);
        consumer1.subscribe(Arrays.asList("ct"));

        KafkaConsumer<String, String> consumer2 = new KafkaConsumer<String, String>(props);
        consumer2.subscribe(Arrays.asList("ct"));
//        consumer2.assign(Arrays.asList(new TopicPartition("ct", 0),
//                                       new TopicPartition("ct", 1),
//                                       new TopicPartition("ct", 2)));

        int loopCnt = 0;
        while (true) {
            loopCnt++;
            ConsumerRecords<String, String> records;

            records = consumer1.poll(1000);
            for (ConsumerRecord<String, String> record : records)
                LOG.info("{}, {}, {}", record.offset(), record.key(), record.value());

            LOG.info("between loop cnt = " + loopCnt);

            records = consumer2.poll(1000);
            for (ConsumerRecord<String, String> record : records)
                LOG.info("{}, {}, {}", record.offset(), record.key(), record.value());

            LOG.info("ended loop cnt = " + loopCnt);
        }
    }
}
