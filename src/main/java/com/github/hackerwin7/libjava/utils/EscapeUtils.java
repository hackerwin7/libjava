package com.github.hackerwin7.libjava.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.BitSet;

// TODO: deduplicate with com.bytedance.dts.common.util.FileUtils?
@SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:IllegalTokenText"})
public class EscapeUtils {

  private static final char ESCAPE_FLAG = '%';
  private EscapeUtils() {
  }

  private static final BitSet CHAR_TO_ESCAPE = new BitSet(128);

  static {
    for (char c = 0; c < ' '; c++) {
      CHAR_TO_ESCAPE.set(c);
    }

    /*
     * ASCII 01-1F are HTTP control characters that need to be escaped.
     * \u000A and \u000D are \n and \r, respectively.
     */
    char[] clist = new char[]{'\u0001', '\u0002', '\u0003', '\u0004',
        '\u0005', '\u0006', '\u0007', '\u0008', '\u0009', '\n', '\u000B',
        '\u000C', '\r', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012',
        '\u0013', '\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019',
        '\u001A', '\u001B', '\u001C', '\u001D', '\u001E', '\u001F',
        '"', '#', '%', '\'', '*', '/', ':', '=', '?', '\\', '\u007F', '{',
        '[', ']', '^'};

    for (char c : clist) {
      CHAR_TO_ESCAPE.set(c);
    }
  }

  private static boolean needsEscaping(char c) {
    return c < CHAR_TO_ESCAPE.size() && CHAR_TO_ESCAPE.get(c);
  }

  /**
   * Escapes a path name.
   *
   * @param path The path to escape.
   * @return An escaped path name.
   */
  public static String escapePathName(String path) {
    if (path == null || path.isEmpty()) {
      throw new IllegalArgumentException("Path should not be null or empty: " + path);
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if (needsEscaping(c)) {
        System.out.println("need escape: " + c);
        sb.append('%');
        sb.append(String.format("%1$02X", (int) c));
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public static String checkAndEscape(String pathVal) {
    if (StringUtils.isBlank(pathVal) || StringUtils.contains(pathVal, ESCAPE_FLAG)) {
      return pathVal;
    }
    return escapePathName(pathVal);
  }

  @SuppressWarnings("checkstyle:EmptyCatchBlock")
  public static String unescapePathName(String path) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if (c == '%' && i + 2 < path.length()) {
        int code = -1;
        try {
          code = Integer.parseInt(path.substring(i + 1, i + 3), 16);
        } catch (Exception ignored) {
        }
        if (code >= 0) {
          sb.append((char) code);
          i += 2;
          continue;
        }
      }
      sb.append(c);
    }
    return sb.toString();
  }

}
