package com.github.hackerwin7.libjava.test.common;

//import org.apache.kafka.common.utils.Checksums;
import org.apache.kafka.common.utils.Crc32;
//import org.apache.kafka.common.utils.Crc32C;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Checksum;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/12/25
 * Time: 15:26
 * Desc: a new JDK test for instance
 */
public class JDKTest {

    public static final String s = "1, 3, 0, 0, 1, 104, 79, 97, -108, 27, -1, -1, -1, -1, 0, 0, 0, 58, 4, 34, 77, 24, 96, 64, -126, 43, 0, 0, 0, 21, 0, 1, 0, -1, 10, 4, 22, 71, -12, -87, 114, 1, 0, 0, 0, 1, 104, 79, 97, -108, 27, -1, -1, -1, -1, 0, 0, 4, 0, 42, 1, 0, -1, -1, -1, -22, 80, 42, 42, 42, 45, 48, 0, 0, 0, 0";

    static {

    }

    public static void main(String[] args) {
        testIdLen();
    }

    public static void testIdLen() {
        String s= "1641-[{\"id\":\"8:1\",\"is_selected\":false,\"name\":\"内容无法正常展示（卡顿、黑白屏）\"},{\"id\":\"4:1\",\"is_selected\":false,\"name\":\"不感兴趣\"},{\"id\":\"7:1\",\"is_selected\":false,\"name\":\"无法关闭\"},{\"id\":\"6:0\",\"is_selected\":false,\"name\":\"举报广告\",\"options\":[{\"id\":\"6:6\",\"is_selected\":false,\"name\":\"诱导点击\"},{\"id\":\"6:1\",\"is_selected\":false,\"name\":\"低俗色情\"},{\"id\":\"6:4\",\"is_selected\":false,\"name\":\"疑似抄袭\"},{\"id\":\"6:5\",\"is_selected\":false,\"name\":\"违法违规\"},{\"id\":\"6:2\",\"is_selected\":false,\"name\":\"虚假欺诈\"}]}]-os_version_name";
        String id = "199-广州唐国花丽生物科技有限公司-similar_customer_name_name";
        id = "{\"byteSize\":8,\"rawData\":199,\"type\":\"LONG\"},{\"byteSize\":14,\"rawData\":\"广州唐国花丽生物科技有限公司\",\"type\":\"STRING\"},{\"byteSize\":26,\"rawData\":\"similar_customer_name_name\",\"type\":\"STRING\"},{\"byteSize\":14,\"rawData\":\"广州唐国花丽生物科技有限公司\",\"type\":\"STRING\"},{\"byteSize\":1,\"rawData\":false,\"type\":\"BOOLEAN\"},{\"byteSize\":15,\"rawData\":\"zhaohuayu.howie\",\"type\":\"STRING\"},{\"byteSize\":8,\"rawData\":1602437231,\"type\":\"LONG\"},{\"byteSize\":15,\"rawData\":\"zhaohuayu.howie\",\"type\":\"STRING\"},{\"byteSize\":8,\"rawData\":1602437231,\"type\":\"LONG\"},{\"byteSize\":45,\"rawData\":\"199-广州唐国花丽生物科技有限公司-similar_customer_name_name\",\"type\":\"STRING\"},{\"byteSize\":8,\"rawData\":0,\"type\":\"LONG\"}";
        System.out.println(s.getBytes(StandardCharsets.UTF_8).length);
        System.out.println(s.length());
    }

    public static void testMatcherRegexDate() {
        final String DayRegexText = "^([a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9])((-[a-zA-Z0-9._]+)*)-(\\d{4}[-.]?(0[1-9]|1[012])[-.]?(0[1-9]|[12][0-9]|3[01]))((-[a-zA-Z0-9._]+)*)$";
        final int DateGroup = 4;
        final Pattern DateRegexPattern = Pattern.compile(DayRegexText);

        String indexName = "ad_creative_realtime_stats-2020.09.29";
        Matcher m = DateRegexPattern.matcher(indexName);
        if (m.find()) {
            System.out.println(m.group(4));
            for (int i = 0; i <= m.groupCount(); i++) {
                System.out.println(m.group(i));
            }
        } else {
            System.out.println("no find");
        }
    }

    public static void testMapNull() {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put("1", "1");
        value.put("2", "2");
        System.out.println((String) value.get("1"));
        System.out.println((String) value.get("111111"));
        System.out.println((String) null);
        String nullStr = (String) null;
        if (nullStr == null) {
            System.out.println(nullStr);
        }
    }

    public static void testUnicodeLen() {
        System.out.println("韩浩宇今天吃饭了吗".length());
    }

    public static void testPath() {
        System.out.println(Paths.get("/cur1/cur2/ss", "51", "t2"));
    }


    public static void Crc32() {
        String[] sa = s.split(",");
        byte[] bytes = new byte[sa.length];
        System.out.println(sa.length);
        for (int i = 0; i < sa.length; i++) {
            bytes[i] = Byte.parseByte(sa[i].trim());
            System.out.print(bytes[i] + ",");
        }
        System.out.println();
        Crc32 crc = new Crc32();
        crc.update(bytes, 0, 76);
        System.out.println("checksum crc32 = " + crc.getValue());
    }

//    public static void Crc32C() {
//
//        // kafka client: 1653099722, kafka broker: 3960739671
//
//        String[] sa = s.split(",");
//        byte[] bytes = new byte[sa.length];
//        System.out.println(sa.length);
//        for (int i = 0; i < sa.length; i++) {
//            bytes[i] = Byte.parseByte(sa[i].trim());
//            System.out.print(bytes[i] + ",");
//        }
//        System.out.println();
//        Checksum crc = Crc32C.create();
//        Checksums.update(crc, bytes, 0, 76);
//        System.out.println("checksum crc32c = " + Checksums.getValue(crc));
//    }

    public static void conversion() {
        byte b = 10; // -128 ~ 127
        int a = 10;
        int c = 1000;
        b = (byte) a;
        System.out.println(b);
        b = (byte) c;
        System.out.println(b);
    }

    public static void javaRuntime() {
        System.out.println(System.getProperty("java.specification.version"));
    }

    /**
     * LRU test demo for map
     */
    public static void LRU() {
//        Map<Long, String> lru = new LinkedHashMap<>();
//        lru.forEach();
    }
}
