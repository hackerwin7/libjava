package com.github.hackerwin7.libjava.exec;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.hackerwin7.libjava.common.Utils;
import com.github.hackerwin7.libjava.http.HttpExecutor;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/03/23
 * Time: 3:49 PM
 * Desc:
 */
public class Tester {
    public static void main(String[] args) throws Exception {
        HttpExecutor executor = HttpExecutor.create();
        HashMap<String, String> params = new HashMap<>();
        params.put("appId", "k8s.kafka.monitor");
        params.put("token", "4UHQX4227ZF3YCITIJYDJIBYDVM43NT22VY25HY");
        params.put("time", String.valueOf(System.currentTimeMillis()));
        ObjectNode data = Utils.createJsonNode();
        data.put("usedId", 0);
        data.put("type", 2);
        params.put("data", Utils.json2String(data));
        String ret = executor.doPost("http://jrdw.bdp.jd.local/jrdw/clusterManage/api/getClusterList.ajax", params);
        System.out.println(Utils.string2Json(ret));
    }

}

