package com.github.hackerwin7.libjava.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.auth.ApiKeyAuth;
import io.kubernetes.client.models.V1Pod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/03/23
 * Time: 4:05 PM
 * Desc:
 */
public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
    private static final ObjectMapper JSON = new ObjectMapper();

    private static String IP = null;
    private static final Object lock = new Object();

    public static JsonNode string2Json(String str) throws IOException {
        return JSON.readTree(str);
    }

    public static String json2String(JsonNode json) throws JsonProcessingException {
        return JSON.writeValueAsString(json);
    }

    public static String json2PrettyString(JsonNode json) throws JsonProcessingException {
        return JSON.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    public static ObjectNode createJsonNode() {
        return JSON.createObjectNode();
    }

    /**
     * get current machine ip, both physical and virtual (docker , kubernetes etc.)
     * @return ip address
     */
    public static String ip() {

//        if (IP != null)
//            return IP;

//        synchronized (lock) {
//
//        }

        // pod, preferred last ip no 192 or 127 starts with
        //  api, the status.podIP is inject building pod bot after pod running to retrieve this info
//        ApiClient api = Configuration.getDefaultApiClient();
//        ApiKeyAuth BearerToken = (ApiKeyAuth) api.getAuthentication("BearerToken");
//        BearerToken.setApiKey("Token");
//        CoreV1Api apiIns = new CoreV1Api();
//        try {
//            V1Pod res = apiIns.readNamespacedPod("111", "jrdw", "true", true, true);
//            System.out.println("api res = " + res);
//        } catch (ApiException e) {
//            LOG.error(e.getMessage(), e);
//        }



        // physical, preferred last ip no 192 or 127 starts with
      InetAddress tmp = null; // prefer last
      InetAddress addr127 = null;
      InetAddress addr192 = null;
      try {
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (inetAddr instanceof Inet6Address)
                        continue;
                    if (inetAddr.getHostAddress().startsWith("127")) {
                      addr127 = inetAddr;
                      continue;
                    }
                    if (inetAddr.getHostAddress().startsWith("192")) {
                      addr192 = inetAddr;
                      continue;
                    }
                    System.out.println("inet Address = " + inetAddr);
                    LOG.debug("inet address = " + inetAddr);
                    if (!inetAddr.isLoopbackAddress()) {
                      tmp = inetAddr;
//                        if (inetAddr.isSiteLocalAddress())
//                            return inetAddr.getHostAddress();
//                        else if (tmp == null)
//                            tmp = inetAddr;
                    }
                }
            }
            if (tmp != null)
                return tmp.getHostAddress();
            InetAddress jdkAddr = InetAddress.getLocalHost();
            if (jdkAddr == null)
                throw new UnknownHostException("jdk supplied address failed!");
            return jdkAddr.getHostAddress();
        } catch (Exception e) {
            LOG.error("failed to retrieve ip, due to: " + e.getMessage(), e);
        }
        if (addr192 != null)
          return addr192.getHostAddress();
        if (addr127 != null)
          return addr127.getHostAddress();
        return "Unknown";
    }

    public static void main(String[] args) {
        System.out.println(ip());
    }
}
