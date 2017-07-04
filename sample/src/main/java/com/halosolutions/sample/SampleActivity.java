package com.halosolutions.sample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.halosolutions.rtmpdump.RTMP;
import com.halosolutions.rtmpdump.RTMPSuck;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.WeakHashMap;

/**
 * Created by luhonghai on 6/25/17.
 */

public class SampleActivity extends AppCompatActivity {
    private static final String TOKEN = "SC8rPbxHKit1XxjV7S5oxg";
    int port = 1935;
    boolean run = false;
    boolean record = false;
    private ProgressBar progress;
    private EditText etUrl;
    private EditText etDest;

    private String url;
    private String dest;
    private final WeakHashMap<String,RTMPSuck> rtmpSuckMap =new WeakHashMap<String, RTMPSuck>();
    private final WeakHashMap<String,RTMP> rtmpWeakHashMap =new WeakHashMap<String, RTMP>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btStart = (Button) findViewById(R.id.start);
        Button btStop = (Button) findViewById(R.id.stop);
        Button btStream = (Button) findViewById(R.id.stream);
        Button btRecord = (Button) findViewById(R.id.record);

        etUrl = (EditText) findViewById(R.id.url);
        etDest = (EditText) findViewById(R.id.dest);

        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);

        progress.setVisibility(View.INVISIBLE);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RTMPSuck rtmpSuck = new RTMPSuck();
                        rtmpSuckMap.put("abc" + port, rtmpSuck);
                        rtmpSuck.init("S:" + TOKEN,"rtmpe://f-radiko.smartstream.ne.jp/TBS/_definst_","TBS/_definst_", port++);
                    }
                }).start();
            }
        });

        btRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = etUrl.getText().toString().trim();
                dest = new File(Environment.getExternalStorageDirectory() , "test.flv").getAbsolutePath();
                if (!record) {
                    progress.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(dest.substring(0, dest.lastIndexOf("/")));
                            if (file.exists() && file.isDirectory()) {
                                try {
                                    File target = new File(dest);
                                    if (target.exists()) {
                                        FileUtils.forceDelete(new File(dest));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                RTMP rtmp = new RTMP();
                                rtmpWeakHashMap.put("abc", rtmp);
                                rtmp.init("S:" + TOKEN, dest);
                            } else {
                                Log.e("RTMTDUMP","Could not found dictionary: " + file.getAbsolutePath());
                            }
                        }
                    }).start();
                    record = true;
                }
            }
        });

    }
}
