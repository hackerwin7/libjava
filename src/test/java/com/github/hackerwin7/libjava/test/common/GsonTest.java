package com.github.hackerwin7.libjava.test.common;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/**
 * @author : wenqi.jk
 * @since : 8/13/24, 20:21
 **/
public class GsonTest {

  @Data
  public static class BmqClusterListResponse {

    private Map<String, ClusterDTO> data;

    private String msg;

    private Integer code;

    @Data
    public static class ClusterDTO {

      @SerializedName("cluster_name")
      private String clusterName;
    }
  }
  public static void main(String[] args) throws Exception {
    testGsonFrom();
  }

  public static void testGsonFrom() throws Exception {
    Gson gson = new Gson();
    // read string from file
    String content = Files.readString(Paths.get(Objects.requireNonNull(GsonTest.class.getClassLoader().getResource("json_file")).toURI()));
//    System.out.println(content);
    BmqClusterListResponse resp = gson.fromJson(content, BmqClusterListResponse.class);
    System.out.println(resp);
  }
}
