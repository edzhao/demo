package com.sdlh.demo.dsp.algorithm;

import java.applet.Applet;
import java.awt.*;
import java.util.stream.Stream;

public class Interpolation extends Applet {

    public double espl3(double[] x,double[] y,double[] dy,double[] ddy,double[] t,double[] z,double[] dz,double[] ddz) {
        int i,j,n=x.length;
        double[] a=new double[n-1];
        double[] b=new double[n-1];
        double[] c=new double[n-1];
        double[] u=new double[n];
        double[] v=new double[n];
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

        alpha=0;beta=0;

        for(i=0;i<n-1;i++) {
            h=x[i+1]-x[i];
            alpha+=h*(y[i]+y[i+1]);
            beta=h*h*h*(ddy[i]+ddy[i+1]);
        }

        return (alpha/2-beta/24);
    }

    public void paint(Graphics g) {
        double[] x=new double[37];
        double[] y=new double[37];
        double[] dy=new double[37];
        double[] ddy=new double[37];
        double[] t=new double[9];
        double[] z=new double[9];
        double[] dz=new double[9];
        double[] ddz=new double[9];
        double f=0;
        int i;

        StringBuilder sb = new StringBuilder();
        for(i=0;i<37;i++) {
            x[i]=i*Math.PI/18.0;
            sb.append("x[" + i + "]=" + x[i] + "\t");
            y[i]=Math.sin(x[i]);
        }
        System.out.println(sb.toString());
        System.out.println("-------------------------------------------------------");

        sb.setLength(0);
        for(i=0;i<9;i++) {
            t[i]=(2*i+1)*Math.PI/36.0;
            sb.append("t[" + i + "]=" + t[i] + "\t");
        }
        System.out.println(sb.toString());
        System.out.println("-------------------------------------------------------");

        f=espl3(x,y,dy,ddy,t,z,dz,ddz);

        sb.setLength(0);
        for (i = 0; i < dy.length; i++) {
            sb.append("dy[" + i + "]=" + dy[i] + "\t");
        }
        System.out.println(sb.toString());
        System.out.println("-------------------------------------------------------");

        sb.setLength(0);
        for (i = 0; i < ddy.length; i++) {
            sb.append("ddy[" + i + "]=" + ddy[i] + "\t");
        }
        System.out.println(sb.toString());
        System.out.println("-------------------------------------------------------");

        sb.setLength(0);
        for (i = 0; i < dz.length; i++) {
            sb.append("dz[" + i + "]=" + dz[i] + "\t");
        }
        System.out.println(sb.toString());
        System.out.println("-------------------------------------------------------");

        sb.setLength(0);
        for (i = 0; i < ddz.length; i++) {
            sb.append("ddz[" + i + "]=" + ddz[i] + "\t");
        }
        System.out.println(sb.toString());
        System.out.println("-------------------------------------------------------");

        g.drawString("x(单位：度)       y                              y'                              y''",10,10);

        for(i=0;i<37;i++)
            g.drawString(""+(10*i)+"  "+y[i]+"  "+dy[i]+"  "+ddy[i],10,20+10*i);

        g.drawString("近似积分值为"+f,10,390);
        g.drawString("t(单位：度)       f(t)                           f'(t)                           f''(t)",10,400);

        for(i=0;i<9;i++)
            g.drawString(""+(5+10*i)+"  "+z[i]+"  "+dz[i]+"  "+ddz[i],10,410+10*i);

    }
}
