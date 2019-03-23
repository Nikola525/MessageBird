package com.example.messagebird;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import networkpacket.NetworkPacket;

public class Login extends Activity {
    private MsdBirdApplication app = null;
    private EditText email = null;
    private EditText password = null;
    private Button login = null;
    Rcv broadcastRec = null;
    BroadcastRcv rcv = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (MsdBirdApplication)getApplication();
        email = findViewById(R.id.edtEamil_login);
        password = findViewById(R.id.edtPassword_login);
        broadcastRec = new Rcv();
        rcv = new BroadcastRcv();
        login = findViewById(R.id.btnLogin_login);
        login.setOnClickListener(new View.OnClickListener() {
            NetworkPacket sndpkt = null;
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,
                        MsgService.class);
                startService(intent);
                sndpkt = new NetworkPacket();
                sndpkt.type = NetworkPacket.TYPE_LOGIN;
                sndpkt.email = email.getText().toString();
                sndpkt.password = password.getText().toString();
                sndpkt.content = new String("test");
                //new LoginInBackGround().execute(sndpkt);
            }
        });

    }

    private class  LoginInBackGround extends AsyncTask<NetworkPacket, Void, String> {
        @Override
        protected String doInBackground(NetworkPacket... packets) {

            try{


                app.oos.writeObject(packets[0]);
                Log.i("content field:", packets[0].content);

                return new String("Success.");



            }catch(Exception e){
                e.printStackTrace();
                return new String("Failed.");
            }


        }
        @Override
        protected void onPostExecute(String content) {
            if(true){
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(Login.this,MainActivity.class);
                //startActivity(intent);


            }else{
                Toast.makeText(getApplicationContext(), "id or password is incorrect.", Toast.LENGTH_LONG).show();
            }
        }

    }

    public class Rcv extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(getApplicationContext(), "HHH", Toast.LENGTH_LONG).show();
        }
    }


}
