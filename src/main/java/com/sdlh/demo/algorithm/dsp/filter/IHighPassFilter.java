package com.sdlh.demo.algorithm.dsp.filter;

public interface IHighPassFilter extends IBaseFilter {
    /**
     * 返回matlab计算好的高通滤波器参数
     *
     * @return 滤波器参数
     */
    double[][] highPassStaticParameter();

    /**
     * 返回动态计算的高通滤波器参数
     *
     * @param freq 信号频率
     * @return 滤波器参数
     */
    double[][] highPassDynamicParameter(double freq);

    /**
     * 高通滤波
     *
     * @param data 要进行滤波的原始数据
     * @return 滤波结果数据
     */
    double[] highPass(double[] data, double[][] parameter);
}
