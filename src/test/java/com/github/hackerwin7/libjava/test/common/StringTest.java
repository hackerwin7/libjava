package com.github.hackerwin7.libjava.test.common;

import com.github.hackerwin7.libjava.utils.EscapeUtils;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author : wenqi.jk
 * @since : 3/20/24, 17:56
 **/
@Slf4j
public class StringTest {
  public static void main(String[] args) {
    testStartWith();
  }

  public static void testStartWith() {
    String oldTopic = "mysql_stock_group_dorado_pipo_refund_shard_20240807_075126_refund_order_deduction_info_dorado_awssg_to_maliva";
    String newTopic = "mysql_stock_group_dorado_pipo_refund_shard_20240807_075126_refund_order_deduction_info";
    System.out.println(oldTopic.startsWith(newTopic));
  }

  public static void testFormatNull() {
    String format = "%s|%s";
    String left = null;
    String right = "right";
    System.out.println(String.format(format, left, right));
  }

  public static void bytesStr() {
    byte[] arr = {31,37,31,39,32,33,34,37,31,39,36,35,34,30,37,31};
    String str = new String(arr);
    System.out.println(str);
  }

  public static void testPartitionStr() {
    String partition = "p_date=20240527/hour=11/zone=eu";
    List<String> partVals = new ArrayList<String>();
    String[] parts = partition.split(",");
    for (String part : parts) {
      String[] kv = part.split("=");
      partVals.add(kv[1]);
    }
    System.out.println(partVals);
  }

  public static void testEscape() {
    char c = ':';
    System.out.println(String.format("%1$02X", (int) c));
    char c1 = '%';
    System.out.println(String.format("%1$02X", (int) c1));
    String path1 = "/xxx/yyy/zzz:date_hour_xs_noval";
    String path2 = "zzz:date_hour_xs_noval";
    String ep2 = EscapeUtils.escapePathName(path2);
    System.out.println(ep2);
    System.out.println(EscapeUtils.escapePathName(ep2));
    System.out.println(EscapeUtils.escapePathName(EscapeUtils.escapePathName(ep2)));
    System.out.println(EscapeUtils.checkAndEscape(ep2));
    System.out.println(EscapeUtils.checkAndEscape(EscapeUtils.checkAndEscape(ep2)));
  }

  public static void test1() {
    Set<String> rets = getMetadataFields("_timestamp_, _offset_,    _topic_     ");
    rets.forEach(s -> log.info("it is---" + s + "---"));
  }

  public static Set<String> getMetadataFields(String metadataString) {
    if (StringUtils.isBlank(metadataString)) {
      return Sets.newHashSet();
    }
    return Sets.newHashSet(Arrays.stream(StringUtils.split(metadataString, ",")).map(String::trim).toArray(String[]::new));
  }
}
