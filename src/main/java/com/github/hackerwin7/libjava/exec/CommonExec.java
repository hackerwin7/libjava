package com.github.hackerwin7.libjava.exec;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.hackerwin7.libjava.common.Utils;
import com.github.hackerwin7.libjava.http.HttpExecutor;
import org.apache.commons.lang3.StringUtils;

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

    private static final String ARRAY_STRING = "array";
    private static final String MAP_STRING = "map";
    private static final String COLLECTION_LEFT_BRACKET = "<";
    private static final String COLLECTION_RIGHT_BRACKET = ">";
    private static final String COMMA_STRING = ",";


    public static void main(String[] args) throws Exception {
        stringTest2();
    }

    public static void stringTest2() {
        System.out.println(getElementOdpsType("array<int>"));
        System.out.println(getElementOdpsType("array<array<string>>"));
        System.out.println(getElementKeyOdpsType("Map<string, int>"));
        System.out.println(getElementValOdpsType("Map<string, int>"));
        System.out.println(getElementKeyOdpsType("Map<map<int, string>, map<string,string>>"));
        System.out.println(getElementValOdpsType("Map<map<int, string>, map<string,string>>"));
    }


    public static String getElementOdpsType(String odpsType) {
        if (StringUtils.startsWithIgnoreCase(odpsType, ARRAY_STRING)) {
            return odpsType.substring(odpsType.indexOf(COLLECTION_LEFT_BRACKET) + 1, odpsType.lastIndexOf(COLLECTION_RIGHT_BRACKET));
        } else {
            return odpsType;
        }
    }

    public static String getElementKeyOdpsType(String odpsType) {
        // recursion not supported
        if (StringUtils.startsWithIgnoreCase(odpsType, MAP_STRING)) {
            return odpsType.substring(odpsType.indexOf(COLLECTION_LEFT_BRACKET) + 1, odpsType.indexOf(COMMA_STRING)).trim();
        } else {
            return odpsType;
        }
    }

    public static String getElementValOdpsType(String odpsType) {
        // recursion not supported
        if (StringUtils.startsWithIgnoreCase(odpsType, MAP_STRING)) {
            return odpsType.substring(odpsType.indexOf(COMMA_STRING) + 1, odpsType.lastIndexOf(COLLECTION_RIGHT_BRACKET)).trim();
        } else {
            return odpsType;
        }
    }

    public static void stringTest1() {
//        String engineType = "map<int, string>";
        String engineType = "map<map<int, string>, map<string, int>>";
        String[] split = StringUtils.split(StringUtils
            .substring(engineType, "map".length() + 1, engineType.length() - 1), ",", 2);

        String keyType = trim(split[0]);
        String valueType = trim(split[1]);

        System.out.println(keyType);
        System.out.println(valueType);
    }

    public static String trim(String typeName) {
        return StringUtils.replace(StringUtils.trim(typeName), " ", "");
    }

    public static void stringTest() {
        String s = "ssr";
        String s1 = s.replaceAll("", "#");
        System.out.println(s1);
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

