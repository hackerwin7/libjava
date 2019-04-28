package com.github.hackerwin7.libjava.test.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class IndexDoc {

    private static final ObjectMapper OM = new ObjectMapper();

    public static final String URL = "http://localhost:9200/";

    private static final String DEFAULT_INDEX_NAME = "test";
    private static final String DEFAULT_INDEX_TYPE = "_doc";

    private static final String API_BULK = "_bulk";
    private static final String API_SEARCH = "_search";

    public static void main(String[] args) throws Exception {
        search(10001);
    }

    public static void doc(int count) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();

        int cur = 0;
        while (cur++ < count) {
            HttpPost post = new HttpPost(URL + DEFAULT_INDEX_NAME + "/" + DEFAULT_INDEX_TYPE);
            post.setHeader("Content-Type", "application/json");
            ObjectNode body = OM.createObjectNode();
            body.put("message", "message-cur-" + cur);
            post.setEntity(new StringEntity(body.toString()));
            HttpResponse res = client.execute(post);
            System.out.println(EntityUtils.toString(res.getEntity()));
        }

        client.close();
    }

    public static void bulk(int count) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(URL + API_BULK);
        post.setHeader("Content-Type", "application/json");
        StringBuilder bulkAppender = new StringBuilder();
        ObjectNode headNode = OM.createObjectNode();
        headNode.set("index", OM.createObjectNode().put("_index", DEFAULT_INDEX_NAME)
                                                   .put("_type", DEFAULT_INDEX_TYPE));
        ObjectNode dataNode = OM.createObjectNode();
        for (int i = 0; i < count; i++) {
            dataNode.put("message", "bulk-message-" + i);
            bulkAppender.append(headNode.toString()).append("\n");
            bulkAppender.append(dataNode.toString()).append("\n");
        }
        post.setEntity(new StringEntity(bulkAppender.toString()));
        HttpResponse res = client.execute(post);
        System.out.println(EntityUtils.toString(res.getEntity()));

        client.close();
    }

    public static void search(int count) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(URL + DEFAULT_INDEX_NAME + "/" + API_SEARCH);
        post.setHeader("Content-Type", "application/json");
        ObjectNode body = OM.createObjectNode();
        body.set("query", OM.createObjectNode().set("match_all", OM.createObjectNode()));
        body.put("size", count);
        post.setEntity(new StringEntity(body.toString()));
        HttpResponse res = client.execute(post);
        System.out.println(EntityUtils.toString(res.getEntity()));

        client.close();
    }
}
