package com.github.hackerwin7.libjava.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/03/23
 * Time: 4:05 PM
 * Desc:
 */
public class Utils {
    private static final ObjectMapper JSON = new ObjectMapper();

    public static JsonNode string2Json(String str) throws IOException {
        return JSON.readTree(str);
    }

    public static String json2String(JsonNode json) throws JsonProcessingException {
        return JSON.writeValueAsString(json);
    }

    public static String json2PrettyString(JsonNode json) throws JsonProcessingException {
        return JSON.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    public static ObjectNode createJsonNode() {
        return JSON.createObjectNode();
    }
}
