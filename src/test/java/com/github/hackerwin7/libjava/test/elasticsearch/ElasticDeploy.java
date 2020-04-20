package com.github.hackerwin7.libjava.test.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticDeploy {

    private static final ElasticDeploy deploy = new ElasticDeploy();

    public void elasticDeployNodesGen() throws IOException, URISyntaxException {
        List<String> ips = Files.lines(Paths.get(ClassLoader.getSystemResource("ips").toURI())).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode nodes = mapper.createArrayNode();

        int nodesPerIp = 4;
        int baseHttpPort = 9200;
        int baseTransportPort = 9300;
        int pathsPerNode = 1;
        String dataPathPrefix = "es_data";
        for (String ip : ips) {
            for (int nodeIdx = 0; nodeIdx < nodesPerIp; nodeIdx++) {
                ObjectNode node = mapper.createObjectNode();
                node.put("ip", ip);
                node.put("http_port", baseHttpPort + nodeIdx);
                node.put("transport_port", baseTransportPort + nodeIdx);

                ArrayNode dataPaths = mapper.createArrayNode();
                for (int pathIdx = 1; pathIdx <= pathsPerNode; pathIdx++) {
                    dataPaths.add("/" + Paths.get("data" + String.format("%02d", pathIdx), dataPathPrefix,
                            "es_data_" + nodeIdx).toString());
                }
                node.set("data_path", dataPaths);
                nodes.add(node);
            }
        }
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodes));
    }



    public static void main(String[] args) throws Exception {
        deploy.elasticDeployNodesGen();
    }
}
