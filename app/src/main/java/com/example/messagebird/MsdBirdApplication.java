package com.example.messagebird;

import android.app.Application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MsdBirdApplication extends Application {
    public Socket socket = null;
    public String host = "10.253.219.39";
    public int port = 8000;
    public ObjectInputStream ois = null;
    public ObjectOutputStream oos = null;
}
