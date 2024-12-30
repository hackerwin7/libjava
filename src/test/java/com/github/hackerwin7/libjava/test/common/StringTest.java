package com.github.hackerwin7.libjava.test.common;

import com.github.hackerwin7.libjava.utils.EscapeUtils;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.byted.security.zti.jwt.shaded.javax.xml.bind.DatatypeConverter;

import java.nio.charset.StandardCharsets;
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
    stringBytesTest();
  }

  public static void stringBytesTest() {
    String s = "";
    int bytesLen = s.getBytes().length;
    System.out.println(bytesLen);
  }

  public static void compareDateTimeStr() {
    String s1 = "2024-03-20 17:56:01";
    String s2 = "2024-03-18 17:56:01";
    System.out.println(s1.compareTo(s2));
  }

  public static void stringFormat() {
    String s = null;
    String ret = String.format(s, "123");
    System.out.println(ret);
  }

  public static void hexString() {
    String s = "\\x00\\x00\\x01\\x91\\x92\\xe9)\\xb4";
    System.out.println(s);
    System.out.println(unHexBytes(s));
  }

  public static String unHexBytes(String arg) {
    List<Byte> bytesList = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < arg.length(); i++) {
      if (arg.charAt(i) == '\\' && arg.charAt(i + 1) == 'x') {
        int hexInt = Integer.parseInt(arg.substring(i + 2, i + 4), 16);
        char c = (char) hexInt;
        sb.append(c);
//        System.out.println("hex char: " + c);
        bytesList.add((byte) hexInt);
        i += 3;
      } else {
        char c = arg.charAt(i);
        sb.append(c);
//        System.out.println("char: " + c);
        bytesList.add((byte) arg.charAt(i));
      }
    }
    byte[] bytes = new byte[bytesList.size()];
    for (int i = 0; i < bytesList.size(); i++) {
      bytes[i] = bytesList.get(i);
    }
//    System.out.println("string builder: " + sb);
    return new String(bytes);
  }

  public static String unHex(String arg) {
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < arg.length(); i++) {
      if (arg.charAt(i) == '\\' && arg.charAt(i + 1) == 'x') {
        str.append((char) Integer.parseInt(arg.substring(i + 2, i + 4), 16));
        i += 3;
      } else {
        str.append(arg.charAt(i));
      }
    }
    return str.toString();
  }

  public static void escapeString() {
    String s = "\\x00\\x00\\x01\\x91\\x92\\xe9)\\xb4";
    System.out.println(s);
    System.out.println(StringEscapeUtils.unescapeJava(s));
    byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
    String s1 = new String(bytes);
    System.out.println(s1);
    System.out.println(StringEscapeUtils.unescapeJava(s1));
    String s2 = "���)�";
    System.out.println(s2);
    byte[] bytes2 = s2.getBytes();
    System.out.println(new String(bytes2));
    System.out.println(StringEscapeUtils.unescapeJava(new String(bytes2)));
  }

  public static void testSplit() {
    String s = "id";
    String[] arr = s.split(",\\s*");
    System.out.println(Arrays.toString(arr));
  }

  public static void testStringBytes() {
    String s = "\\x00\\x00\\x01\\x91\\x93\\x16\\xa0]";
    byte[] bytes = s.getBytes();
    System.out.println(s);
    System.out.println(new String(bytes));
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
