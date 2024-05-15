package com.github.hackerwin7.libjava.test.common;

import com.github.hackerwin7.libjava.utils.EscapeUtils;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;

/**
 * @author : wenqi.jk
 * @since : 3/20/24, 17:56
 **/
@Slf4j
public class StringTest {
  public static void main(String[] args) {
    testEscape();
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
