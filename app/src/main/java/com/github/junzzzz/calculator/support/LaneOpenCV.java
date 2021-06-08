package com.github.junzzzz.calculator.support;

import org.opencv.core.Mat;

public class LaneOpenCV {
    static {
        System.loadLibrary("lane_detection");
    }

    public static Mat detect(Mat input) {
        Mat output = new Mat();
        detect(input.getNativeObjAddr(), output.getNativeObjAddr());
        return output;
    }

    private static native void detect(long inputMat, long outputMat);
}
