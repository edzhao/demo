/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.number;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 成都深地领航能源科技有限公司
 */
@Slf4j
public class ConvertDemo {
    public static void main(String[] args) {
        hexToInt("F6DF");
    }

    private static void hexToInt(String hex) {
        log.info(hex + ":" + Integer.parseInt(hex, 16));
    }

    private static void hexToSignedChar(String hex) {

    }

    private static void hexToUnSignedChar(String hex) {
    }
}
