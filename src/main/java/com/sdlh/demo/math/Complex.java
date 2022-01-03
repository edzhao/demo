package com.sdlh.demo.math;

import java.util.Scanner;

public class Complex {
    double realPart;
    double imaginaryPart;

    public static void main(String[] a) {
        Scanner input = new Scanner(System.in);
        int x1,y1,x2,y2;
        x1=input.nextInt();
        y1=input.nextInt();
        x2=input.nextInt();
        y2=input.nextInt();
        Complex c1 = new Complex(x1, y1);
        Complex c2 = new Complex(x2, y2);
        Complex c3=c1.add(c2);
        Complex c4=c1.sub(c2);
        Complex c5=c1.mul(c2);
        Complex c6=c1.div(c2);
        c3.print();
        c4.print();
        c5.print();
        c6.print();
    }

    public double getRealPart() {
        return realPart;
    }

    public void setRealPart(double realPart) {
        this.realPart = realPart;
    }

    public double getImaginaryPart() {
        return imaginaryPart;
    }

    public void setImaginaryPart(double imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }

    private void Complex(double realPart,double imaginaryPart){     // 供不带参数的构造方法调用
        this.realPart=realPart;
        this.imaginaryPart=imaginaryPart;
    }

    Complex(double realPart, double imaginaryPart){
        this.realPart=realPart;
        this.imaginaryPart=imaginaryPart;
    }

    Complex add(Complex a) {
        double real2 = a.getRealPart();
        double image2 = a.getImaginaryPart();
        double newReal = realPart + real2;
        double newImage = imaginaryPart + image2;
        Complex result = new Complex(newReal, newImage);
        return result;
    }

    Complex sub(Complex a){
        double real2 = a.getRealPart();
        double image2 = a.getImaginaryPart();
        double newReal = realPart - real2;
        double newImage = imaginaryPart - image2;
        Complex result = new Complex(newReal,newImage);
        return result;
    }

    Complex mul(Complex a) {
        double real2 = a.getRealPart();
        double image2 = a.getImaginaryPart();
        double newReal =  realPart*real2 - imaginaryPart*image2;
        double newImage = imaginaryPart*real2 + realPart*image2;
        Complex result = new Complex(newReal, newImage);
        return result;
    }

    Complex div(Complex a) {
        double real2 = a.getRealPart();
        double image2 = a.getImaginaryPart();
        double newReal = (realPart*real2 + imaginaryPart*image2)/(real2*real2 + image2*image2);
        double newImage = (imaginaryPart*real2 - realPart*image2)/(real2*real2 + image2*image2);
        Complex result = new Complex(newReal, newImage);
        return result;
    }

    public void print() {
        if (imaginaryPart > 0) {
            System.out.println(realPart + "+" + imaginaryPart + "i");
        } else if (imaginaryPart < 0) {
            System.out.println(realPart + "" + imaginaryPart + "i");//注意！
        } else {
            System.out.println(realPart);
        }
    }
}
