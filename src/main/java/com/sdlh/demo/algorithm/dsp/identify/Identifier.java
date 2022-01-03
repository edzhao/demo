/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.algorithm.dsp.identify;

/**
 * 信号识别接口
 *
 * @author 成都深地领航能源科技有限公司
 */
public interface Identifier {
    void ratePulse(double[] data);
    void syncPulse(double[] data);
    void fid(double[] data);
    void ordinary(double[] data);
}
