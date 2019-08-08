package com.example.myapplication;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.myapplication.MainActivity;
import com.example.myapplication.FFT;

/**
 * 声音分析工具
 * Created by jfj;
 */
//继承
public class SoundAnalysisThread extends Thread {
    /**
     * 线程通讯的handler
     */
    private Handler handler;
    /**
     * 傅里叶变化工具类
     */
    private FFT fft = new FFT();

    /**
     * 可能存在的采样频率
     */
    private static final int[] SAMPLE_RATES_LIST = {11025, 8000, 22050, 44100, 16000};
    /**
     * 采样频率对应的采样点数
     */
    private static final int[] SAMPLE_COUNT = {8 * 1024,
            4 * 1024, 16 * 1024, 32 * 1024, 8 * 1024};
    /**
     * 采样频率
     */
    private static final int sampleRate = 44100;
    /**
     * 采样点数
     */
    private int sampleCount = 8*1024;

    //32 * 1024
    /**
     * 声音的信息
     */
    private Sound sound = new Sound();
    /**
     * 当前的频率
     */
    private double currentFrequency;
    /**
     * 当前的声音
     */
    private double currentVolume;
    /**
     * 声音采集频率
     */
    private AudioRecord mAudioRecord;

    private int mBufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);//计算最小缓冲区

    public SoundAnalysisThread(Handler handler) {
        this.handler = handler;
    }
    int[] buffer = new int[mBufferSizeInBytes];
    //private int sampleCount=mBufferSizeInBytes;

    @Override
    public void run() {
        super.run();
        //Log.i("xiaozhu----------", "run" + sampleCount);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, //设定录音来源为主麦克风
                sampleRate, //音频采样率，越高质量越好。常用频率为44100Hz,可以在所有的设备上完美运行，
                AudioFormat.CHANNEL_IN_MONO, //单声道
                AudioFormat.ENCODING_PCM_16BIT,
                mBufferSizeInBytes);
        Log.i("最小的大小是" ,Integer.toString(mBufferSizeInBytes));
        //Log.i("初始化状态",""+mAudioRecord.getRecordingState());
        //getMinBufferSize采集数据需要的缓冲区的大小
        //getMinBufferSize (int sampleRateInHz, int channelConfig, int audioFormat)与前面相同
        //Log.i("xiaozhu----------", "STATE_INITIALIZED" + mAudioRecord.getState());
        if (mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED)
            throw new RuntimeException("初始化失败");

        mAudioRecord.startRecording();//开始录音
        byte[] bufferRead = new byte[sampleCount];
        int lenght;

        while ((lenght = mAudioRecord.read(bufferRead, 0, sampleCount)) > 0) {//这里是sampleCount
            currentFrequency = fft.getFrequency(bufferRead, sampleRate, sampleCount);
            sound.mFrequency = currentFrequency;
            Message message = Message.obtain();//取一个消息
            message.obj = sound;
            message.what = MainActivity.SOUND_MESSAGE;//消息类型
            handler.sendMessage(message);//发送
            //Log.i("xiaozhu----------", "currentFrequency" + currentFrequency + "---" + currentVolume);
            if (currentFrequency > 0) {
                //try {
                    //if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                       // mAudioRecord.stop();
                  //  }
                    //Thread.sleep(20);
                    //if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                      //  mAudioRecord.startRecording();
                    //}
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
              //  }
            }
        }
    }

    /**
     * 停止解析
     */
    public void close() {
        if (mAudioRecord != null && mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            mAudioRecord.stop();
            mAudioRecord.release();
        }

    }


}
