package com.github.hackerwin7.libjava.kafka;

import kafka.admin.AdminUtils;
import kafka.admin.BrokerMetadata;
import kafka.admin.RackAwareMode;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.network.ListenerName;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
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
        if (args.length >= 5)
            protocol = SecurityProtocol.SASL_PLAINTEXT;
        LOG.info("info: " + getPartitionSingleBrokerInfo(args[0], args[1]));
        LOG.info("partition topic: " + getPartitionsTopic(args[0], args[1]));
        LOG.info("partition count: " + getPartitionNum(args[0], args[1]));
        LOG.info(JaasUtils.isZkSecurityEnabled() + "");
        LOG.info(protocol.name);
        LOG.info("add partition to: " + addPartition(args[0], args[1], Integer.parseInt(args[2])));
        LOG.info("add partition1 to: " + addPartition1(args[0], args[1], Integer.parseInt(args[3])));
    }

    public static int getPartitionNum(String zkUrl, String topic) {
        ZkUtils zkUtils =  ZkUtils.apply(zkUrl, SESSION_TIMEOUT, CONNECTION_TIMEOUT, JaasUtils.isZkSecurityEnabled());
        Seq<String> topics = scala.collection.JavaConversions.asScalaBuffer(Arrays.asList(topic));
        int num = zkUtils.getPartitionsForTopics(topics).apply(topic).size();
        zkUtils.close();
        return num;
    }

    public static Map getPartitionSingleBrokerInfo(String zkUrl, String topic) {
        Map<Integer, List<String>> info = new HashMap<>();
        ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIMEOUT, CONNECTION_TIMEOUT, JaasUtils.isZkSecurityEnabled());
        Seq<String> topics = scala.collection.JavaConversions.asScalaBuffer(Arrays.asList(topic));
        Map<Object, Seq<Object>> jmap = scala.collection.JavaConversions.mapAsJavaMap(zkUtils.getPartitionAssignmentForTopics(topics).apply(topic));
        for(Map.Entry<Object, Seq<Object>> entry : jmap.entrySet()) {
            int pid = (int) entry.getKey();
            List<Object> bids = scala.collection.JavaConversions.seqAsJavaList(entry.getValue());
            List<String> brokers = new ArrayList<>();
            for(Object bid : bids) {
                brokers.add(zkUtils.getBrokerInfo((int) bid).get().getNode(ListenerName.forSecurityProtocol(protocol)).host());
            }
            info.put(pid, brokers);
        }
        zkUtils.close();
        return info;
    }

    public static scala.collection.Map getPartitionsTopic(String zkUrl, String topic) {
        ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIMEOUT, CONNECTION_TIMEOUT, JaasUtils.isZkSecurityEnabled());
        try {
            LOG.info("======== " + zkUtils.getPartitionAssignmentForTopics(
                    scala.collection.JavaConversions.asScalaBuffer(Collections.singletonList(topic))
            ).apply(topic));
            LOG.info("-------- " + (scala.collection.Map) zkUtils.getPartitionAssignmentForTopics(
                    scala.collection.JavaConversions.asScalaBuffer(Collections.singletonList(topic))
            ).apply(topic));
            return zkUtils.getPartitionAssignmentForTopics(
                    scala.collection.JavaConversions.asScalaBuffer(Collections.singletonList(topic))
            ).apply(topic);
        } finally {
            zkUtils.close();
        }
    }

    public static int addPartition(String zkUrl, String topic, int numPartitions) {
        ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIMEOUT, CONNECTION_TIMEOUT, JaasUtils.isZkSecurityEnabled());
        try {
            Seq<String> topics = scala.collection.JavaConversions.asScalaBuffer(Collections.singletonList(topic));
            scala.collection.Map<Object, Seq<Object>> existingAssignment = zkUtils.getPartitionAssignmentForTopics(topics).apply(topic);
            Seq<BrokerMetadata> allBrokers = AdminUtils.getBrokerMetadatas(zkUtils, RackAwareMode.Enforced$.MODULE$, scala.Option.<Seq<Object>>empty());
            scala.Option<scala.collection.Map<Object, Seq<Object>>> newAssignment = Option.empty();
            AdminUtils.addPartitions(zkUtils, topic, existingAssignment, allBrokers, numPartitions, newAssignment, false);
            return numPartitions;
        } finally {
            zkUtils.close();
        }
    }

    public static int addPartition1(String zkUrl, String topic, int numPartitions) {
        ZkUtils zkUtils = ZkUtils.apply(zkUrl, SESSION_TIMEOUT, CONNECTION_TIMEOUT, JaasUtils.isZkSecurityEnabled());
        try {
            AdminUtils.addPartitions(zkUtils,
                                     topic,
                                     zkUtils.getPartitionAssignmentForTopics(
                                             scala.collection.JavaConversions.asScalaBuffer(
                                                     Collections.singletonList(topic))).apply(topic),
                                     AdminUtils.getBrokerMetadatas(zkUtils, RackAwareMode.Enforced$.MODULE$, Option.<Seq<Object>>empty()),
                                     numPartitions,
                                     Option.<scala.collection.Map<Object, Seq<Object>>>empty(),
                                     false);
            return numPartitions;
        } finally {
            zkUtils.close();
        }

    }
}
