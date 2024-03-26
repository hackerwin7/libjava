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
    CSV,
    /**
     * complex json
     */
    CJSON
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
    if (formatType == FormatType.CJSON) {
      Map<String, Object> map = Maps.newHashMap();
      map.put("appname", "huaishan");
      map.put("uid", 3338132944L);
      map.put("from", "tt");
      map.put("ver", "1.5.600");
      map.put("reserve", "{\"wxUserInfo\":{\"openid\":\"_0002xo9FuiFpCA489yk3w6mQmFfERCAc1k5\",\"unionid\":\"f3531ab9-fa8e-530e-a87e-24560ffee678\"},\"adExtend\":{\"ad_params\":\"{\\\"cid\\\":1793344530783321,\\\"ad_id\\\":1793343348268147,\\\"log_extra\\\":{\\\"group_type\\\":102,\\\"attributed_material_items\\\":\\\"[{\\\\\\\"item_type\\\\\\\":9,\\\\\\\"mid\\\\\\\":7156508405497692174},{\\\\\\\"item_type\\\\\\\":103,\\\\\\\"mid\\\\\\\":6939774746863271947},{\\\\\\\"item_type\\\\\\\":105,\\\\\\\"mid\\\\\\\":7148737045779365896},{\\\\\\\"item_type\\\\\\\":100,\\\\\\\"mid\\\\\\\":7343231371836293170},{\\\\\\\"item_type\\\\\\\":106,\\\\\\\"mid\\\\\\\":7111996612092985351},{\\\\\\\"item_type\\\\\\\":104,\\\\\\\"mid\\\\\\\":7341010125630930971},{\\\\\\\"item_type\\\\\\\":53,\\\\\\\"mid\\\\\\\":7343211273108078642}]\\\",\\\"pigeon_num\\\":3203710,\\\"is_pack_v2\\\":true,\\\"convert_id\\\":0,\\\"disable_ad_label_display_by_sati\\\":0,\\\"real_site_id\\\":\\\"7343194969127010316\\\",\\\"variation_types\\\":\\\"1001\\\",\\\"orit\\\":40001,\\\"wdsignals\\\":0,\\\"jdsignals\\\":0,\\\"ad_price\\\":\\\"ZfKtIAAPogdl8q0gAA-iB1oWL0kPRgxr3MAVnA\\\",\\\"landing_type\\\":16,\\\"style_ids\\\":[600009],\\\"card_id\\\":0,\\\"material_info\\\":\\\"type:19,mid:16657493,source:0,p_ids:[],id:0|type:53,mid:7343211273108078642,source:0,p_ids:[],id:0|type:10,mid:7343251331668397322,source:0,p_ids:[],id:7343251331668397322|type:9,mid:7156508405497692174,source:0,p_ids:[],id:463484300166475352\\\",\\\"clickid\\\":\\\"ENm43v2Y4ZcDGPz3gNrw9PgEIJfOoMHTjYEEMAw4wbgCQiIyMDI0MDMxNDE1NTQwNkEwQjIwRUY3QTg2OTkwODlFQTg4SMG4ApABAA\\\",\\\"rit\\\":40001,\\\"component_ids\\\":[800532,800545,800610],\\\"ad_item_id\\\":7343251331668397322,\\\"reward_again_mark\\\":0,\\\"is_pack_ng\\\":1,\\\"ad_show_type\\\":0,\\\"hyrule_atype\\\":[14],\\\"bdid\\\":\\\"ce3ef3e31466d9480a67a2681c8947051daf84e265292ae39017c5151625734e\\\",\\\"content_type\\\":1,\\\"ad_recommend_flag\\\":0,\\\"price\\\":1216719,\\\"pricing_type\\\":9,\\\"attributed_site_id\\\":\\\"7343194969127010316\\\",\\\"style_id\\\":600009,\\\"external_action\\\":14,\\\"ad_author_id\\\":2140103725031931,\\\"render_type\\\":\\\"h5\\\",\\\"req_id\\\":\\\"20240314155406A0B20EF7A8699089EA88\\\",\\\"maca\\\":\\\"\\\",\\\"compliance_data\\\":\\\"{\\\\\\\"biz_type\\\\\\\":\\\\\\\"ad\\\\\\\",\\\\\\\"ad\\\\\\\":{\\\\\\\"landing_type\\\\\\\":16,\\\\\\\"pricing_type\\\\\\\":9,\\\\\\\"market_online_status\\\\\\\":31,\\\\\\\"content_type\\\\\\\":1,\\\\\\\"is_dsp\\\\\\\":false,\\\\\\\"dsp_type\\\\\\\":0,\\\\\\\"platform_version\\\\\\\":2,\\\\\\\"group_type\\\\\\\":102}}\\\",\\\"convert_component_suspend\\\":0,\\\"isoid\\\":\\\"\\\",\\\"real_material_items\\\":\\\"[{\\\\\\\"item_type\\\\\\\":9,\\\\\\\"mid\\\\\\\":7156508405497692174},{\\\\\\\"item_type\\\\\\\":100,\\\\\\\"mid\\\\\\\":7343231371836293170},{\\\\\\\"item_type\\\\\\\":106,\\\\\\\"mid\\\\\\\":7111996612092985351},{\\\\\\\"item_type\\\\\\\":104,\\\\\\\"mid\\\\\\\":7341010125630930971},{\\\\\\\"item_type\\\\\\\":103,\\\\\\\"mid\\\\\\\":6939774746863271947},{\\\\\\\"item_type\\\\\\\":105,\\\\\\\"mid\\\\\\\":7148737045779365896},{\\\\\\\"item_type\\\\\\\":53,\\\\\\\"mid\\\\\\\":7343211273108078642}]\\\",\\\"variation_id\\\":\\\"7343194969127010316\\\"},\\\"is_soft_ad\\\":0,\\\"web_url\\\":\\\"https:\\\\/\\\\/www.chengzijianzhan.com\\\\/tetris\\\\/page\\\\/7343194969127010316\\\\/?projectid=7343223544295424039&promotionid=7343231362503458842&creativetype=15&clickid=B.eBa7IEwdefDUhKkmgOvZ5Q3VzUMqOF2MCKtVXkileEfWT17Cs2yujYVi4cYwR789nSU6IoSwCLyFTzriF73VCF6PzgSi2msuBijxnDOclCQi32Geh5zUaGGaMpglfnTdhB&mid1=0&mid2=7156508405497692174&mid3=7343211273108078642&mid4=__MID4__&mid5=7343231371836293170&ad_id=1793343348268147&cid=1793344530783321&req_id=20240314155406A0B20EF7A8699089EA88\\\",\\\"web_title\\\":\\\"广州华多\\\",\\\"intercept_flag\\\":2}\",\"advertiser_id\":\"1789035251312729\",\"channelId\":\"1\",\"clickid\":\"B.DP2lmE6safpTcaQjcXoCE0d1MFjqThNjgSb1FpYpn4bNVvLwaL7OiVJizhBHtz3fUiSHBVCWYRuYaeqYxe7KhC9nZQJRbTW3Axx4zBHuSBIxbbDfw8ZKNDDNmUwyfzpuw\",\"creativetype\":\"15\",\"dramaId\":\"65dca4b651551c5649db4649\",\"materialId\":\"7978580\",\"pageId\":\"a03w421ge04paf083sf4n21x\",\"payTemplateType\":\"customized\",\"projectid\":\"7343223544295424039\",\"promotionid\":\"7343231362503458842\",\"requestid\":\"20240314155406A0B20EF7A8699089EA88\",\"tt_album_id\":\"7340162829644890660\",\"tt_episode_id\":\"7340162848884310578\",\"verifyId\":\"\",\"videoId\":\"65dca4c63ddab23c63e451ad\"},\"from\":\"tt\"}");
      map.put("unionid", "f3531ab9-fa8e-530e-a87e-24560ffee678");
      map.put("scene", "990001");
      map.put("time", 1710405788L);
      map.put("appkey", "am7rnon4n89z82p1s4tycuqi1pyt9dx8");
      map.put("model", "ELS-AN00");
      map.put("openid", "_0002xo9FuiFpCA489yk3w6mQmFfERCAc1k5");
      map.put("initTime", "1710405788362");
      map.put("ip", "106.38.221.125");
      return JSON.toJSONString(map);
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
