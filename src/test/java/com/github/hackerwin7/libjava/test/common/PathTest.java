package com.github.hackerwin7.libjava.test.common;

import com.github.hackerwin7.libjava.common.Pair;
import com.github.hackerwin7.libjava.common.PartitionSpec;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author : wenqi.jk
 * @since : 6/6/24, 13:23
 **/
public class PathTest {

  public static void main(String[] args) {
    hasPartition("date=20220601,app=test,123123123123");
  }


  public static void hasPartition(String partition) {
    List<String> partVals = new ArrayList<String>();
    String[] parts = partition.split(",");
    for (String part : parts) {
      String[] kv = part.split("=");
      partVals.add(kv[1]);
    }
  }
}
