package com.github.hackerwin7.libjava.test.common;

import lombok.extern.slf4j.Slf4j;
import org.byted.infsec.client.Identity;
import org.byted.infsec.client.InfSecException;
import org.byted.infsec.client.SecTokenC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author : wenqi.jk
 * @since : 8/24/24, 11:11
 **/
@Slf4j
public class TokenTest {
  public static void main(String[] args) {
    testParseToken();
  }

  public static void testParseToken() {
    try {
      String content = Files.readString(Paths.get(Objects.requireNonNull(GsonTest.class.getClassLoader().getResource("token_file")).toURI()));
      log.info("token: {}", content);
      Identity identity = SecTokenC.parseToken(content);
      log.info("identity: {}", identity);
      log.info("identity.User: {}", identity.User);
    } catch (IOException | URISyntaxException | InfSecException e) {
      log.error("error: {}", e.getMessage(), e);
    }
  }
}
