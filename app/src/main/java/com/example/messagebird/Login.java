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
    private EditText usrid = null;
    private EditText password = null;
    private Button login = null;
    private Button toregiter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (MsdBirdApplication)getApplication();
        usrid = findViewById(R.id.edtUsrid_login);
        usrid.setText(String.valueOf(app.registeredusrid));
        password = findViewById(R.id.edtPassword_login);
        login = findViewById(R.id.btnLogin_login);
        login.setOnClickListener(new View.OnClickListener() {
            NetworkPacket sndpkt = null;
            @Override
            public void onClick(View v) {


                sndpkt = new NetworkPacket();
                sndpkt.type = NetworkPacket.TYPE_LOGIN;
                sndpkt.password = password.getText().toString();
                sndpkt.from = Long.parseLong(usrid.getText().toString());

                new LoginInBackGround().execute(sndpkt);

            }
        });

        toregiter = findViewById(R.id.btnLinkToRegisterScreen_login);
        toregiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this,
                        Register.class);
                startActivity(intent);
            }
        });

    }

    private class  LoginInBackGround extends AsyncTask<NetworkPacket, Void, Boolean> {
        @Override
        protected Boolean doInBackground(NetworkPacket... packets) {

            try{


                app.oos.writeObject(packets[0]);

                NetworkPacket rcvpkt = (NetworkPacket)app.ois.readObject();

                if(rcvpkt.state == NetworkPacket.STATE_SUCCESS){
                    return true;
                }else{
                    return false;
                }




            }catch(Exception e){
                e.printStackTrace();
                return false;
            }


        }
        @Override
        protected void onPostExecute(Boolean b) {
            if(b.booleanValue()){

                app.logined = true;
                app.usrid = Long.parseLong(usrid.getText().toString());
                Intent intent = new Intent(Login.this,
                        MainActivity.class);
                startActivity(intent);




            }else{
                Toast.makeText(getApplicationContext(), "UserID or password is not correct,\n Please retry.", Toast.LENGTH_LONG).show();
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
