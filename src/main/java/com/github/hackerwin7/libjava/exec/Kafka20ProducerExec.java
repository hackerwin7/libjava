package com.github.hackerwin7.libjava.exec;

import com.alibaba.fastjson2.JSON;
import com.github.hackerwin7.libjava.common.Utils;
import com.github.javafaker.Faker;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

/**
 * @author : wenqi.jk
 * @since : 3/12/24, 11:31
 **/
@Slf4j
public class Kafka20ProducerExec extends Thread {

  private static final int ARGS_COUNT = 4;

  private final String bootstrapServers;
  private final String topic;
  private final int numRecords;
  private final FormatType formatType;
  private final Map<String, String> fields;

  enum FormatType {
    /**
     * json
     */
    JSON,
    /**
     * csv
     */
    CSV
  }

  public Kafka20ProducerExec(String bootstrapServers, String topic, String formatType, int numProduced) {
    this.bootstrapServers = bootstrapServers;
    this.topic = topic;
    this.numRecords = numProduced;
    this.formatType = FormatType.valueOf(formatType.toUpperCase());
    this.fields = Maps.newHashMap();
  }

  /**
   * message sample:
   * key: 125
   * val: 125|gogo|235.127
   */
  @Override
  public void run() {
    int sentRecords = 0;
    Random random = new Random(System.currentTimeMillis());
    try (KafkaProducer<String, String> producer = createKafkaProducer()) {
      while (sentRecords < numRecords) {
        // id, name, price
        String key = generateKey(random);
        String value = generateValue(random);
        producer.send(new ProducerRecord<>(topic, key, value), (metadata, exception) -> {
          if (exception != null) {
            log.warn("Failed to send message.");
            log.error(exception.getMessage(), exception);
          }
        });
        sentRecords++;
      }
    }
  }

  private String generateValue(Random random) {
    Faker faker = new Faker();
    if (formatType == FormatType.JSON) {
      fields.clear();
      fields.put("id", String.valueOf(Math.abs(random.nextInt())));
      fields.put("name", faker.name().firstName());
      fields.put("address", faker.address().fullAddress());
      fields.put("price", String.valueOf(random.nextDouble()));
      return JSON.toJSONString(fields);
    }
    if (formatType == FormatType.CSV) {
      return StringUtils.join(Arrays.asList(Math.abs(random.nextInt()), faker.name().fullName(), random.nextDouble()), "|");
    }
    return "default";
  }

  private String generateKey(Random random) {
    return String.valueOf(Math.abs(random.nextInt()));
  }

  public KafkaProducer<String, String> createKafkaProducer() {
    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(CLIENT_ID_CONFIG, getClientId(topic));
    props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new KafkaProducer<>(props);
  }

  public static String getClientId(String topic) {
    return StringUtils.join(Arrays.asList("lib_java", "client", topic, UUID.randomUUID()), "-");
  }

  public static void main(String[] args) {
    if (args.length != ARGS_COUNT) {
      System.out.println("Usage: Kafka20ProducerExec <bootstrap_servers> <topic> <format_type:json|csv> <num_records>");
      System.exit(1);
    }
    String bootstrapServers = args[0];
    String topic = args[1];
    String formatType = args[2];
    int numRecords = Integer.parseInt(args[3]);
    Kafka20ProducerExec producerExec = new Kafka20ProducerExec(bootstrapServers, topic, formatType, numRecords);
    producerExec.start();

//    test1();
  }

  public static void test1() {
    Kafka20ProducerExec producerExec = new Kafka20ProducerExec("kafka-cnngue0c2fawaqrk.kafka.ivolces.com:9092",
        "csv_test", "json", 10000);
    Random random = new Random(System.currentTimeMillis());
    log.info(producerExec.generateKey(random));
    log.info(producerExec.generateKey(random));
    log.info(producerExec.generateKey(random));
    log.info(producerExec.generateValue(random));
    log.info(producerExec.generateValue(random));
    log.info(producerExec.generateValue(random));
  }
}
