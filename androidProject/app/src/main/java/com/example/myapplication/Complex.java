package com.example.myapplication;
public class Complex {
    /**
     * 实部
     */
    public double real;
    /**
     * 虚部
     */
    public double image;


    public Complex() {
        this(0.0, 0.0);
    }


    public Complex(double real, double image) {
        this.real = real;
        this.image = image;
    }


    @Override
    public String toString() {
        return "Complex{" +
                "real=" + real +
                ", image=" + image +
                '}';
    }

    public Complex plus(Complex other) {

        return new Complex(this.real + other.real, this.image + other.image);
    }

    public Complex multiple(Complex other) {
        double aa=this.real * other.real - this.image * other.image;
        double bb=this.real * other.image + this.image * other.real;
        return new Complex(aa,bb);
    }

    public Complex minus(Complex other) {

        return new Complex(this.real - other.real, this.image - other.image);
    }


    public final double getMod() {
        return Math.sqrt(this.real * this.real + this.image * this.image);
    }


}
