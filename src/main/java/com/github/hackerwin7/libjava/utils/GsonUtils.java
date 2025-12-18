package com.github.hackerwin7.libjava.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.typesafe.config.Config;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author kunpu.qiu
 * @date 2019/1/6
 */

public class GsonUtils {

    private static Gson GSON = new GsonBuilder().disableHtmlEscaping()
            .create();

    private static JsonParser PARSER = new JsonParser();

    public static final JsonObject EMPTY_JSON_OBJECT = new JsonObject();

    public static final JsonArray EMPTY_JSON_ARRAY = new JsonArray();


    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (text == null || text.equals("")) {
            return null;
        }
        try {
            Type type = new ListParameterizedType(clazz);
            return GSON.fromJson(text, type);

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static <T> T fromJson(Object src, Class<T> clazz) {
        if (src instanceof String) {
            return GSON.fromJson((String) src, clazz);
        }
        return GSON.fromJson(GSON.toJson(src), clazz);
    }

    public static <T> T fromJsonOrigin(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static JsonElement toJsonElement(Object src) {
        try {
            return GSON.toJsonTree(src);
        } catch (Throwable e) {
            return GSON.fromJson(toJson(src), JsonElement.class);
        }
    }

    public static String toJson(Object src, String root) {
        JsonObject json = new JsonObject();
        json.add(root, GSON.toJsonTree(src));
        return json.toString();
    }

    public static String toJson(Object src, Type typeOfSrc) {
        return GSON.toJson(src, typeOfSrc);
    }


    public static Map<String, Object> parseMap(String json) {
        // See https://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Generic-Types
        return GSON.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static <T> T config2Object(Config config, Class<T> clazz) {
        if (config == null) {
            return null;
        }
        Map<String, Object> map = config.root().unwrapped();
        return GSON.fromJson(GSON.toJson(map), clazz);
    }

    public static JsonElement safeGet(JsonArray arr, int i) {
        JsonElement result = i < arr.size() ? arr.get(i) : null;
        return result;
    }

    public static void merge(@NonNull JsonObject json, JsonObject anotherJson) {
        if (anotherJson == null) {
            return;
        }
        for (Map.Entry<String, JsonElement> entry : anotherJson.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (json.has(key)) {
                JsonElement originValue = json.get(key);
                if (value.isJsonObject() && originValue.isJsonObject()) {
                    merge(originValue.getAsJsonObject(), value.getAsJsonObject());
                } else if (value.isJsonArray() && originValue.isJsonArray()) {
                    JsonArray array = originValue.getAsJsonArray();
                    JsonArray anotherArray = value.getAsJsonArray();
                    Set<JsonElement> valueSet = new HashSet<>();
                    array.forEach(x -> valueSet.add(x));
                    anotherArray.forEach(x -> {
                        if (!valueSet.contains(x)) {
                            array.add(x);
                        }
                    });
                }
            } else {
                json.add(key, value);
            }
        }
    }

    /**
     * 将childJson中的值插入或覆盖入parentJson中
     *
     * @param parentJson
     * @param childJson
     * @return
     */
    static JsonObject mergeChildToParent(@NonNull JsonObject parentJson, JsonObject childJson) {
        if (Objects.isNull(childJson) || childJson.isJsonNull()) {
            return parentJson;
        }

        for (Map.Entry<String, JsonElement> childEntry : childJson.entrySet()) {
            String childKey = childEntry.getKey();
            JsonElement childVal = childEntry.getValue();
            if (parentJson.has(childKey)) {
                JsonElement parentVal = parentJson.get(childKey);
                if (parentVal.isJsonObject() && childVal.isJsonObject()) {
                    mergeChildToParent(parentVal.getAsJsonObject(), childVal.getAsJsonObject());
                } else if (parentVal.isJsonArray() && childVal.isJsonArray()) {
                    JsonArray parentArray = parentVal.getAsJsonArray();
                    JsonArray childArray = childVal.getAsJsonArray();

                    // update parent elements by indexes when lists name matched
                    int parentArraySize = parentArray.size();
                    int childArraySize = childArray.size();
                    for (int i = 0; i < parentArraySize; i++) {
                        if (i < childArraySize) {
                            mergeChildToParent(parentArray.get(i).getAsJsonObject(), childArray.get(i).getAsJsonObject());
                        }
                    }
                } else if (parentVal.isJsonPrimitive() && childVal.isJsonPrimitive()) {
                    // use value of child when conflict
                    parentJson.add(childKey, childVal);
                }
            } else {
                parentJson.add(childKey, childVal);
            }
        }

        return parentJson;
    }

    public static String mergeChildToParent(String parentJsonStr,
                                            String childJsonStr) {
        return GSON.toJson(
                mergeChildToParent(
                        PARSER.parse(parentJsonStr).getAsJsonObject(),
                        PARSER.parse(childJsonStr).getAsJsonObject())
        );
    }

    /**
     * 判断字符串是否是Json格式
     *
     * @param str
     * @return
     */
    public static boolean isJson(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }

        try {
            GSON.fromJson(str, Object.class);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    public static String getAsStringOrDefault(JsonObject jsonObject, String key, String defaultRet) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key) instanceof JsonPrimitive ?
                    jsonObject.get(key).getAsString() :
                    jsonObject.get(key).toString();
        }
        return defaultRet;
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

    }
}
