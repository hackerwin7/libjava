package com.github.hackerwin7.libjava.test.common;

//import org.apache.kafka.common.utils.Checksums;
import org.apache.kafka.common.utils.Crc32;
//import org.apache.kafka.common.utils.Crc32C;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
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
        testUnicodeLen();
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
