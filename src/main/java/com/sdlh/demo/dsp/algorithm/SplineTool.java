package com.sdlh.demo.dsp.algorithm;

/**
 *  三次样条插值类
 * @auther 长江大学
 */
public class SplineTool{

    /**
     *  插值算法
     * @return  插值数组
     */
    public static double[] splines(double[] ys, int cols){
        boolean sp_initialized = false;  //初始化标识符
        double[] xs = {0, 45, 90, 135, 180, 225, 270, 315, 360};    //原始数据y数组,方位角
        int n = xs.length; //原始数据个数
        double[] sp_y2s = new double[n];   //spline算法中间值，由ys求得
        double[] yy = new double[cols];   //初始化插值点列，0-360度，步长为5度
        for(int i = 0; i < yy.length; i++){
            //步长为5
            yy[i] = 5*i;
        }

        double[] results = new double[yy.length];

        for(int i = 0; i < yy.length; i++){
            if (!sp_initialized) {

                double p, qn, sig, un;
                double[] us;

                us = new double[n-1];
                us[0] = sp_y2s[0] = 0.0;

                for (int j=1; j<=n-2; j++) {
                    sig = (xs[j] - xs[j-1]) / (xs[j+1] - xs[j-1]);
                    p = sig * sp_y2s[j-1] + 2.0;
                    sp_y2s[j] = (sig - 1.0) / p;
                    us[j] = (ys[j+1] - ys[j]) / (xs[j+1] - xs[j]) - (ys[j] - ys[j-1]) / (xs[j] - xs[j-1]);
                    us[j] = (6.0 * us[j] / (xs[j+1] - xs[j-1]) - sig * us[j-1]) / p;
                }
                qn = un = 0.0;

                sp_y2s[n-1] = (un - qn * us[n-2]) / (qn * sp_y2s[n-2] + 1.0);
                for (int k=n-2; k>=0; k--) {
                    sp_y2s[k] = sp_y2s[k] * sp_y2s[k+1] + us[k];
                }

                sp_initialized = true;
            }

            int klo, khi, k;
            double h, b, a;

            klo = 0;
            khi = n-1;
            while (khi-klo > 1) {
                k = (khi+klo) >> 1;
                if (xs[k] > yy[i])
                    khi = k;
                else
                    klo = k;
            }
            h = xs[khi] - xs[klo];
            if (h == 0.0) {
                throw new ArithmeticException();
            }
            a = (xs[khi] - yy[i]) / h;
            b = (yy[i] - xs[klo]) / h;
           results[i] = a*ys[klo] + b*ys[khi] + ((a*a*a-a)*sp_y2s[klo]+(b*b*b-b)*sp_y2s[khi])*(h*h)/6.0;
        }

        return results;
    }
}