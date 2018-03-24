package com.github.hackerwin7.libjava.http;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/03/23
 * Time: 10:26 AM
 * Desc: a http executor utils
 */
public class HttpExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(HttpExecutor.class);

    private static volatile HttpExecutor instance;
    private HttpClient httpClient;

    private HttpExecutor() {
        httpClient = HttpClients.createDefault();
    }

    public static HttpExecutor create() {
        if (instance == null) {
            synchronized (HttpExecutor.class) {
                if (instance == null)
                    return new HttpExecutor();
            }
        }
        return instance;
    }

    public String doPost(String url, Map<String, String> params) throws IOException {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet())
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        post.setEntity(new UrlEncodedFormEntity(pairs));
        HttpResponse response = httpClient.execute(post);
        return EntityUtils.toString(response.getEntity());
    }

    public String doGet(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        HttpResponse response = httpClient.execute(get);
        return EntityUtils.toString(response.getEntity());
    }
}
