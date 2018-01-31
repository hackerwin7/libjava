package com.github.hackerwin7.libjava.kafka;

import kafka.utils.ZkUtils;
import org.apache.kafka.common.network.ListenerName;
import org.apache.kafka.common.protocol.SecurityProtocol;
import org.apache.kafka.common.security.JaasUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.Seq;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/01/31
 * Time: 4:21 PM
 * Desc:
 */
public class KafkaMetadataExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMetadataExecutor.class);
    private static final int SESSION_TIMEOUT = 30_000;
    private static final int CONNECTION_TIMEOUT = 30_000;

    private static SecurityProtocol protocol = SecurityProtocol.PLAINTEXT; // todo maybe always PLAINTEXT can get the metadata

    public static void main(String[] args) {
        if (args.length >= 3)
            protocol = SecurityProtocol.SASL_PLAINTEXT;
        LOG.info("info: " + getPartitionSingleBrokerInfo(args[0], args[1]));
        LOG.info("partition count: " + getPartitionNum(args[0], args[1]));
        LOG.info(JaasUtils.isZkSecurityEnabled() + "");
        LOG.info(protocol.name);
    }

    public static int getPartitionNum(String zkUrl, String topic) {
        ZkUtils zkUtils =  ZkUtils.apply(zkUrl, SESSION_TIMEOUT, CONNECTION_TIMEOUT, JaasUtils.isZkSecurityEnabled());
        Seq<String> topics = scala.collection.JavaConversions.asScalaBuffer(Arrays.asList(topic));
        int num = zkUtils.getPartitionsForTopics(topics).apply(topic).size();
        zkUtils.close();
        return num;
    }

    public static Map getPartitionSingleBrokerInfo(String zkUrl, String topic) {
        Map<Integer, String> info = new HashMap<>();
        ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIMEOUT, CONNECTION_TIMEOUT, JaasUtils.isZkSecurityEnabled());
        Seq<String> topics = scala.collection.JavaConversions.asScalaBuffer(Arrays.asList(topic));
        Map<Object, Seq<Object>> jmap = scala.collection.JavaConversions.mapAsJavaMap(zkUtils.getPartitionAssignmentForTopics(topics).apply(topic));
        for(Map.Entry<Object, Seq<Object>> entry : jmap.entrySet()) {
            int pid = (int) entry.getKey();
            int bid = (int) (scala.collection.JavaConversions.seqAsJavaList(entry.getValue()).get(0));
            String broker = zkUtils.getBrokerInfo(bid).get().getNode(ListenerName.forSecurityProtocol(protocol)).host();
            info.put(pid, broker);
        }
        zkUtils.close();
        return info;
    }
}
