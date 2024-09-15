package com.github.hackerwin7.libjava.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import net.logstash.logback.marker.Markers;
import net.logstash.logback.marker.ObjectAppendingMarker;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

import java.util.Iterator;

public class LogInstanceMarker extends TurboFilter {
  public static final String NAME_MARKER = "name";
  public static final String TIMESTAMP_MARKER = "timestamp";
  public static final String LOGGER_NAME_PREFIX = "com.github.hackerwin7.libjava.test.common.LogTest";
  private static Long instanceId = -1L;

  @Override
  public FilterReply decide(Marker marker, Logger logger, Level level, String s, Object[] objects, Throwable throwable) {
    try {
      if (instanceId < 0) {
        return FilterReply.NEUTRAL;
      }
      if (!logger.getName().startsWith(LOGGER_NAME_PREFIX)) {
        return FilterReply.NEUTRAL;
      }
      if (containsInstanceMarker(marker, NAME_MARKER)) {
        return FilterReply.NEUTRAL;
      }
      if (marker == null) {
        marker = getInstanceMarker();
      } else {
        marker.add(getInstanceMarker());
      }
      logger.log(marker, logger.getName(), getLocationLevel(level.levelInt), s, objects, throwable);
      return FilterReply.DENY;
    } catch (Exception e) {
      logger.warn(getInstanceMarker(), "Failed to add instance marker, exp: {}", e.getMessage(), e);
      return FilterReply.NEUTRAL;
    }
  }

  public static void loadInstanceMarker(long instanceId) {
    LogInstanceMarker.instanceId = instanceId;
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    loggerContext.addTurboFilter(new LogInstanceMarker());
  }

  public static Marker getInstanceMarker() {
    return Markers.append(TIMESTAMP_MARKER, System.currentTimeMillis()).and(Markers.append(NAME_MARKER, instanceId));
  }

  public static int getLocationLevel(int loggerLevel) {
    switch (loggerLevel) {
      case Level.TRACE_INT:
        return LocationAwareLogger.TRACE_INT;
      case Level.DEBUG_INT:
        return LocationAwareLogger.DEBUG_INT;
      case Level.INFO_INT:
        return LocationAwareLogger.INFO_INT;
      case Level.ERROR_INT:
        return LocationAwareLogger.ERROR_INT;
      case Level.WARN_INT:
      default:
        return LocationAwareLogger.WARN_INT;
    }
  }

  public static boolean containsInstanceMarker(Marker marker, String filedName) {
    // iterate  marker
    if (marker == null || !marker.hasReferences()) {
      return false;
    }
    if (marker instanceof ObjectAppendingMarker && ((ObjectAppendingMarker) marker).getFieldName().equals(filedName)) {
      return true;
    }
    Iterator<Marker> iMarker = marker.iterator();
    while (iMarker.hasNext()) {
      Marker m = iMarker.next();
      if (m instanceof ObjectAppendingMarker && ((ObjectAppendingMarker) m).getFieldName().equals(filedName)) {
        return true;
      }
    }
    return false;
  }
}
