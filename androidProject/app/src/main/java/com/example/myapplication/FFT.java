
package com.example.myapplication;
import android.util.Log;

/**
 * 快速傅里叶变换
 * 分治
 * Created by jfj
 */

public class FFT {
    public Complex[] fft(Complex[] x) {
        int n = x.length;
        //Log.i("n的值是",Integer.toString(n));
        //Log.i("n1的值是",Double.toString(x[0].real));
        if (n == 1) {
            return x;
        }
        // 如果信号数为奇数，使用dft计算
        //if (n % 2 != 0) {
        //    return dft(x);
        //}
        // 提取下标为偶数的原始信号值进行递归fft计算
        Complex[] even = new Complex[n / 2];
        for (int k = 0; k < n / 2; k++) {
            even[k] = x[2 * k];//用偶数项的系数赋值
        }
        Complex[] evenValue = fft(even);//分治

        // 提取下标为奇数的原始信号值进行fft计算
        Complex[] odd = new Complex[n / 2];
        for (int k = 0; k < n / 2; k++) {
            odd[k] = x[2 * k + 1];
        }

        Complex[] oddValue = fft(odd);

        // 下面偶数+奇数
        Complex[] result = new Complex[n];
        for (int k = 0; k < n / 2; k++) {
            // 欧拉公式e^(-i*2pi*k/N) = cos(-2pi*k/N) + i*sin(-2pi*k/N)
            double p = -2 * k * Math.PI / n;
            Complex m = new Complex(Math.cos(p), Math.sin(p));
            //Complex a=oddValue[k];
            //Log.i("a的值是"+Double.toString(oddValue[k].getClass()));
            result[k] = evenValue[k].plus(m.multiple(oddValue[k]));
            // exp(-2*(k+n/2)*PI/n) 相当于 -exp(-2*k*PI/n)，其中exp(-n*PI)=-1(欧拉公式);
            result[k + n / 2] = evenValue[k].minus(m.multiple(oddValue[k]));
        }
        return result;
    }

    /*public Complex[] dft(Complex[] x) {
        int n = x.length;
        if (n == 1)
            return x;
        Complex[] result = new Complex[n];
        for (int i = 0; i < n; i++) {
            result[i] = new Complex(0, 0);
            for (int k = 0; k < n; k++) {//两层循环
                //使用欧拉公式e^(-i*2pi*k/N) = cos(-2pi*k/N) + i*sin(-2pi*k/N)
                double p = -2 * k * Math.PI / n;
                Complex m = new Complex(Math.cos(p), Math.sin(p));
                result[i].plus( x[k].multiple(m) );//x[k]可以是虚数
            }
        }
        return result;
    }*/


    public double getFrequency(byte[] data, int SAMPLE_RATE, int FFT_NUM) {
        Complex[] f = new Complex[FFT_NUM];

        for (int i = 0; i < FFT_NUM; i++) {
            f[i] = new Complex(data[i], 0); //实部为正弦波FFT_N点采样，赋值为1
            //虚部为0
            //把数据传给f 数组
        }
        Log.i("数组的值是",Double.toString(f[0].real));
        fft(f);
        double[] s = new double[FFT_NUM / 2];//计算完的结果放入s
        //double[] s = new double[FFT_NUM];//计算完的结果放入s
        for (int i = 0; i < FFT_NUM/2; i++) {
            s[i] = f[i].getMod();
            //s[i]=10*Logjfj.log(s[i],10);
//            str += ""+s[i]+" ";
        }
//        Log.i("FFT","s: "+str);

        int fmax = 0;
        for (int i = 1; i < FFT_NUM/2; i++) {  //利用FFT的对称性，只取前一半进行处理
            if (s[i] > s[fmax])
                fmax = i;                          //计算最大频率的序号值
        }
//        Log.i("FFT","max index:"+fmax+" fft:"+f[fmax]+" s:"+s[fmax]);
        s[fmax]=10*Logjfj.log(s[fmax],10);
        if (s[fmax]<20){
            double fre= 0.0;
            return fre;
        }
        double fre = fmax * (double) SAMPLE_RATE / FFT_NUM;
        Log.i("FFT", "fre:" + fre);
        return fre;
    }


}


