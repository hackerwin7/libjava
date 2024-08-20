package com.github.hackerwin7.libjava.test.common;

import dp_portal.utils.parser.ParserTool;
import parser.HiveSchema;

/**
 * @author : wenqi.jk
 * @since : 8/7/24, 14:58
 **/
public class LibTest {
  public static void main(String[] args) {
    testJsonParse();
  }

  public static void testJsonParse() {
    String content =
        "{\n\t\"b_user_id\": 100059750626,\n\t\"date\": \"20211119\",\n\t\"extend_info\": [{\"level\":\"4\"}],\n\t\"tag\": 13\n}";
    System.out.println(content);
    HiveSchema hiveSchema = ParserTool.parseJson(content);
    System.out.println(hiveSchema);


//    String content1 =
//        "{\n\t\"b_user_id\": 100059750626,\n\t\"date\": \"20211119\",\n\t\"extend_info\": [map<\"level\":\"4\">],\n\t\"tag\": 13\n}";
//    System.out.println(content1);
//    HiveSchema hiveSchema1 = ParserTool.parseJson(content1);
//    System.out.println(hiveSchema1);
  }
}
