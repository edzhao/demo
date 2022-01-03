/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字节转换处理工具类<p>
 *
 * @author 成都深地领航能源科技有限公司
 */
@Slf4j
public class BytesUtil {

    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param hexString 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    /**
     * 字节做异或运算
     *
     * @param bytes
     * @return byte
     */
    public static Byte exclusiveOr(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            Byte value = 0;
            for (int i = 0; i < bytes.length; i++) {
                if (i == 0) {
                    value = bytes[0];
                } else {
                    value = (byte) (value ^ bytes[i]);
                }
            }
            return value;
        }
        return null;
    }


    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = bytes.length; i < len; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            if (i < len - 1) {
                sb.append(hex.toUpperCase() + " ");
            } else {
                sb.append(hex);
            }
        }
        return sb.toString();
    }

    /**
     * 获取int, 通过byte 数组
     *
     * @param bytes
     * @return int
     */
    public static int getIntForeHigh(byte... bytes) {
        if (bytes.length > 4) {
            throw new IllegalArgumentException("bytes 长度不能大于 4 位");
        }
        int num = 0;
        for (int i = 0; i < bytes.length; i++) {
            num += ((bytes[i] & 0xFF) << (bytes.length - 1 - i) * 8);
        }
        return num;
    }


    /**
     * 整数转换为字节数组
     *
     * @param val
     * @return 字节数组
     */
    public static byte[] intToByte4(int val) {
        byte[] array = new byte[4];
        array[3] = (byte) (val & 0xFF);
        array[2] = (byte) (val >> 8 & 0xFF);
        array[1] = (byte) (val >> 16 & 0xFF);
        array[0] = (byte) (val >> 24 & 0xFF);
        return array;
    }

    /**
     * 短整型转换为字节数组
     *
     * @param val
     * @return 字节数组
     */
    public static byte[] shortToByte2(short val) {
        byte[] array = new byte[2];
        array[1] = (byte) (val & 0xFF);
        array[0] = (byte) (val >> 8 & 0xFF);
        return array;
    }


    /**
     * 计算通道个数
     *
     * @param channel 通道设置值
     */
    public static int calculationChannelSetLength(int channel) {
        //转换为二进制字符串
        String binaryValue = Integer.toBinaryString(channel);
        return getStringCount(binaryValue, "1");
    }


    /**
     * 计算字符串中关键字出现的索引
     *
     * @param str 字符串
     * @param key 单个字符
     * @return 索引数组
     */
    public static List<Integer> getChannelIndex(String str, char key) {
        if (StringUtils.isNotEmpty(str)) {
            String tmp = StringUtils.reverse(str);
            List<Integer> array = new ArrayList<>();
            for (int i = 0; i < tmp.length(); i++) {
                char ch = tmp.charAt(i);
                if (ch == key) {
                    array.add(i + 1);
                }
            }
            return array;
        }
        return null;
    }

    /**
     * 获取一个字符串，查找这个字符串出现的次数
     *
     * @param str 查找的字符串
     * @param key 查找的关键字
     * @return 次数
     */
    public static int getStringCount(String str, String key) {
        return StringUtils.countMatches(str, key);
    }


    public static int bytearray2intarray(byte[] barray) {
        int[] iarray = new int[barray.length];
        int i = 0;
        int number = 0;
        for (byte b : barray) {
            iarray[i++] = b & 0xff;
            number += b & 0xff;
        }
        return number;
    }


    /**
     * byte数组转换为整数
     * 第0个byte与上0xff,生成整数,在右移24位，取得一个整数
     * 第1个byte与上0xff,生成整数,在右移16位，取得一个整数
     * 第2个byte与上0xff,生成整数,在右移8位，取得一个整数
     * 第3个byte与上0xff,生成整数
     * 把四个整数做或操作,转换为已整数
     */
    public static int byteArrToInt(byte[] bytes) {
        if (bytes.length > 4) {
            throw new IllegalArgumentException("bytes 长度不能大于 4 位");
        }
        return ((bytes[0] & 0xff) << 24) | ((bytes[1] & 0xff) << 16) | ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
    }

    /**
     * 十六进制校验和的结果
     *
     * @param hexdata
     * @return
     */
    public static String makeChecksum(String hexdata) {
        if (hexdata == null || hexdata.equals("")) {
            return "00";
        }
        hexdata = hexdata.replaceAll(" ", "");
        int total = 0;
        int len = hexdata.length();
        if (len % 2 != 0) {
            return "00";
        }
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        String s = intToHex(Integer.valueOf(hexInt(total)));
        return s;
    }

    public static String hexInt(int total) {
        int a = total / 256;
        int b = total % 256;
        if (a > 255) {
            return hexInt(a) + format(b);
        }
        return format(a) + format(b);
    }

    public static String format(int hex) {
        String hexa = Integer.toHexString(hex);
        int len = hexa.length();
        if (len < 2) {
            hexa = "0" + hexa;
        }
        return hexa;
    }

    public static String intToHex(int n) {
        //StringBuffer s = new StringBuffer();
        StringBuilder sb = new StringBuilder(8);
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            sb = sb.append(b[n % 16]);
            n = n / 16;
        }
        a = sb.reverse().toString();
        return a;
    }

    /**
     * 16进制转ASCII
     *
     * @param hex
     * @return
     */
    public static String hex2ASCII(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Byte aByte = exclusiveOr("AA 55 FF A7".getBytes());
        BytesUtil.bytesToHexString(new byte[]{aByte}).toUpperCase();
        log.info("111111111    : " + aByte);
    }
}
