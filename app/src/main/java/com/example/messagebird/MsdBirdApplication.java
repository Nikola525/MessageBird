package com.example.messagebird;

import android.app.Application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MsdBirdApplication extends Application {
    public Socket socket = null;
    public String host = "47.102.119.234";
    public int port = 8000;
    public ObjectInputStream ois = null;
    public ObjectOutputStream oos = null;
    BufferedReader bufferedreader = null;
    public boolean connectedtotheserver = false;
    public boolean logined = false;
    public long usrid = 0;
    public long registeredusrid = 0;
    public long partnerusrid = 0;
    public static final long MESSAGE_ACTIVITY = 39;
    public static final long MSGDETAIL_ACTIVITY = 37;
    public static final long CONTACT_ACTIVITY = 35;
    public static final long SETTING_ACTIVITY = 33;
    public static final long MAILACTIVITY_ACTIVITY = 31;
    public static long activeactivity = 0;

}
