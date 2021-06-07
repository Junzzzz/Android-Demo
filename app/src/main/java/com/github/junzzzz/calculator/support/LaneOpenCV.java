package com.github.junzzzz.calculator.support;

public class LaneOpenCV {
    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();
}
