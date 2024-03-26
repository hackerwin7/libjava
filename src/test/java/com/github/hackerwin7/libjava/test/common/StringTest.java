package com.github.hackerwin7.libjava.test.common;

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
    test1();
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
