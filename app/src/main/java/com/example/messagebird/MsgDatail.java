package com.example.messagebird;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import networkpacket.NetworkPacket;

public class MsgDatail extends Activity {
    private MsdBirdApplication app = null;
    public TextView tv_msgpanel = null;
    private TextView tv_partner = null;
    private EditText et_msg = null;
    private Button btn_send = null;
    BroadcastRcv broadcastrcv = null;
    Rcv rcv = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msgdetail);
        app = (MsdBirdApplication)getApplication();
        app.activeactivity = MsdBirdApplication.MSGDETAIL_ACTIVITY;
        tv_msgpanel = findViewById(R.id.tvMessagepanel_msgdetail);
        tv_partner = findViewById(R.id.tvPartner_msgdetail);
        et_msg = findViewById(R.id.edtMessage_msgdetail);
        btn_send = findViewById(R.id.btnSend_msgdetail);
        btn_send.setOnClickListener(new View.OnClickListener() {
            NetworkPacket sndpkt = null;
            @Override
            public void onClick(View v) {

                sndpkt = new NetworkPacket();
                sndpkt.type = NetworkPacket.TYPE_MESSAGE;
                sndpkt.to = app.partnerusrid;
                sndpkt.from = app.usrid;
                sndpkt.content = et_msg.getText().toString();

                new SendMessageInBackround().execute(sndpkt);
            }
        });

        Intent intent = getIntent();
        String partnerusrid = intent.getExtras().getString("partnerusrid");
        app.partnerusrid = Long.parseLong(partnerusrid);

        rcv = new Rcv();
        IntentFilter rcvfilter = new IntentFilter("android.intent.action.Rcv");
        registerReceiver(rcv, rcvfilter);
        tv_partner.setText(String.valueOf(app.partnerusrid));


    }





    public class Rcv extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(Integer.parseInt(intent.getStringExtra("type")) == NetworkPacket.TYPE_NEWMSG){
                //tv_msgpanel.setGravity(Gravity.LEFT);
                tv_msgpanel.append(String.valueOf(app.partnerusrid) + ": "+ intent.getStringExtra("content") + "\n");
            }
        }
    }

    private class  SendMessageInBackround extends AsyncTask<NetworkPacket, Void, Boolean> {
        @Override
        protected Boolean doInBackground(NetworkPacket... packets) {

            try {


                app.oos.writeObject(packets[0]);
                return true;


            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }
        @Override
        protected void onPostExecute(Boolean b) {
            if(b.booleanValue()){
                //tv_msgpanel.setGravity(Gravity.RIGHT);
                tv_msgpanel.append(String.valueOf(app.usrid) + ": "+ et_msg.getText() + "\n");
            }else{
                Toast.makeText(getApplicationContext(), "Sorry,Failed to send Message,\n" +
                        "Please try again. ", Toast.LENGTH_LONG).show();
            }
        }
    }

}
