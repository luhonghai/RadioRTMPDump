package com.halosolutions.rtmpdump;

public class RTMPSuck {
    static {
        System.loadLibrary("rtmp");
        System.loadLibrary("rtmpsuck");
    }
    public native void update(String token, String tcUrl, String app);
    public native void init(String token, String tcUrl, String app, int port);
    public native void stop();
}
