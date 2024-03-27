package com.github.hackerwin7.libjava.test.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : wenqi.jk
 * @since : 3/26/24, 19:41
 **/
@Slf4j
public class ConvertTest {
  public static void main(String[] args) {
    test2();
  }

  public static void test1() {
//    Map<String, Object> map = new LinkedHashMap<>();
    Map<String, Object> map = new HashMap<>();
    map.put("1", "1123111");
    Object object = (Object) map;
//    JSONObject jsonObject = (JSONObject) object;
    JSONObject jsonObject = (JSONObject) JSON.toJSON(object);
    log.info(jsonObject.getString("1"));
  }

  public static void test2() {
    ConnectionInfo info = new ConnectionInfo();
    info.setUrl("1313131313");
    Object infoObj = (Object) info;
    JSONObject jsonObject = (JSONObject) infoObj;
    log.info(jsonObject.getString("db_url"));
  }
}
