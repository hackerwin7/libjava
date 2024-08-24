package com.github.hackerwin7.libjava.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * @author : wenqi.jk
 * @since : 5/15/24, 16:13
 **/
public class JsonTest {
  public static void main(String[] args) throws Exception {
    testJson2LiteralString();
  }

  public static void testJson2LiteralString() throws Exception {
    String json = "{\n" +
        "  \"region\": \"cn\",\n" +
        "  \"projectId\": 1,\n" +
        "  \"dataSourceType\": \"hive\",\n" +
        "  \"subtype\": \"internal\"\n" +
        "}";
    System.out.println(json);
    // convert to pure string
//    JsonNode jsonNode = JsonMapper.builder().build().readTree(json);
//    String jsonString = jsonNode.toString();
//    System.out.println(jsonString);
//    // print jsonString with escape quote
//    System.out.println(jsonString.replace("\"", "\\\""));
//    // print jsonString with escape quote and new line
//    System.out.println(jsonString.replace("\"", "\\\"")
//                                .replace("\n", "\\n")
//                                .replace("\r", "\\r"));
    System.out.println(json.replace("\n", "")
        .replace("\r", "")
        .replace(" ", "")
        .replace("\"", "\\\""));
    String json2 = "{\n" +
        "  \"code\": 0,\n" +
        "  \"message\": \"success\",\n" +
        "  \"data\": [\n" +
        "    {\n" +
        "      \"name\": \"default\",\n" +
        "      \"displayName\": \"国内\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"name\": \"lq\",\n" +
        "      \"displayName\": \"灵丘\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"rootCause\": null\n" +
        "}";
    System.out.println(json2LiteralString(json2));
    // convert json2 string to json
    JsonNode jsonNode = JsonMapper.builder().build().readTree(json2);
    System.out.println(jsonNode.toString());
    // convert json2 string to json
    JsonObject jsonObject = new Gson().fromJson(json2, JsonObject.class);
    System.out.println(jsonObject.toString());
    // convert json2 string to json
  }

  public static String json2LiteralString(String s) throws Exception {
    return s.replace("\n", "")
        .replace("\r", "")
        .replace(" ", "")
        .replace("\"", "\\\"");
  }

  public static void test2class() throws Exception {
    String json = "[{\"Host\":\"10.147.35.26\",\"Port\":4080,\"Tags\":{}}]";
    JsonMapper.Builder builder = JsonMapper.builder();

    // 空对象不抛异常
    builder.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    // 未知属性不抛异常
    builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // 不存在的枚举值转为 NULL
    builder.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    // 不解析带有时区的时间信息
    builder.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    // 允许使用单引号引用名称和字符串值 - 不是 JSON 规范
    builder.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    // 允许使用带有反斜杠的引用，即将 \" 当成 " 来使用 - 不是 JSON 规范
    builder.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
    // 允许 JSON 字符串包含未转义的控制字符（值小于32的 ASCII 字符，包括制表符和换行符） - 不是 JSON 规范
    builder.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true);
    JsonMapper jsonMapper = builder.build();

//    Consuls consuls = jsonMapper.readValue(json, Consuls.class);

//    ConsulDTO[] consulDTOS = jsonMapper.readValue(json, ConsulDTO[].class);
//    System.out.println(Arrays.toString(consulDTOS));
//    System.out.println(consulDTOS[0].getHost());

//    List<ConsulDTO> consulDTOS = jsonMapper.readValue(json, List.class);
//    System.out.println(consulDTOS);
//    System.out.println(consulDTOS.get(0).getHost()); // linkedHashMap cannot cast to ConsulDTO

    List<ConsulDTO> consulDTOS = jsonMapper.readValue(json, new TypeReference<>(){});
    System.out.println(consulDTOS);
    System.out.println(consulDTOS.get(0).getHost());

    JsonNode jsonNode = jsonMapper.readTree(json);
    System.out.println(jsonNode);
    System.out.println(jsonNode.get(0).get("Host").asText());

  }

  public static void readJsonFile() throws Exception {
    Gson gson = new Gson();
    JsonObject json = gson.fromJson(new BufferedReader(new FileReader("/Users/bytedance/Documents/notes.json")), JsonObject.class);
    System.out.println(json);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("body", json.toString());
    System.out.println(jsonObject);
  }

  @Data
  public static class Consuls {
    List<ConsulDTO> consulList;
  }

  @Data
  public static class ConsulDTO {

    @JsonProperty("Host")
    private String host;

    @JsonProperty("Port")
    private Integer port;

    @JsonProperty("Tags")
    private Tags tags;

    @Data
    static class Tags {

      private String kind;
      private String weight;
      private String env;
      private String cluster;
    }
  }
}
