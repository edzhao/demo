/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.algorithm.dsp.filter;

/**
 * 滤波器接口
 *
 * @author 成都深地领航能源科技有限公司
 */
public interface IBaseFilter {

    /**
     * 滤波接口方法
     *
     * @param data 要进行滤波的原始数据
     * @param mode 是否加入高通滤波器(默认只用了低通滤波器)
     * @return
     */
    static double[] filter(double[] data, double freq, int mode, boolean isDynamic, INotchFilter notchFilter, IBaseFilter filter) {
        double[] tempData = new double[0];
        IHighPassFilter highPassFilter = (IHighPassFilter) filter;
        ILowPassFilter lowPassFilter = (ILowPassFilter) filter;
        switch (mode) {
            case 1:
                tempData = isDynamic ? lowPassFilter.lowPass(highPassFilter.highPass(data, highPassFilter.highPassDynamicParameter(freq)), lowPassFilter.lowPassDynamicParameter(freq))
                        : lowPassFilter.lowPass(highPassFilter.highPass(data, highPassFilter.highPassStaticParameter()), lowPassFilter.lowPassStaticParameter());
                break;
            case 2:
                break;
            default:
                tempData = isDynamic ? lowPassFilter.lowPass(data, lowPassFilter.lowPassDynamicParameter(freq))
                        : lowPassFilter.lowPass(data, lowPassFilter.lowPassStaticParameter());
                break;
        }

        return isDynamic ? notchFilter.filter(tempData, notchFilter.dynamicParameter(freq))
                : notchFilter.filter(tempData, notchFilter.staticParameter());
    }

    /**
     * 返回matlab计算好的对应的滤波器参数
     *
     * @return 滤波器参数
     */
    default double[][] staticParameter() {
        return new double[0][];
    }

    /**
     * 返回动态计算的滤波器参数
     *
     * @param freq 信号频率
     * @return 滤波器参数
     */
    default double[][] dynamicParameter(double freq) {
        return new double[0][];
    }

    /**
     * 陷波器滤波
     *
     * @param data 要进行滤波的原始数据
     * @return 滤波结果数据
     */
    default double[] filter(double[] data, double[][] parameter){
        return data;
    }
}
