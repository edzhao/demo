package com.sdlh.demo.insert;/** *  测试数据类 *      保存各类静态数据,初始化静态数据 * *      gamma为原始gamma数据，格式为 [time,speed,gamma1,gamma2,...,gamma8] * *      GAMMA为带深度信息的数据，格式为 [depth,gamma1,gamma2,...,gamma8] * */public class TestData {    static int DataNum; //gamma数据行数    static final double startDepth = 3000.125; //起深，假设数据从3000.125米开始    static final double stepsDepth = 0.125; //默认深度插值步长    static final double stepsThita = 5; //默认方位角插值步长    static final double[] thitaArray; //方位角插值数组    static final double[] depthArray; //深度插值数组    static final double[] depthArrayDeduplicated;//去除重复深度点后的数组    static final double[][] GAMMA; //带深度信息的gamma数据    static double[] thita = {0,45,90,135,180,225,270,315}; //方位角    /**     *  原始gamma数据，第一列为时间，第二列为速度，后面8列为对应8个方位角的gamma值     */    static double[][] gamma = new double[][]{            //时间，速度，8道伽马值            {16006,0.14,17,15,14,15,14,14,16,16}, {16137,0,15,16,14,17,14,14,15,14},            {16432,0.13,16,16,15,16,16,15,16,15}, {16691,0,15,14,15,14,16,17,14,15},            {16721,0.09,13,12,14,13,13,14,13,14}, {16729,0.16,14,12,14,13,14,14,14,12},            {16821,0.13,17,15,14,15,14,14,16,16}, {16888,0.17,17,15,14,15,14,14,16,16}};    static {        //获取数据行数，以便后续遍历        DataNum = thita.length;        //获取方位角插值数组        int thitaNum = 360/5 + 1;        thitaArray = new double[thitaNum];        for(int i=0;i<thitaNum;i++){            thitaArray[i] = stepsThita*i;        }        thitaArray[thitaNum-1] = 360;        //获取深度插值数组        depthArray = new double[DataNum];        depthArray[0] = startDepth;        //原理：相邻两行数据时间相减得到钻井运行时间，乘以钻井当前运速度得当前深度        for(int i=1;i<DataNum;i++){            depthArray[i] = depthArray[i-1] + (gamma[i][0] - gamma[i-1][0])*gamma[i-1][1];        }        //获取带深度信息的gamma数据，即GAMMA        GAMMA = new double[DataNum][gamma[0].length-1];        for(int i=0;i<GAMMA.length;i++){            GAMMA[i][0] = depthArray[i];            for(int j=1;j<GAMMA[0].length;j++){                GAMMA[i][j] = gamma[i][j+1];            }        }        //获取去重的深度数组        depthArrayDeduplicated = SplineTool.deduplicate(depthArray);    }}