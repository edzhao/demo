package com.sdlh.demo.algorithm.dsp.filter;

public interface ILowPassFilter extends IBaseFilter {
    /**
     * 返回matlab计算好的低通滤波器参数
     *
     * @return 滤波器参数
     */
    double[][] lowPassStaticParameter();

    /**
     * 返回动态计算的低通滤波器参数
     *
     * @param freq 信号频率
     * @return 滤波器参数
     */
    double[][] lowPassDynamicParameter(double freq);
    /**
     * 低通滤波
     *
     * @param data 要进行滤波的原始数据
     * @return 滤波结果数据
     */
    double[] lowPass(double[] data, double[][] parameter);
}
