package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button b_start;
    private Button b_quanxian;
    private SoundAnalysisThread soundAnalysisThread;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO= 1;
    /**
     * 音乐跳变临界点
     */
    private static final int FREQUENCY_CRITICAL = 1000;
    public static int SOUND_MESSAGE = 1;
    /**
     * 声音请求权限信息
     */
    private static final int PERMISSION_AUDIORECORD = 2;

    private TextView Text1;


    private void showToast(String string){
        Toast.makeText(MainActivity.this,string,Toast.LENGTH_LONG).show();
    }
    private double currentFrequency=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Text1 = (TextView) findViewById(R.id.text1);
        Text1.setText("jfj");
        b_start = (Button) findViewById(R.id.start);
        b_quanxian =(Button) findViewById(R.id.quanxian);
        //b_start.setOnClickListener(this);
        b_start.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case (R.id.start):
                                b_start.setSelected(true);
                                startAnalysis();

                        }
                    }
                }
        );
        b_quanxian.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()){
                            case(R.id.quanxian):
                                Toast toast = Toast.makeText(getApplicationContext(),"自定义位置Toast", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.RECORD_AUDIO},
                                            MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                                    showToast("权限申请成功");
                                }else {
                                    showToast("权限已申请");
                                }
                        }
                    }
                }
        );


    }




    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Sound sound = (Sound) msg.obj;
                    updateText(sound);
                    break;
            }
        }
    };

    private void startAnalysis() {
        soundAnalysisThread = new SoundAnalysisThread(handler);
        soundAnalysisThread.start();
    }

    private void updateText(Sound sound) {
        double frequency = sound.mFrequency;
        if (Math.abs(frequency - currentFrequency) < FREQUENCY_CRITICAL) {
            Text1.setText("频率差异过小");
        } else {
            currentFrequency = frequency;
            Text1.setText(String.format("频率是%f", currentFrequency));
        }


    }

}