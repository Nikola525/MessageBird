package com.example.messagebird;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import networkpacket.NetworkPacket;

public class MsgService extends Service {

    private boolean finished = false;
    private MsdBirdApplication app = null;
    @Override
    public void onCreate() {
        super.onCreate();
        app = (MsdBirdApplication)getApplication();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("Service", "start");
        String uniqueActionString  = "com.androidbook.intents.testbc";
        Intent broadcastIntent = new Intent(uniqueActionString);

        broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        broadcastIntent.putExtra("message", "Hello world");
        sendBroadcast(broadcastIntent);
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {

        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


}
