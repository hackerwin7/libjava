package com.github.hackerwin7.libjava.exec;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UncheckedIOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private Producer producer;
    private Consumer consumer;

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

//        Thread.sleep(5 * 1000);
//
//        new Thread(() -> {
//            //other thread close the handle
//            LOG.info("Other threading close handler...");
//            if (producer != null)
//                producer.close();
//        }).start();
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
//        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 0);
//        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 600000);
//        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 600000 + 1000);
        if (enableAuth) {
            props.put("security.protocol", "SASL_PLAINTEXT");
            props.put("sasl.kerberos.service.name", "kafka");
        }

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        this.producer = producer;
        long i = 0;
        Random rand = new Random();
        int length = 256;
        while (i <= MAX_SEND) {
            int ilen = String.valueOf(i).length();
            long cur = System.currentTimeMillis();
            try {
                @SuppressWarnings("unchecked")
                ProducerRecord record = new ProducerRecord(topic, null, null);

//                ProducerRecord record = new ProducerRecord<String, String>(topic,
//                                                                           "key-" + i + "-" + cur,
//                                                                           StringUtils.repeat("*", length- ilen - 1) + "-" + i);
                LOG.info("Producing " + record);
                // async
//                producer.send(record, new Callback() {
//                    @Override
//                    public void onCompletion(RecordMetadata metadata, Exception exception) {
//                        if (exception != null)
//                            LOG.error("callback error: {}", exception.getMessage(), exception);
//                    }
//                });

                // sync
                producer.send(record, ((metadata, exception) -> {
                    if (exception != null)
                        LOG.error("callback error: {}", exception.getMessage(), exception);
                })).get();
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

//        Thread.sleep(10 * 1000);
//
//        new Thread(() -> {
//            //other thread close the handle
//            LOG.info("Other threading close handler...");
//            if (consumer != null)
//                consumer.close();
//        }).start();
    }

    public void consume(String brokers, String topic, String clientid, String groupId) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", StringUtils.isBlank(groupId) ? genId() : groupId); // if groupId empty, it will stay constant in one day
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("client.id", StringUtils.isBlank(clientid) ? genId() : clientid);
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        if (enableAuth) {
            props.put("security.protocol", "SASL_PLAINTEXT");
            props.put("sasl.kerberos.service.name", "kafka");
        }

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        this.consumer = consumer;
        consumer.assign(Collections.singleton(new TopicPartition(topic ,0)));
//        consumer.subscribe(Collections.singletonList(topic));
//        consumer.subscribe(Arrays.asList(topic, "tt1", "kky"));
        consumer.seekToEnd(new LinkedList<TopicPartition>()); // default empty args
        int readCnt = 0, turnCnt = 0;
        final AtomicBoolean running = new AtomicBoolean(true);
//        LOG.info("list topics = " + consumer.listTopics());
        boolean paused = false, resumed = false;
        while (running.get()) {
            turnCnt++;
            LOG.debug("polling count = " + turnCnt);
            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                LOG.info("==> " + records.count() + " records;");
                readCnt += records.count();
//                if (readCnt <= 10 && !paused && !resumed) {
//                    consumer.pause(Arrays.asList(new TopicPartition("tt", 0)));
//                    LOG.info("paused topic tt for partition 0.");
//                    paused = true;
//                }
//                if (turnCnt == 5) {
//                    consumer.resume(Arrays.asList(new TopicPartition("tt", 0)));
//                    LOG.info("resume topic tt for partition 0.");
//                    resumed = true;
//                    paused = false;
//                }
//                if (turnCnt == 5) {
//                    /*change assignment will clear the pause state of subscriptions, see source code of SubscriptionState*/
//                    consumer.subscribe(Arrays.asList(topic, "tt1", "kky"));
//                    LOG.info("re-subscribe to change assignment.");
//                }
                if(records.count() == 0)
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        LOG.warn("!!! interrupt exception, skip it.");
                    }
                else
                    for (ConsumerRecord<String, String> record : records) {
                        LOG.debug("Consumed topic = " + record.topic() + ", partition = "+ record.partition() +", offset = "+ record.offset() + ", timestamp = " + record.timestamp() +", key = "+ record.key() +", val = " + record.value());
                    }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        consumer.close();
    }

    private static String genId() {
        return "kpc-" + sdf.format(new Date());
    }
}
