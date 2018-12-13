package com.github.hackerwin7.libjava.exec;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.hackerwin7.libjava.common.Utils;
import com.github.hackerwin7.libjava.http.HttpExecutor;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/03/23
 * Time: 3:49 PM
 * Desc:
 */
public class CommonExec {
    public static void main(String[] args) throws Exception {
        inetAddrTest("wenyuhe.jdq.jd.local", 9888);
    }

    public static void httpTest() throws Exception {
        HttpExecutor executor = HttpExecutor.create();
        HashMap<String, String> params = new HashMap<>();
        params.put("appId", "k8s.kafka.monitor");
        params.put("token", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        params.put("time", String.valueOf(System.currentTimeMillis()));
        ObjectNode data = Utils.createJsonNode();
        data.put("usedId", 0);
        data.put("type", 2);
        params.put("data", Utils.json2String(data));
        String ret = executor.doPost("http://xxx/xxx/xx/getClusterList.ajax", params);
        System.out.println(Utils.string2Json(ret));
    }

    public static void inetAddrTest(String host, int port) throws Exception {

        long sleepTime = 0;
        long sleepUnit = 5000;
        while (sleepTime < 1440 * 60 * 1000) {
            InetSocketAddress isa = new InetSocketAddress(host, port);
            System.out.println("hostName: " + isa.getHostName() + ", hostString = " + isa.getHostString() + ", host addr = " + isa.getAddress().getHostAddress());
            System.out.println("Sleeping...");
            Thread.sleep(sleepUnit);
            sleepTime += sleepUnit;
        }

    }

}

