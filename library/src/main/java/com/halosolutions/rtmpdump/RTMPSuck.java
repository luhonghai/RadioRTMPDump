package com.halosolutions.rtmpdump;

public class RTMPSuck {
    static {
        System.loadLibrary("rtmp");
        System.loadLibrary("rtmpsuck");
    }
    public native void update(String token, String tcUrl, String app);
    public native void init(String token, int port);
    public void init(String token) {
        init(token, 1935);
    }
    public native void stop();
}
