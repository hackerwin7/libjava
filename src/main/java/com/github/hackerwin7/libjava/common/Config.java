package com.github.hackerwin7.libjava.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2018/01/31
 * Time: 4:34 PM
 * Desc:
 */
public class Config {
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    private static final String CONF_FILE = "conf.properties";
    private static final Properties props = new Properties();

    static {
        try {
            props.load(Config.class.getClassLoader().getResourceAsStream(CONF_FILE));
        } catch (IOException e) {
            LOG.error( CONF_FILE + " file load failed! check it whether exists classpath. err = " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return (String) props.get(key);
    }
}
