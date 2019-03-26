package com.example.messagebird;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import networkpacket.NetworkPacket;

public class MsgService extends Service {

    private boolean finished = false;
    private MsdBirdApplication app = null;
    private ThreadGroup myThreads = null;
    @Override
    public void onCreate() {
        super.onCreate();
        app = (MsdBirdApplication)getApplication();
        myThreads = new ThreadGroup("ServiceWorker");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("Service", "start");
        new Thread(myThreads, new ServiceWorker(),
                "BackgroundService")
                .start();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        myThreads.interrupt();
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    class ServiceWorker implements Runnable
    {
        public void run() {
            final String TAG = "ServiceWorker:" +
                    Thread.currentThread().getId();
            Log.d(TAG, "run...");
            while(!finished){
                try{

                    String str = app.bufferedreader.readLine();
                    JSONObject jsonobj = new JSONObject(str);

                    String uniqueActionString  = "android.intent.action.Rcv";
                    Intent broadcastIntent = new Intent(uniqueActionString);

                    broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    broadcastIntent.putExtra("type", jsonobj.get("type").toString());
                    broadcastIntent.putExtra("parnerusrid", jsonobj.get("from").toString());
                    broadcastIntent.putExtra("content", jsonobj.get("content").toString());
                    broadcastIntent.putExtra("name", jsonobj.get("content").toString());


                    sendBroadcast(broadcastIntent);
                    Log.i("Service", broadcastIntent.toString().toString());
                }catch (Exception e){
                    e.printStackTrace();
                    try{
                        Thread.sleep(3000);
                    }catch(Exception exc){
                        exc.printStackTrace();
                    }

                }


            }

        }
    }

}
