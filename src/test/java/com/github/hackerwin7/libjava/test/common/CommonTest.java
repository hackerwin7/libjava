package com.github.hackerwin7.libjava.test.common;

import com.github.javafaker.Bool;
import com.google.common.collect.Maps;
import org.apache.flink.table.descriptors.DescriptorProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author : wenqi.jk
 * @since : 9/3/24, 19:57
 **/
public class CommonTest {
  public static void main(String[] args) {
    testBoolean();
  }

  public static void testBoolean() {
//    Boolean b1 = null;
    Boolean b2 = false;
//    printBoolean(b1);
    printBoolean(b2);
  }

  private static void printBoolean(boolean bool) {
    System.out.println(bool);
  }

  public static void testDescriptorProperties() {
    DescriptorProperties descriptorProperties = new DescriptorProperties(true);
    Map<String, String> maps = Maps.newHashMap();
    maps.put("k1", "v1");
    maps.put("k2", "v2");
    maps.put("connector.properties.0.key", "k11111");
    maps.put("connector.properties.0.value", "v111111");
    maps.put("connector.properties.1.key", "k222");
    maps.put("connector.properties.1.value", "v222");
    descriptorProperties.putProperties(maps);
    List<Map<String, String>> dmaps = descriptorProperties.getFixedIndexedProperties("connector.properties", Arrays.asList("key", "value"));
    dmaps.forEach(kv -> System.out.println(descriptorProperties.getString(kv.get("key")) + ":" + descriptorProperties.getString(kv.get("value"))));
  }
}
