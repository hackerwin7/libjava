package com.github.hackerwin7.libjava.test.common;

import com.bytedance.data.commons.http.HttpConnectManager;

import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : wenqi.jk
 * @since : 9/11/24, 19:04
 **/
public class HttpTest {
  public static void main(String[] args) {
    testCreateTopic();
  }

  @SneakyThrows
  public static void testGetTopicDetail() {
    HttpConnectManager httpClient = new HttpConnectManager(60000, 60000, 60000,
        HttpConnectManager.createDefaultConnectionManager());
    String token = Files.readString(Paths.get(Objects.requireNonNull(GsonTest.class.getClassLoader().getResource("token_file")).toURI()));
    String token1 = Files.readString(Paths.get(Objects.requireNonNull(GsonTest.class.getClassLoader().getResource("token_file1")).toURI()));
    RequestBuilder builder = RequestBuilder.post("http://paas-gw-sg.byted.org/api/v2/rocketmq-platform/api/topic/info/basic");
    builder.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    builder.addHeader("region", "Singapore");
    Map<String, String> params = new HashMap<>();
    params.put("topic_id", token1);
    String body = "{\"topic_name\":\"mysql_stock_group_dorado_sam_test_20240911_094330_rmq_test_wq_dorado_alisg_to_maliva_v4\",\"mq\":\"RocketMQ\",\"cluster_id\":1393,\"region\":\"VA\",\"cluster_name\":\"rmq_dbus_stock2\",\"order_type\":1,\"message_size\":8192,\"qps\":100,\"service_tree_id\":3989664,\"broker_queue_num\":6}";
    String entity = httpClient.requestAndGetEntity(builder, params, null, null);
    System.out.println(entity);
  }

  @SneakyThrows
  public static void testCreateTopic() {
    HttpConnectManager httpClient = new HttpConnectManager(60000, 60000, 60000,
        HttpConnectManager.createDefaultConnectionManager());
    String token = Files.readString(Paths.get(Objects.requireNonNull(GsonTest.class.getClassLoader().getResource("token_file")).toURI()));
    String token1 = Files.readString(Paths.get(Objects.requireNonNull(GsonTest.class.getClassLoader().getResource("token_file1")).toURI()));
    RequestBuilder builder = RequestBuilder.post("http://paas-gw-us.byted.org/api/v2/rocketmq-platform/api/secret/create/topic");
    builder.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    builder.addHeader("region", "Americas");
    Map<String, String> params = new HashMap<>();
    params.put("secret_key", token1);
    params.put("secret_user", "tianboxiu");
    String body = "{\"topic_name\":\"mysql_stock_group_dorado_sam_test_20240911_094330_rmq_test_wq_dorado_alisg_to_maliva_v5\",\"mq\":\"RocketMQ\",\"cluster_id\":1393,\"region\":\"VA\",\"cluster_name\":\"rmq_dbus_stock2\",\"order_type\":1,\"message_size\":8192,\"qps\":100,\"service_tree_id\":3989664,\"broker_queue_num\":6}";
    String entity = httpClient.requestAndGetEntity(builder, params, body, ContentType.APPLICATION_JSON);
    System.out.println(entity);
  }
}
