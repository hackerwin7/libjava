package com.github.hackerwin7.libjava.exec;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final boolean enableProduce;
    private final boolean enableConsume;
    private final boolean enableAuth;

    public KafkaProduceConsumeExecutor(boolean enableProduce, boolean enableConsume, boolean enableAuth) {
        this.enableProduce = enableProduce;
        this.enableConsume = enableConsume;
        this.enableAuth = enableAuth;
    }

    public KafkaProduceConsumeExecutor() {
        this(true, true, false);
    }

    public static void main(String[] args) throws Exception {
        String brokers = "localhost:9092", topic = "test", clientId = "", groupId = "";
        boolean enableProduce = true, enableConsume = true, enableAuth = false;
        switch (args.length) {
            default:
            case 7:
                groupId = args[6];
            case 6:
                enableAuth = !StringUtils.equals(args[5], "0");
            case 5:
                enableConsume = !StringUtils.equals(args[4], "0");
            case 4:
                enableProduce = !StringUtils.equals(args[3], "0");
            case 3:
                clientId = args[2];
            case 2:
                topic = args[1];
            case 1:
                brokers = args[0];
            case 0:
        }
        KafkaProduceConsumeExecutor executor = new KafkaProduceConsumeExecutor(enableProduce, enableConsume, enableAuth);
        executor.run(brokers, topic, clientId, groupId);
    }

    public void run(String brokers, String topic, String clientId, String groupId) throws Exception {
        if (enableProduce) {
            LOG.info("producing...");
            produce_start(brokers, topic, clientId);
        }
        if (enableConsume) {
            LOG.info("consuming...");
            consume_start(brokers, topic, clientId, groupId);
        }
    }

    public void produce_start(final String brokers, final String topic, final String clientId) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    produce(brokers, topic, clientId);
                } catch (Exception e) {
                    LOG.error("run exception: " + e.getMessage(), e);
                }
            }
        }).start();
    }

    public void produce(String brokers, String topic, String clientId) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("client.id", StringUtils.isBlank(clientId) ? genId() : clientId);
        if (enableAuth) {
            props.put("security.protocol", "SASL_PLAINTEXT");
            props.put("sasl.kerberos.service.name", "kafka");
        }

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        long i = 0;
        Random rand = new Random();
        while (i <= MAX_SEND) {
            long cur = System.currentTimeMillis();
            try {
                ProducerRecord record = new ProducerRecord<String, String>(topic, "key-" + i + "-" + cur, "msg-" + i + "-" + cur);
                LOG.info("Producing " + record);
                producer.send(record, new Callback() {
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

    public void consume_start(final String broker, final String topic, final String clientid, final String groupId) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consume(broker ,topic, clientid, groupId);
                } catch (Exception e) {
                    LOG.error("exception: " + e.getMessage(), e);
                }
            }
        }).start();
    }

    public void consume(String brokers, String topic, String clientid, String groupId) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", StringUtils.isBlank(groupId) ? genId() : groupId); // if groupId empty, it will stay constant in one day
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("client.id", StringUtils.isBlank(clientid) ? genId() : clientid);
        if (enableAuth) {
            props.put("security.protocol", "SASL_PLAINTEXT");
            props.put("sasl.kerberos.service.name", "kafka");
        }

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList(topic));
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
                        LOG.info("Consumed topic = " + record.topic() + ", partition = "+ record.partition() +", offset = "+ record.offset() + ", timestamp = " + record.timestamp() +", key = "+ record.key() +", val = " + record.value());
                        Thread.sleep(1000);
                    }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private static String genId() {
        return "kpc-" + sdf.format(new Date());
    }
}
