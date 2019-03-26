package com.example.messagebird;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import networkpacket.NetworkPacket;

public class BroadcastRcv extends BroadcastReceiver {
    private MsdBirdApplication app = null;
    Activity activity = null;
    BroadcastRcv(Activity act, MsdBirdApplication a){
        super();
        app = a;
        activity = act;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context.getApplicationContext(), intent.getExtras().get("message").toString(), Toast.LENGTH_LONG).show();
        if(app.activeactivity == MsdBirdApplication.MSGDETAIL_ACTIVITY){
            
            Toast.makeText(context.getApplicationContext(), "BDCST:MSGDETGAIL", Toast.LENGTH_LONG).show();
        }
    }
}
