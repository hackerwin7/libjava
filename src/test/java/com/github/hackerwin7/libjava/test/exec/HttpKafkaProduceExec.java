package com.github.hackerwin7.libjava.test.exec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.hackerwin7.libjava.common.Config;
import com.github.hackerwin7.libjava.http.HttpExecutor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/01/31
 * Time: 1:41 PM
 * Desc:
 */
public class HttpKafkaProduceExec {

    private static final Logger LOG = LoggerFactory.getLogger(HttpKafkaProduceExec.class);

    private static final HttpExecutor httpExecutor = HttpExecutor.create();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final AtomicLong INC = new AtomicLong();

    private final String url;
    private final String clientId;
    private final String version;
    private final String token;

    private String batch = "1";

    HttpKafkaProduceExec(String url, String clientId, String version, String token) {
        this.url = url;
        this.clientId = clientId;
        this.version = version;
        this.token = token;
    }

    public static void main(String[] args) {
        HttpKafkaProduceExec exec = new HttpKafkaProduceExec("http://192.168.144.118:8888/put",
                                                             "c80ec5f6",
                                                             "3",
                                                             "143242343242");
        exec.start(100, 128);
    }

    public void start(int msgCount, int msgSize) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(() -> run(msgCount, msgSize), 1, 1, TimeUnit.SECONDS);
    }

    private void run(int msgCount, int msgSize) {
        try {
            long now = System.currentTimeMillis();
            Map<String, String> params = new HashMap<>();
            params.put("clientid", clientId);
            params.put("version", version);
            params.put("timestamp", String.valueOf(now));
            params.put("token", token);
            params.put("batch", batch);
            params.put("message", MAPPER.writeValueAsString(messages(msgCount, msgSize, now, -1)));

            LOG.info(httpExecutor.doPost("http://192.168.144.118:8888/put", params));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

    }

    private JsonNode messages(int count, int size, long now, int partition) {
        ObjectNode node = MAPPER.createObjectNode();
        ArrayNode arr = MAPPER.createArrayNode();
        for (int i = 0; i < count; i++) {
            ObjectNode item = MAPPER.createObjectNode();
            item.put("key", String.valueOf(INC.incrementAndGet()));
            item.put("value", message(size));
            if (partition > 0)
                item.put("partition", partition);
            if (now > 0)
                item.put("timestamp", String.valueOf(now));
            arr.add(item);
        }
        return node.set("data", arr);
    }

    private String message(int size) {
        return StringUtils.repeat('s', size);
    }
}
