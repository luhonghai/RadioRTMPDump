package com.halosolutions.sample;

import android.app.Activity;
import android.os.Bundle;
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

public class SampleActivity extends Activity {

    boolean run = false;
    private ProgressBar progress;
    private EditText etUrl;
    private EditText etDest;

    private String url;
    private String dest;
    private final WeakHashMap<String,RTMPSuck> rtmpSuckMap =new WeakHashMap<String, RTMPSuck>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btStart = (Button) findViewById(R.id.start);
        Button btStop = (Button) findViewById(R.id.stop);
        Button btStream = (Button) findViewById(R.id.stream);

        etUrl = (EditText) findViewById(R.id.url);
        etDest = (EditText) findViewById(R.id.dest);

        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);

        progress.setVisibility(View.INVISIBLE);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = etUrl.getText().toString().trim();
                dest = etDest.getText().toString().trim();
                if (! run && url != "" && dest != "") {
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
                                RTMPSuck rtmpSuck = new RTMPSuck();
                                rtmpSuckMap.put("abc", rtmpSuck);
                                rtmpSuck.init("S:", 2135);
                            } else {
                                Log.e("RTMTDUMP","Could not found dictionary: " + file.getAbsolutePath());
                            }
                        }
                    }).start();
                    run = true;
                }
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.INVISIBLE);
                RTMPSuck rtmpSuck = rtmpSuckMap.get("abc");
                if (rtmpSuck != null) {
                    rtmpSuck.stop();
                    rtmpSuckMap.remove("abc");
                }
                run = false;
            }
        });

        btStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RTMPSuck rtmpSuck = rtmpSuckMap.get("abc");
                if (rtmpSuck != null) {
                    String stationId ="TBS";
                    String token = "jGreh9cItmGT0_nWB1LePQ";
                    rtmpSuck.update("S:" + token,"rtmpe://f-radiko.smartstream.ne.jp/" +  stationId + "/_definst_", stationId + "/_definst_");
                    // TBS < station ID
                    //rtmpe://f-radiko.smartstream.ne.jp/TBS/_definst_/simul-stream.stream
                    // -> rtmp://127.0.0.1:1935/TBS/_definst_/simul-stream.stream
                }
            }
        });
    }
}
