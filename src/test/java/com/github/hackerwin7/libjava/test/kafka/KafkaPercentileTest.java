package com.github.hackerwin7.libjava.test.kafka;

import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.*;
import org.apache.kafka.common.metrics.stats.Percentile;
import org.apache.kafka.common.metrics.stats.Percentiles;
import org.apache.kafka.common.utils.SystemTime;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/03/16
 * Time: 3:58 PM
 * Desc:
 */
public class KafkaPercentileTest {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        Map<String, String> tags = new HashMap<>();
        tags.put("name", "tp");
        List<MetricsReporter> reporters = new ArrayList<>();
        reporters.add(new JmxReporter("tp"));
        Metrics metrics = new Metrics(new MetricConfig().samples(60).timeWindow(100, TimeUnit.MILLISECONDS),
                                      reporters,
                                      new SystemTime());
        Sensor ps = metrics.sensor("ps");

        int bucketNum = 5000 / 1 + 2;
        int sizeInBytes = 4 * bucketNum;
        ps.add(new com.github.hackerwin7.libjava.test.kafka.Percentiles(sizeInBytes, 5000, Percentiles.BucketSizing.CONSTANT,
                                                                        new Percentile(new MetricName("t99", "tp", "", tags), 99.0),
                                                                        new Percentile(new MetricName("t999", "tp", "", tags), 99.9)));


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleWithFixedDelay(() -> {
            for (int i = 0; i < 100; i++)
                ps.record(i);
        }, 1, 1, TimeUnit.SECONDS);

//        final List<String> _metricsNames = Arrays.asList("tp:*:*");
//        executor.scheduleWithFixedDelay(
//                () -> {
//                    StringBuilder sb = new StringBuilder();
//                    for (String metricName : _metricsNames) {
//                        String mbeanExpr = metricName.substring(0, metricName.lastIndexOf(":"));
//                        String attributeExpr = metricName.substring(metricName.lastIndexOf(":") + 1);
//                        List<MbeanAttributeValue> attributeValues = getMBeanAttributeValues(mbeanExpr, attributeExpr);
//                        for (MbeanAttributeValue attributeValue : attributeValues) {
//                            sb.append(attributeValue.toString() + "\n");
//                        }
//                    }
//                    System.out.println(sb.toString());
//
//                },
//                0, 1, TimeUnit.SECONDS);

        executor.scheduleWithFixedDelay(() -> System.out.println(metrics.metrics().get("tp").value()),
                                        0, 1, TimeUnit.SECONDS);
    }

    static List<MbeanAttributeValue> getMBeanAttributeValues(String mbeanExpr, String attributeExpr) {
        List<MbeanAttributeValue> values = new ArrayList<>();
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            Set<ObjectName> mbeanNames = server.queryNames(new ObjectName(mbeanExpr), null);
            for (ObjectName mbeanName : mbeanNames) {
                MBeanInfo mBeanInfo = server.getMBeanInfo(mbeanName);
                MBeanAttributeInfo[] attributeInfos = mBeanInfo.getAttributes();
                for (MBeanAttributeInfo attributeInfo : attributeInfos) {
                    if (attributeInfo.getName().equals(attributeExpr) || attributeExpr.length() == 0 || attributeExpr.equals("*")) {
                        double value = (Double) server.getAttribute(mbeanName, attributeInfo.getName());
                        values.add(new MbeanAttributeValue(mbeanName.getCanonicalName(), attributeInfo.getName(), value));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
}


class MbeanAttributeValue {
    private final String _mbean;
    private final String _attribute;
    private final double _value;

    public MbeanAttributeValue(String mbean, String attribute, double value) {
        _mbean = mbean;
        _attribute = attribute;
        _value = value;
    }

    @Override
    public String toString() {
        return _mbean + ":" + _attribute + "=" + _value;
    }
}