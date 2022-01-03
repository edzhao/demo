package com.sdlh.demo.dsp.algorithm;

import java.applet.Applet;
import java.awt.*;

public class Akima extends Applet {
    public double eespl(double x,double h,int n,double[] y,double t,double[] s)
    {
        int k;
        double g0,g1;
        double[] u=new double[n+3];
        for(k=0;k<n-1;k++)u[k+2]=(y[k+1]-y[k])/h;
        u[1]=2*u[2]-u[3];u[0]=2*u[1]-u[2];
        u[n+1]=2*u[n]-u[n-1];u[n+2]=2*u[n+1]-u[n];
        for(k=0;k<n-1;k++)if(t<x+k*h+h)break;
        s[0]=Math.abs(u[k+1]-u[k]);s[1]=Math.abs(u[k+2]-u[k+1]);
        s[2]=Math.abs(u[k+3]-u[k+2]);s[3]=Math.abs(u[k+4]-u[k+3]);
        if(s[0]+s[2]<1e-12)g0=(u[k+1]+u[k+2])/2;
        else g0=(s[2]*u[k+1]+s[0]*u[k+2])/(s[2]+s[0]);
        if(s[1]+s[3]<1e-12)g1=(u[k+2]+u[k+3])/2;
        else g1=(s[3]*u[k+2]+s[1]*u[k+3])/(s[3]+s[1]);
        s[0]=y[k];s[1]=g0;s[2]=(3*u[k+2]-2*g0-g1)/h;s[3]=(g1+g0-2*u[k+2])/h/h;
        g0=t-x-k*h;g1=((s[3]*g0+s[2])*g0+s[1])*g0+s[0];
        return g1;
    }

    public void paint(Graphics g)
    {double[] y=new double[11];
        double[] s=new double[4];
        double f,t=-1.0;
        int i;
        for(i=0;i<11;i++){y[i]=1.0/(1+25*t*t);t+=0.2;}
        t=-0.85;f=eespl(-1.0,0.2,11,y,t,s);
        g.drawString("t="+t+"  f(t)="+f+"  实际值="+(1.0/(1+25*t*t)),10,20);
        g.drawString("三次插值多项式系数为"+s[0]+"  "+s[1],10,40);
        g.drawString("                   "+s[2]+"   "+s[3],10,60);
        t=0.15;f=eespl(-1.0,0.2,11,y,t,s);
        g.drawString("t="+t+"  f(t)="+f+"  实际值="+(1.0/(1+25*t*t)),10,100);
        g.drawString("三次插值多项式系数为"+s[0]+"  "+s[1],10,120);
        g.drawString("                   "+s[2]+"  "+s[3],10,140);
    }

}
