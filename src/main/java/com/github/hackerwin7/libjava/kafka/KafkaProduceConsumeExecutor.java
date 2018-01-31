package com.github.hackerwin7.libjava.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2017/10/24
 * Time: 10:44 AM
 * Desc:
 */
public class KafkaProduceConsumeExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaProduceConsumeExecutor.class);

    private static final Long MAX_SEND = 1000000L;

    public static void main(String[] args) throws Exception {
        if(args.length < 2) {
            args = new String[2];
            args[0] = "localhost:9092";
            args[1] = "kafka-monitor-topic-metrics";
        }

        KafkaProduceConsumeExecutor executor = new KafkaProduceConsumeExecutor();

//        LOG.info("producing...");
//        //executor.produce(args[0], args[1]);
//        executor.produce_start(args[0], args[1]);

        LOG.info("consuming...");
//        executor.consume(args[0], args[1]);
        executor.consume_start(args[0], args[1]);
    }

    public void produce_start(final String brokers, final String topic) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    produce(brokers, topic);
                } catch (Exception e) {
                    LOG.error("run exception: " + e.getMessage(), e);
                }
            }
        }).start();
    }

    public void produce(String brokers, String topic) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("security.protocol", "SASL_PLAINTEXT");
//        props.put("sasl.kerberos.service.name", "kafka");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        long i = 0;
        while (i <= MAX_SEND) {
            long cur = System.currentTimeMillis();
            try {
                producer.send(new ProducerRecord<String, String>(topic, "key-" + i + "-" + cur, "msg-" + i + "-" + cur), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception != null)
                            LOG.error(exception.getMessage(), exception);
                    }
                });
            } catch (Exception e) {
                LOG.error("exception: " + e.getMessage(), e);
            }
            Thread.sleep(1000);
            i++;
        }
        producer.close();
    }

    public void consume_start(final String broker, final String topic) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consume(broker, topic);
                } catch (Exception e) {
                    LOG.error("exception: " + e.getMessage(), e);
                }
            }
        }).start();
    }

    public void consume(String brokers, String... topics) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("security.protocol", "SASL_PLAINTEXT");
//        props.put("sasl.kerberos.service.name", "kafka");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topics));
        consumer.poll(0); // assign topic partitions
        consumer.seekToEnd(new LinkedList<TopicPartition>()); // default empty args
        while (true) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(100);
                LOG.info("==> " + records.count() + " records;");
                if(records.count() == 0)
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        LOG.warn("!!! interrupt exception, skip it.");
                    }
                else
                    for (ConsumerRecord<String, String> record : records) {
                        LOG.info("topic = " + record.topic() + ", partition = "+ record.partition() +", offset = "+ record.offset() + ", timestamp = " + record.timestamp() +", key = "+ record.key() +", val = " + record.value());
                        Thread.sleep(1000);
                    }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
