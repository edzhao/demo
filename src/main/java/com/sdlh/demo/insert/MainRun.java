package com.sdlh.demo.insert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  测试类
 *
 * @auther 长江大学
 */

public class MainRun {
    /**
     *  算法步骤
     *  1. 均值抽样
     *  2. 方位角方向插值
     *  3. 深度方向插值
     */

    public static void main(String[] args) {

        SplineTool splineTool = new SplineTool();
        //用于保存中间数据,由于处理后数据量未知，无法初始化数组
        List<double[]> result = new ArrayList<>();


        System.out.println("====原始gamma数据====");
        for(double[] g:TestData.gamma) {
            System.out.println(Arrays.toString(g));
        }

        System.out.println("====深度gamma数据====");
        for(double[] G:TestData.GAMMA){
            System.out.println(Arrays.toString(G));
        }




        //1. 均值抽样
        System.out.println("====1. 均值抽样====");
        for (double depth : TestData.depthArrayDeduplicated) {
            double[] temp = new double[9]; //保存同深度点伽马数据
            int count = 0; //同深度点数据计数,最少一条数据

            //同深度点求和
            for (double[] gamma : TestData.GAMMA) {
                if (Math.abs(depth - gamma[0]) < 0.0000001d) {
                    count++;
                    //同深度点数据求和
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] += gamma[i];
                    }
                }
            }

            //取平均
            for (int i = 0; i < temp.length; i++) {
                temp[i] /= count;
            }

            System.out.println(Arrays.toString(temp));


            /**
             *  2. 方位角方向插值
             */
            //保存插值前数据，去除深度点，在末尾加上360度处的伽马值
            double[] gamma_temp = new double[9];
            double[] thita_temp = new double[9];

            //保存插值后数据
            double[] gammma = new double[73];

            gamma_temp[8] = temp[1];//插值数据补上360度时的数据
            System.arraycopy(temp, 1, gamma_temp, 0, gamma_temp.length - 1);

            thita_temp[8] = 360;
            System.arraycopy(TestData.thita, 0, thita_temp, 0, TestData.thita.length);


            /*System.out.println(Arrays.toString(thita_temp));
            System.out.println(Arrays.toString(gamma_temp));
            System.out.println(Arrays.toString(TestData.thitaArray));*/

            gammma[0] = temp[0]; //保存深度信息
            System.arraycopy(splineTool.splines(thita_temp,gamma_temp,TestData.thitaArray), 0, gammma, 1, gammma.length-1);

            result.add(gammma);

        }

        //打印横向插值结果
        System.out.println("====2. 方位角方向插值====");
        for(double[] doubles:result){
            System.out.println(Arrays.toString(doubles));
        }

        /**
         *  3. 深度方向插值
         *      已知起始深度，按0.125可生成一个深度插值数组
         *          问题在于gamma数据对应的深度与深度插值数组对不上
         *          解决方案：
         *              若gamma数据深度与深度插值数组深度差小于0.1，则用当前深度插值数组深度代替即可
         */

        double startDepth = TestData.startDepth;
        double endDepth = TestData.depthArray[TestData.DataNum-1];

        //生成深度插值数组
        int depthNum = (int) ((endDepth - startDepth)/TestData.stepsDepth) + 1;
        double[] depthArray = new double[depthNum];
        for(int i=0;i<depthNum;i++){
            depthArray[i] = startDepth + TestData.stepsDepth*i;
        }
        //修正深度
        double[] depthArray_justified = new double[TestData.depthArrayDeduplicated.length];
        for(int i=0;i<TestData.depthArrayDeduplicated.length;i++){
            for(double depth: depthArray){
                if(Math.abs(TestData.depthArrayDeduplicated[i] - depth) <= 0.1){
                    depthArray_justified[i] = depth;
                }
            }
        }
        System.out.println("====3. 深度方向插值====");
        System.out.println("修正深度后深度");
        System.out.println(Arrays.toString(depthArray_justified));

        /**
         *  开始深度方向插值
         *      由于深度是单调递增的属性，而方位角是周期性
         *      所以插值时不需要在末尾增加数据
         *
         *      插值时，需要遍历72道伽马值，从而得到每一道在深度列上的插值
         */

        //转置
        double[][] GAMMA_temp = SplineTool.reverse(result.toArray(new double[result.size()][]));
        //中间数组保存插值数据
        double[][] GAMMA_result = new double[73][610];
        System.arraycopy(depthArray,0,GAMMA_result[0],0,depthArray.length);
        for(int i=1;i<GAMMA_temp.length;i++){
            double[] splines = splineTool.splines(depthArray_justified, GAMMA_temp[i], depthArray);
            System.arraycopy(splines,0,GAMMA_result[i],0,splines.length);
        }

        //再转置回来
        double[][] finalResult = SplineTool.reverse(GAMMA_result);

        System.out.println("====最终插值结果====");
        for(double[] d: finalResult){
            System.out.println(Arrays.toString(d));
        }
    }
}
