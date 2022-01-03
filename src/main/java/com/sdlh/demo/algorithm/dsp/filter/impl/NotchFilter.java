/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.algorithm.dsp.filter.impl;


import com.sdlh.demo.algorithm.dsp.filter.INotchFilter;

/**
 * 陷波器实现类
 *
 * @author 成都深地领航能源科技有限公司
 */
public class NotchFilter implements INotchFilter {

    @Override
    public double[] filter(double[] data, double[][] parameter) {
        return new double[0];
    }

    @Override
    public double[][] staticParameter() {
        double[][] sos = new double[][]{{1, 2, 1, 1, -1.99913900721990490000, 0.99918150500520764000},
                {1, 2, 1, 1, -1.99751694173024830000, 0.99755940503361140000},
                {1, 2, 1, 1, -1.99593588662255180000, 0.99597831631577549000},
                {1, 2, 1, 1, -1.99442052977360620000, 0.99446292725330698000},
                {1, 2, 1, 1, -1.99299442321715640000, 0.99303679038062087000},
                {1, 2, 1, 1, -1.99167963366225460000, 0.99172197287586472000},
                {1, 2, 1, 1, -1.99049642114266300000, 0.99053873520348923000},
                {1, 2, 1, 1, -1.98946294918807290000, 0.98950524127930672000},
                {1, 2, 1, 1, -1.98859502924592760000, 0.98863730288688090000},
                {1, 2, 1, 1, -1.98790590148996490000, 0.98794816048140999000},
                {1, 2, 1, 1, -1.98740605363788550000, 0.98744830200354272000},
                {1, 2, 1, 1, -1.98710307896806240000, 0.98714532089307083000},
                {1, 1, 0, 1, -0.99350078718466261000, 0.00000000000000}};
        return sos;
    }

    @Override
    public double[][] dynamicParameter(double freq) {
        return new double[0][];
    }
}
