/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.dsp.algorithm;

import java.util.stream.IntStream;

/**
 * 插值算法工具类
 *
 * @author 深地领航科技有限公司
 */
public class InterpolationUtil {

    /**
     * 第三种边界条件的三次样条函数插值
     *
     * @param y 已知360度，9扇区伽马值数组(第9个扇区的伽马值等于第1个扇区的值)
     * @param max 要插值的扇区总个数(必须是9的整数倍 + 1)
     * @return 插值后所有扇区的伽马值数组
     */
    public static double[] espl3(double[] y, int max) {
        int i, j, len = y.length, time = (max - 1)/len;
        int[] x = new int[len];

        for (i = 0; i < len; i++) {
            x[i] = i * len;
        }

        int[] t = new int[max - len];
        for (i = 0; i < t.length; i++) {
            t[i] = i + (i / time) + 1;
        }

        int n = x.length, m = t.length;
        double[] dy = new double[n];
        double[] ddy = new double[n];
        double[] z = new double[m];
        double[] dz = new double[m];
        double[] ddz = new double[m];
        double[] a = new double[n-1];
        double[] b = new double[n-1];
        double[] c = new double[n-1];
        double[] u = new double[n];
        double[] v = new double[n];
        double alpha=0,alpha1=0,beta=0,beta1=0,h,h1,h2;
        a[0]=0;b[0]=1;c[1]=0;
        alpha1=(x[n-1]-x[n-2])/(x[n-1]-x[n-2]+x[1]-x[0]);
        beta1=3*((1-alpha1)*(y[n-1]-y[n-2])/(x[n-1]-x[n-2])+alpha1*(y[1]-y[0])/(x[1]-x[0]));

        for(i=1;i<n-1;i++) {
            h1=x[i]-x[i-1];
            h2=x[i+1]-x[i];
            alpha=h1/(h1+h2);;
            beta=3*((1-alpha)*(y[i]-y[i-1])/h1+alpha*(y[i+1]-y[i])/h2);
            h=2+(1-alpha1)*a[i-1];
            a[i]=-alpha1/h;b[i]=-(1-alpha1)*b[i-1]/h;c[i]=(beta1-(1-alpha1)*c[i-1])/h;
            alpha1=alpha;beta1=beta;
        }

        u[n-1]=1;v[n-1]=0;

        for(i=n-2;i>0;i--) {
            u[i]=a[i]*u[i+1]+b[i];
            v[i]=a[i]*v[i+1]+c[i];
        }

        dy[n-2]=(beta-alpha*v[1]-(1-alpha)*v[n-2])/(2+alpha*u[1]+(1-alpha)*u[n-2]);

        for(i=0;i<n-2;i++)
            dy[i]=u[i+1]*dy[n-2]+v[i+1];

        dy[n-1]=dy[0];

        for(i=0;i<n-1;i++)
            ddy[i]=(6*(y[i+1]-y[i])/(x[i+1]-x[i])-2*(2*dy[i]+dy[i+1]))/(x[i+1]-x[i]);

        ddy[n-1]=ddy[0];

        for(j=0;j<t.length;j++) {
            for(i=0;i<n-1;i++)
                if(t[j]<x[i+1])break;

            h=x[i+1]-x[i];h1=x[i+1]-t[j];h2=t[j]-x[i];
            z[j]=0;z[j]+=(3-2*h1/h)*h1*h1*y[i]/h/h;z[j]+=(3-2*h2/h)*h2*h2*y[i+1]/h/h;
            z[j]+=(1-h1/h)*h1*h1*dy[i]/h;z[j]-=(1-h2/h)*h2*h2*dy[i+1]/h;
            dz[j]=0;dz[j]+=6*(h1/h-1)*h1*y[i]/h/h;dz[j]-=6*(h2/h-1)*h2*y[i+1]/h/h;
            dz[j]+=(3*h1/h-2)*h1/h*dy[i];dz[j]+=(3*h2/h-2)*h2/h*dy[i+1];
            ddz[j]=0;ddz[j]+=6*(1-2*h1/h)*y[i]/h/h;ddz[j]+=6*(1-2*h2/h)*y[i+1]/h/h;
            ddz[j]+=(2-6*h1/h)*dy[i]/h;ddz[j]-=(2-6*h2/h)*dy[i+1]/h;
        }

        double[] all = new double[max];
        for (i = 0; i < x.length; i++) {
            all[x[i]] = y[i];
        }
        for (i = 0; i < t.length; i++) {
            all[t[i]] = z[i];
        }

        return all;
    }

    public static void main(String[] args) {
        int[] total = IntStream.rangeClosed(0, 72).toArray();
        int max = 73, len = 9;
        int time = (max - 1)/len;
        int[] x = new int[len];
        double[] y = {139.242, 133.496, 125.361, 116.693, 117.453, 130.368, 130.92, 136.581, 139.242};
        int[] t = new int[max - len];

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            x[i] = i * len;
            sb.append("x[" + i + "]=" + x[i] + "\t");
        }
        System.out.println(sb.toString());
        System.out.println("-------------------------------------------------------");
        sb.setLength(0);

        for (int i = 0; i < t.length; i++) {
            t[i] = i + (i / time) + 1;
            sb.append("t[" + i + "]=" + t[i] + "\t");
        }
        System.out.println(sb.toString());
        System.out.println("-------------------------------------------------------");
        sb.setLength(0);

        long t1 = System.currentTimeMillis();
        double[] all = InterpolationUtil.espl3(y, max);
        System.out.println("插值算法话费时间" + (System.currentTimeMillis() - t1) + "ms");

        for (int i = 0; i < all.length; i++) {
            sb.append(all[i] + "  ");
        }
        System.out.println(sb.toString());
        System.out.println("-------------------------------------------------------");
    }

}
