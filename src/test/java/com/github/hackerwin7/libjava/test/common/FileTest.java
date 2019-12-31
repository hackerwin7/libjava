package com.github.hackerwin7.libjava.test.common;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTest {

    public static void sumFile() throws Exception {
        File file = new File("/Users/fanwenqi/Projects/DevOps/elasticsearch/log1");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        double sum = 0.0;
        while ((line = br.readLine()) != null) {
            double num = Double.parseDouble(StringUtils.strip(line));
            sum += num;
        }
        fr.close();
        System.out.println(sum);
    }

    public static void adddoubles() throws Exception {
//        double oldV = 1.7458313836007748E16;
//        double corrected = 1.0;
//        double newV = oldV + corrected;
//        System.out.println(newV);
        double newV = 1.7458313836007748E16;
        double oldV = 1.7458313760867754E16;
        double corrected = 7.5139995E7;
        System.out.println(newV - oldV);
        System.out.println(corrected);
        System.out.println((newV - oldV) - corrected);
        System.out.println(1.7458313760867754E16 + 7.5139995E7);

        double d1 = 17458313843517748d;
        double d2 = 1d;
        System.out.println(d1 + d2);
        System.out.println((d1 + d2) == d1);
    }

    public static double kahanSum(double ...nums) {
        double sum = 0.0;
        double compensation = 0.0;
        for (double num : nums) {
            double corrected = num - compensation;
            double newSum = sum + corrected;
            compensation = (newSum - sum) - corrected;
            sum = newSum;
        }
        return sum;
    }

    public static void sumFileWithKahan() throws Exception {
        File file = new File("/Users/fanwenqi/Projects/DevOps/elasticsearch/log1");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        List<Double> fileNums = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            double num = Double.parseDouble(StringUtils.strip(line));
            fileNums.add(num);
        }
        fr.close();
//        System.out.println(kahanSum(fileNums.toArray()));
    }

    public static void doubleMisc() throws Exception {
        long data = 17458313843517749L;
        long data1 = 17458313843517755L;
        double n2 = Math.pow(2, 53);
        double doubleData = (double) data;
        double doubleData1 = (double) data1;
        System.out.println(doubleData);
        System.out.println(doubleData1);
        System.out.println(n2);
    }

    public static void main(String[] args) throws Exception {
//        doubleMisc();
        adddoubles();
//        sumFile();
    }


}

//        es: 1.7458313843517748E16
//      java: 1.7458313843517784E16
// java long: 17458313843517749
