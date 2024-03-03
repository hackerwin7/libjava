package com.github.hackerwin7.libjava.cases;

/**
 * @author : wenqi.jk
 * @since : 3/1/24, 15:42
 **/
public interface Shape {
    /**
     *  painting test for cases
     */
    default void draw() {
        System.out.println("Shape");
    }
}
