package com.github.hackerwin7.libjava.test.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author : wenqi.jk
 * @since : 5/15/24, 16:13
 **/
public class JsonTest {
  public static void main(String[] args) throws Exception {
    readJsonFile();
  }

  public static void readJsonFile() throws Exception {
    Gson gson = new Gson();
    JsonObject json = gson.fromJson(new BufferedReader(new FileReader("/Users/bytedance/Documents/notes.json")), JsonObject.class);
    System.out.println(json);
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("body", json.toString());
    System.out.println(jsonObject);
  }
}
