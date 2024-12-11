package com.github.hackerwin7.libjava.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tianboxiu
 * 将用fastJson或jackJson代替
 */
@Deprecated
public class JsonUtils {
  private static Gson gson;
  private static JsonParser parser = new JsonParser();

  static {
    //gson默认会用double/float存储number类型的值,因此需要改变下存储方式
    gson = new GsonBuilder().registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
      if (src == src.longValue()) {
        return new JsonPrimitive(src.longValue());
      } else {
        return new JsonPrimitive(src);
      }
    }).create();
  }

  public static <T> List<T> parseArray(String text, Class<T> clazz) {
    if (text == null || text.equals("")) {
      return null;
    }
    try {
      Type type = new ListParameterizedType(clazz);
      return gson.fromJson(text, type);

    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * parse an object from one type to another Json type, if possible
   * 1. if source is a json string, it should not parse it to a json string, which will judge the object as a primitive string
   *
   * @param src
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T fromJson(Object src, Class<T> clazz) {
    if (src instanceof String) {
      return gson.fromJson((String) src, clazz);
    }
    return gson.fromJson(gson.toJson(src), clazz);
  }

  /**
   * Parses the specified JSON string into a parse tree
   *
   * @param json JSON text
   * @return a parse tree of {@link JsonElement}s corresponding to the specified JSON
   */
  public static JsonElement parse(String json) {
    return parser.parse(json);
  }

  /**
   * parse an object by analyzing a json file
   */
  public static <T> T fromFile(URL fileUrl, Class<T> clazz) throws IOException {
    String fileContent = IOUtils.toString(fileUrl);
    return gson.fromJson(fileContent, clazz);
  }

  public static String toJson(Object src) {
    return gson.toJson(src);
  }

  private static class ListParameterizedType implements ParameterizedType {

    private Type type;

    private ListParameterizedType(Type type) {
      this.type = type;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return new Type[]{type};
    }

    @Override
    public Type getRawType() {
      return ArrayList.class;
    }

    @Override
    public Type getOwnerType() {
      return null;
    }

    // implement equals method too! (as per javadoc)
  }

}
