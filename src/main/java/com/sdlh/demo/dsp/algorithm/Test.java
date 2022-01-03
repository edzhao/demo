package com.sdlh.demo.dsp.algorithm;

/**
 *  横向插值算法
 *
 * @auther 长江大学
 */
public class Test {
    public static void main(String []args) {

        //测试
        //模拟文本读取数据
        double[] x = {139.242, 133.496, 125.361, 116.693, 117.453, 130.368, 130.92, 136.581, 139.242};  //第一行gamma值
        //声明插值对象，并传入参数
        /**
         *  参数说明
         *  x为gamma单行数组，y为与x匹配的方位角数组，且二者一一对应
         *  yy为需要插值的点数据，且包含y，运用时可将yy数组定死，如0，5，10，……，360
         *      如需改变步长，可自行更改
         *
         *  插值算法要求，第一个参数必须为递增数列，即与插值点列yy同类型数据
         *
         *
         */
        /**
         *  splines为72长度的数组，元素依次为0，5，10，……，315度对应的gamma值
         */
        double[] splines = SplineTool.splines(x, 73);
        long t = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < splines.length; i++){
            sb.append("splines[" + i + "]=" +splines[i] + "\t");
        }
        System.out.println(sb.toString());
        System.out.println("spline算法花费时间" + (System.currentTimeMillis() - t) + "ms");

        double[] gammma = new double[73];
        // gamma = 深度点 + splines ，即定义新数组在splines前面加上深度即可


        //将以上代码封装，遍历文本文件，即可得到所有行的插值数据

    }
}
