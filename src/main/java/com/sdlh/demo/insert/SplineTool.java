package com.sdlh.demo.insert;

import java.util.Arrays;

/**
 *  三次样条插值类
 * @auther 长江大学
 */
public class SplineTool{

    private int n;  //原始数据个数
    private double[] xs;    //原始数据x数组
    private double[] ys;    //原始数据y数组
    private double[] yy;    //插值点数组

    private boolean sp_initialized;  //初始化标识符
    private double[] sp_y2s;    //spline算法中间值，由ys求得

    public SplineTool(double[] _xs, double[] _ys, double[] _yy) {
        this.n = _xs.length;
        this.xs = Arrays.copyOf(_xs, _xs.length);
        this.ys = Arrays.copyOf(_ys, _ys.length);
        this.yy = _yy;

        this.sp_initialized = false;
    }

    public SplineTool() {}

    /**
     *  插值算法
     * @return  插值数组
     */
    public double[] splines(){
        double[] doubles = new double[yy.length];
        for(int i=0;i<yy.length;i++){
           doubles[i] =  spline(yy[i]);
        }
        return doubles;
    }

    /**
     *  插值算法
     * @return  插值数组
     */
    public double[] splines(double[] _xs, double[] _ys, double[] _yy){
        this.n = _xs.length;
        this.xs = Arrays.copyOf(_xs, _xs.length);
        this.ys = Arrays.copyOf(_ys, _ys.length);
        this.yy = _yy;
        this.sp_initialized = false;

        return splines();
    }

    /**
     *  插值算法
     * @param x 插值点
     * @return  插值
     */
    public double spline(double x) {
        if (!this.sp_initialized) {

            double p, qn, sig, un;
            double[] us;

            us = new double[n-1];
            sp_y2s = new double[n];
            us[0] = sp_y2s[0] = 0.0;

            for (int i=1; i<=n-2; i++) {
                sig = (xs[i] - xs[i-1]) / (xs[i+1] - xs[i-1]);
                p = sig * sp_y2s[i-1] + 2.0;
                sp_y2s[i] = (sig - 1.0) / p;
                us[i] = (ys[i+1] - ys[i]) / (xs[i+1] - xs[i]) - (ys[i] - ys[i-1]) / (xs[i] - xs[i-1]);
                us[i] = (6.0 * us[i] / (xs[i+1] - xs[i-1]) - sig * us[i-1]) / p;
            }
            qn = un = 0.0;

            sp_y2s[n-1] = (un - qn * us[n-2]) / (qn * sp_y2s[n-2] + 1.0);
            for (int k=n-2; k>=0; k--) {
                sp_y2s[k] = sp_y2s[k] * sp_y2s[k+1] + us[k];
            }

            this.sp_initialized = true;
        }

        int klo, khi, k;
        double h, b, a;

        klo = 0;
        khi = n-1;
        while (khi-klo > 1) {
            k = (khi+klo) >> 1;
            if (xs[k] > x)
                khi = k;
            else
                klo = k;
        }
        h = xs[khi] - xs[klo];
        if (h == 0.0) {
            throw new ArithmeticException();
        }
        a = (xs[khi] - x) / h;
        b = (x - xs[klo]) / h;
        return a*ys[klo] + b*ys[khi] + ((a*a*a-a)*sp_y2s[klo]+(b*b*b-b)*sp_y2s[khi])*(h*h)/6.0;
    }

    /**
     *  数组去重
     */
    public static double[] deduplicate(double[] arr) {
        double[] newArr = new double[arr.length];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            boolean flag = true;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] == arr[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                newArr[index] = arr[i];
                index++;
            }
        }
        double[] newArrs = new double[index];
        System.arraycopy(newArr, 0, newArrs, 0, index);
        return newArrs;
    }

    //矩阵转置
    //二维数组转置
    public static double[][] reverse(double[][] arr) {
        double[][] floats = new double[arr[0].length][arr.length];

        for(int i=0; i< arr.length; i++) {
            //列循环从
            for(int j=0; j< arr[i].length; j++) {
                double temp = arr[i][j];
                floats[j][i] = temp;
            }
        }
        return floats;
    }

}