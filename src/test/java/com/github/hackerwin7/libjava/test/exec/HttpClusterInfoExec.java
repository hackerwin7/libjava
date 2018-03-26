package com.github.hackerwin7.libjava.test.exec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/03/26
 * Time: 7:20 PM
 * Desc:
 */
public class HttpClusterInfoExec {
    public static void main(String[] args) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("http://jrdw.bdp.jd.local/jrdw/clusterManage/api/getClusterList.ajax");
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("appId", "k8s.kafka.monitor"));
            pairs.add(new BasicNameValuePair("token", "4UHQX4227ZF3YCITIJYDJIBYDVM43NT22VY25HY"));
            pairs.add(new BasicNameValuePair("time", String.valueOf(System.currentTimeMillis())));
            pairs.add(new BasicNameValuePair("data", new ObjectMapper().writeValueAsString(new ObjectMapper().createObjectNode().put("usedId", "0").put("type", "2"))));
            post.setEntity(new UrlEncodedFormEntity(pairs));
            CloseableHttpResponse response = client.execute(post);
            String ret = EntityUtils.toString(response.getEntity());
            response.close();
            JsonNode root = new ObjectMapper().readTree(ret);
            for (JsonNode cluster : root.get("list"))
                if (!cluster.get("name").asText().contains("测试"))
                    System.out.print(cluster.get("tagName").asText() + ",");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
