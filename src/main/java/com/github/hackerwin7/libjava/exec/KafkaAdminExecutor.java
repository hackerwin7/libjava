package com.github.hackerwin7.libjava.exec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/03/02
 * Time: 3:06 PM
 * Desc:
 */
public class KafkaAdminExecutor {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String clientId = args[0];
        String rate = args[1];
        Properties props = new Properties();
        props.put("producer_byte_rate", rate);
        props.put("consumer_byte_rate", rate);
        ZkUtils zkUtils = ZkUtils.apply("172.22.98.20:2181,172.22.98.21:2181,172.22.98.22:2181/kafka", 30000, 30000, JaasUtils.isZkSecurityEnabled());
        AdminUtils.changeClientIdConfig(zkUtils, clientId, props);

        String ret = zkUtils.readData(ZkUtils.getEntityConfigPath("clients", clientId)).toString();
        System.out.println("ret = " + ret);
        Iterator<Object> it = scala.collection.JavaConversions.asJavaIterator(zkUtils.readData(ZkUtils.getEntityConfigPath("clients", clientId)).productIterator());
        while (it.hasNext()) {
            String objData = (String) it.next();
            System.out.println("it obj = " + objData);
            if (objData.charAt(0) == '{') {
                ret = objData;
                break;
            }
        }
        JsonNode config = mapper.readTree(ret).get("config");
        System.out.println("updated config = " + config);

        zkUtils.close();
    }
}
