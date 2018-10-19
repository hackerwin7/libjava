package com.github.hackerwin7.libjava.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.internals.NoOpConsumerRebalanceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/10/18
 * Time: 5:19 PM
 * Desc:
 */
public class KafkaConsumerThread extends Thread {
    // consumer settings
    public static org.apache.kafka.clients.consumer.KafkaConsumer<String, String> createNativeConsumer(String groupName, String kafkaBootstrap) {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaBootstrap);
        props.put("group.id", groupName);
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", true);
        props.put("metadata.max.age.ms", 5000);
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");


        return new KafkaConsumer<String, String>(props);
    }


    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerThread.class);
    private boolean stop = false;
    private KafkaConsumer<String, String> consumer;
    private String topicName;
    private ConsumerRebalanceListener consumerRebalanceListener;
    private AtomicLong receivedRecordNumber = new AtomicLong(0);


    public KafkaConsumerThread(String topicName, String groupName, ConsumerRebalanceListener consumerRebalanceListener, String kafkaBootstrap) {
        this.consumer = createNativeConsumer(groupName, kafkaBootstrap);
        this.topicName = topicName;
        this.consumerRebalanceListener = consumerRebalanceListener;
    }


    @Override
    public void run() {
        log.info("Start consumer ..");
        consumer.subscribe(Collections.singleton(topicName), consumerRebalanceListener);
        while (!stop) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(100);
                log.info("polling...");
                receivedRecordNumber.addAndGet(records.count());
                Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
                while (iterator.hasNext()) {
                    ConsumerRecord<String, String> record = iterator.next();
                    log.info("Receive [part:{}] [key:{}][value:{}]", record.partition(), record.key(), record.value());
                }
                Thread.sleep(3000);
            } catch (Exception e) {
                log.error(e.getMessage());
                log.info("no data");
            }
        }
        consumer.close();
    }


    public void stopConsumer() {
        this.stop = true;
    }

    public static void main(String[] args) {
        KafkaConsumerThread kct = new KafkaConsumerThread("test-topic", "t-t-g", new NoOpConsumerRebalanceListener(), "localhost:9092");
        kct.start();
    }
}