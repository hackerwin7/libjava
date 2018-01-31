package com.github.hackerwin7.libjava.http;

import com.github.hackerwin7.libjava.common.Config;
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

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/01/31
 * Time: 1:41 PM
 * Desc:
 */
public class HttpExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(HttpExecutor.class);
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat();

    private static final String CONF_URL_KEY = "http.executor.url";

    public static void main(String[] args) throws Exception {
        run( FORMATTER.format(new Date()) + "kky");
    }

    public static void run(String msg) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(Config.get(CONF_URL_KEY));
        List<NameValuePair> params = new ArrayList<>();
        long now = System.currentTimeMillis();
        params.add(new BasicNameValuePair("clientid", "17ddbab3"));
        params.add(new BasicNameValuePair("version", "3"));
        params.add(new BasicNameValuePair("message", URLEncoder.encode(msg, "UTF-8")));
        params.add(new BasicNameValuePair("timestamp", String.valueOf(now)));
        params.add(new BasicNameValuePair("token", URLEncoder.encode("fae8767b-91db-11e6-8623-a4dcbef13e1c", "UTF-8")));
        post.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = client.execute(post);
        LOG.info(EntityUtils.toString(response.getEntity()));
        client.close();
    }
}
