#include <jni.h>
#include <string>
#include <vector>
#include <numeric>

#include <opencv2/opencv.hpp>

using namespace std;
using namespace cv;

void roi_mask(InputArray src, OutputArray dst) {
    Mat mask = Mat::zeros(src.size(), CV_8UC1);
    // 关注区域
    Point points[4];
    points[0] = Point(0, mask.rows);
    points[1] = Point(mask.cols / 3, mask.rows / 1.5);
    points[2] = Point(mask.cols / 1.5, mask.rows / 1.5);
    points[3] = Point(mask.cols, mask.rows);

    fillConvexPoly(mask, points, 4, Scalar::all(255));
    bitwise_and(mask, src, dst);
}

void hough_lines(InputArray src, OutputArray &lines, int rho = 1, double theta = CV_PI / 180,
                 int threshold = 15,
                 int min_line_len = 40,
                 int max_line_gap = 20) {
    HoughLinesP(src, lines, rho, theta, threshold, min_line_len, max_line_gap);
}

void draw_lines(Mat &img, Mat &dst, vector<Vec4i> &lines,
                const Scalar &color = Scalar(0, 0, 255), int thickness = 2) {
    vector<Point> left_points, right_points;
    int line_y_max = 0, line_y_min = 999;

    for (Vec4i &line : lines) {
        int x1 = line[0];
        int y1 = line[1];
        int x2 = line[2];
        int y2 = line[3];
        if (y1 > line_y_max) line_y_max = y1;
        if (y2 > line_y_max) line_y_max = y2;
        if (y1 < line_y_min) line_y_min = y1;
        if (y2 < line_y_min) line_y_min = y2;

        double k = 1.0 * (y2 - y1) / (x2 - x1);

        if (k < -0.2) {
            left_points.emplace_back(x1, y1);
            left_points.emplace_back(x2, y2);
        } else if (k > 0.2) {
            right_points.emplace_back(x1, y1);
            right_points.emplace_back(x2, y2);
        }
    }

    if (left_points.empty() || right_points.empty()) {
        dst = img;
        return;
    }

    // 拟合点
    Vec4f left, right;

    fitLine(left_points, left, DIST_L2, 0, 1e-2, 1e-2);
    fitLine(right_points, right, DIST_L2, 0, 1e-2, 1e-2);

    Point points[4];

    // 计算拟合线
    double k = left[1] / left[0];
    double b = left[3] - k * left[2];
    points[0] = Point((line_y_max - b) / k, line_y_max);
    points[1] = Point((line_y_min - b) / k, line_y_min);

    k = right[1] / right[0];
    b = right[3] - k * right[2];
    points[2] = Point((line_y_min - b) / k, line_y_min);
    points[3] = Point((line_y_max - b) / k, line_y_max);

    Mat zero_img = Mat::zeros(img.size(), CV_8UC3);

    line(zero_img, points[0], points[1], color, thickness);
    line(zero_img, points[2], points[3], color, thickness);
    fillConvexPoly(zero_img, points, 4, Scalar(0, 255, 0));
    addWeighted(img, 1, zero_img, 0.2, 0, dst);
}

extern "C" {

JNIEXPORT void JNICALL
Java_com_github_junzzzz_calculator_support_LaneOpenCV_detect(JNIEnv *env, jclass clazz,
                                                             jlong imageAddress,
                                                             jlong outputAddress) {
    Mat origin = *((Mat *) imageAddress);
    cvtColor(origin, origin, COLOR_RGBA2RGB);

    Mat frame;
    vector<Vec4i> lines;
    // 灰度化
    cvtColor(origin, frame, COLOR_RGB2GRAY);
    // 高斯滤波
    GaussianBlur(frame, frame, Size(3, 3), 0);
    // 边缘检测
    Canny(frame, frame, 150, 250);
    // 设置关注区域
    roi_mask(frame, frame);
    // 霍夫变换
    hough_lines(frame, lines);
    // 合并道路线
    draw_lines(origin, frame, lines, Scalar(255, 0, 0), 6);

    // 输出
    *((Mat *) outputAddress) = frame;
}

}
