package com.github.hackerwin7.libjava.test.common;

/**
 * @author : wenqi.jk
 * @since : 8/1/24, 15:49
 **/
public enum EnumInstance {
    AA("A"),
    BB("B"),
    CCC("C");

    private String display;

    EnumInstance(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
