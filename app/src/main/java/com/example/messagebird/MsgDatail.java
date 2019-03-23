package com.example.messagebird;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import networkpacket.NetworkPacket;

public class MsgDatail extends Activity {
    private MsdBirdApplication app = null;
    private TextView tv_msgpanel = null;
    private EditText et_msg = null;
    private Button btn_send = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msgdetail);
        app = (MsdBirdApplication)getApplication();
        tv_msgpanel = findViewById(R.id.tvMessagepanel_msgdetail);
        et_msg = findViewById(R.id.edtMessage_msgdetail);
        btn_send = findViewById(R.id.btnSend_msgdetail);
        btn_send.setOnClickListener(new View.OnClickListener() {
            NetworkPacket sndpkt = null;
            @Override
            public void onClick(View v) {

                sndpkt = new NetworkPacket();
                sndpkt.type = NetworkPacket.TYPE_LOGIN;
                sndpkt.content = et_msg.getText().toString();
                try{
                    app.oos.writeObject(sndpkt);
                }catch(IOException ios){
                    ios.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }

                //new LoginInBackGround().execute(sndpkt);
            }
        });
    }


    public class Rcv extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(getApplicationContext(), intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
        }
    }
}
