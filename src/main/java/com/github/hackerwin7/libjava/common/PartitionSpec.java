package com.github.hackerwin7.libjava.common;

import javax.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.hackerwin7.libjava.utils.EscapeUtils.escapePathName;
import static com.github.hackerwin7.libjava.utils.EscapeUtils.unescapePathName;
import static com.github.hackerwin7.libjava.utils.Preconditions.checkArgument;

public final class PartitionSpec implements Iterable<Map.Entry<String, String>>, Serializable {
  private static final Pattern PARTITION_NAME_PATTERN = Pattern.compile("([^/]+)=([^/]+)");
  private static final char COMMA_SEPARATOR = ',';
  private static final char FILE_SEPARATOR = '/';

  private final LinkedHashMap<String, String> parts;

  public PartitionSpec() {
    this(new LinkedHashMap<>());
  }

  private PartitionSpec(LinkedHashMap<String, String> parts) {
    this.parts = parts;
  }

  public void add(String name, String value) {
    parts.put(name, value);
  }

  public boolean contains(String name) {
    return parts.containsKey(name);
  }

  @Nullable
  public String get(String name) {
    return parts.get(name);
  }

  public int size() {
    return parts.size();
  }

  public List<String> valuesAsList() {
    return new ArrayList<>(parts.values());
  }

  public Map<String, String> asMap() {
    return Collections.unmodifiableMap(parts);
  }

  @Override
  public Iterator<Map.Entry<String, String>> iterator() {
    return parts.entrySet().iterator();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartitionSpec entries = (PartitionSpec) o;
    return Objects.equals(parts, entries.parts);
  }

  @Override
  public int hashCode() {
    return parts.hashCode();
  }

  public PartitionSpec mapValues(BiFunction<String, String, String> f) {
    PartitionSpec result = new PartitionSpec();
    for (Map.Entry<String, String> entry : parts.entrySet()) {
      result.add(entry.getKey(), f.apply(entry.getKey(), entry.getValue()));
    }
    return result;
  }

  public PartitionSpec filterIndex(int indexToFilter) {
    PartitionSpec result = new PartitionSpec();
    int index = 0;
    for (Map.Entry<String, String> entry : parts.entrySet()) {
      if (indexToFilter != index) {
        result.add(entry.getKey(), entry.getValue());
      }
      index++;
    }
    return result;
  }

  public String generatePartitionPath() {
    return generatePartitionStringInternal(FILE_SEPARATOR, true);
  }

  public String generateCommaSeperatedPartitionString() {
    return generatePartitionStringInternal(COMMA_SEPARATOR, false);
  }

  private String generatePartitionStringInternal(char delimiter, boolean addTrailingDelimiter) {
    StringBuilder suffixBuf = new StringBuilder();
    int i = 0;
    for (Map.Entry<String, String> e : parts.entrySet()) {
      if (i > 0) {
        suffixBuf.append(delimiter);
      }
      suffixBuf.append(escapePathName(e.getKey()));
      suffixBuf.append('=');
      suffixBuf.append(escapePathName(e.getValue()));
      i++;
    }
    if (addTrailingDelimiter) {
      suffixBuf.append(delimiter);
    }
    return suffixBuf.toString();
  }

  public String generateValuePath() {
    StringBuilder builder = new StringBuilder();
    int index = 0;
    for (Map.Entry<String, String> entry : parts.entrySet()) {
      builder.append(entry.getValue());
      if (index < parts.size() - 1) {
        builder.append('/');
      }
      index++;
    }
    return builder.toString();
  }

  public Path appendToPath(Path partitionPath) {
    for (Map.Entry<String, String> spec : parts.entrySet()) {
      partitionPath = partitionPath.suffix("/" + spec.getKey() + "=" + spec.getValue());
    }
    return partitionPath;
  }

  public static PartitionSpec of(String... kvs) {
    checkArgument(kvs.length % 2 == 0);
    PartitionSpec result = new PartitionSpec();
    for (int i = 0; i < kvs.length; i += 2) {
      result.add(kvs[i], kvs[i + 1]);
    }
    return result;
  }

  public static PartitionSpec fromMap(LinkedHashMap<String, String> partitions) {
    return new PartitionSpec(partitions);
  }

  /**
   * Make partition spec from path.
   *
   * @param currPath partition file path.
   * @return Sequential partition specs.
   */
  public static PartitionSpec fromPath(Path currPath) {
    PartitionSpec fullPartSpec = new PartitionSpec();
    List<String[]> kvs = new ArrayList<>();
    do {
      String component = currPath.getName();
      Matcher m = PARTITION_NAME_PATTERN.matcher(component);
      if (m.matches()) {
        String k = unescapePathName(m.group(1));
        String v = unescapePathName(m.group(2));
        String[] kv = new String[2];
        kv[0] = k;
        kv[1] = v;
        kvs.add(kv);
      }
      currPath = currPath.getParent();
    } while (currPath != null && !currPath.getName().isEmpty());

    // reverse the list since we checked the part from leaf dir to table's base dir
    for (int i = kvs.size(); i > 0; i--) {
      fullPartSpec.add(kvs.get(i - 1)[0], kvs.get(i - 1)[1]);
    }

    return fullPartSpec;
  }

  public String getPartitionWhereClause() {
    StringBuilder stringBuilder = new StringBuilder();
    int index = 0;
    int size = parts.size();
    for (Map.Entry<String, String> partitionSpec : parts.entrySet()) {
      stringBuilder.append(partitionSpec.getKey()).append('=').append('\'').append(partitionSpec.getValue()).append('\'');
      if (index < size - 1) {
        stringBuilder.append(" AND ");
      }
      index++;
    }
    return stringBuilder.toString();
  }

  /**
   * Represents a common Specification of Path that is tailor-made solely for the purpose of PartitionSpec,
   * hence using the inner class
   */
  public interface Path {

    Path getParent();

    String getName();

    Path suffix(String suffix);
  }
}
