package com.github.hackerwin7.libjava.test.common;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author : wenqi.jk
 * @since : 12/12/25, 17:10
 **/
public class URITest {

  public static void main(String[] args) {
    testHdfsUri();
  }

  public static void testHdfsUri() {
    String  hdfsUri = "hdfs://harunacompass";
    try {
      URI uri = new URI(hdfsUri);
      System.out.println(uri);
      System.out.println(uri.getScheme());
      System.out.println(uri.getAuthority());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
}
